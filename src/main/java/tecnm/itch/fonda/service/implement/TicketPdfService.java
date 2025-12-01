package tecnm.itch.fonda.service.implement;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import tecnm.itch.fonda.ResourceNotFoundException;
import tecnm.itch.fonda.client.ClienteClient;
import tecnm.itch.fonda.client.ClienteClient.ClienteDto;
import tecnm.itch.fonda.client.EmpleadoClient;
import tecnm.itch.fonda.entity.DetalleVenta;
import tecnm.itch.fonda.entity.Venta;
import tecnm.itch.fonda.repository.DetalleVentaRepository;
import tecnm.itch.fonda.repository.VentaRepository;
import tecnm.itch.reservaciones.dto.EmpleadoDto;

@Service
@AllArgsConstructor
public class TicketPdfService {

	private final VentaRepository ventaRepository;
	private final DetalleVentaRepository detalleVentaRepository;

	private final EmpleadoClient empleadoClient;
	private final ClienteClient clienteClient;

	// --- CORRECCIÓN 1: FUENTES AÑADIDAS ---
	private static final Font FONT_TITULO = new Font(Font.HELVETICA, 20, Font.BOLD);
	private static final Font FONT_SUBTITULO = new Font(Font.HELVETICA, 18, Font.BOLD); // <-- Faltaba esta
	private static final Font FONT_HEADER = new Font(Font.HELVETICA, 11, Font.BOLD); // <-- Faltaba esta
	private static final Font FONT_TEXTO = new Font(Font.HELVETICA, 10, Font.NORMAL);
	private static final Font FONT_TOTAL = new Font(Font.HELVETICA, 12, Font.BOLD);
	private static final Font FONT_TABLA_HEADER = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
	private static final Font FONT_TABLA_BODY = new Font(Font.HELVETICA, 9, Font.NORMAL);

	private static final Locale localeMx = new Locale("es", "MX");
	private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeMx);
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, h:mm a",
			localeMx);

	@Transactional(readOnly = true)
	public byte[] generarTicket(Integer idVenta) {
		// 1. Obtener datos de la Venta
		Venta venta = ventaRepository.findById(idVenta)
				.orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + idVenta));
		List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(idVenta);

		// --- 2. BUSCAR LOS NOMBRES USANDO LOS CLIENTS ---
		String nombreEmpleado = "No asignado";
		try {
			EmpleadoDto empleado = empleadoClient.getEmpleadoById(venta.getIdVenta());
			if (empleado != null && empleado.getNombre() != null) {
				nombreEmpleado = empleado.getNombre();
			}
		} catch (Exception e) {
			System.err.println("No se pudo encontrar el empleado para la venta ID: " + venta.getIdVenta());
			if (venta.getIdEmpleado() != null) {
				nombreEmpleado = "Empleado (ID: " + venta.getIdEmpleado() + ")";
			}
		}

		String nombreCliente = "Mostrador";
		if (venta.getIdCliente() != null) {
			try {
				ClienteDto cliente = clienteClient.getClienteById(venta.getIdCliente());
				if (cliente != null && cliente.getNombreCliente() != null) {
					nombreCliente = cliente.getNombreCliente();
				}
			} catch (Exception e) {
				System.err.println("No se pudo encontrar el nombre del cliente con ID: " + venta.getIdCliente());
				nombreCliente = "Cliente (ID: " + venta.getIdCliente() + ")";
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);

		try {
			PdfWriter.getInstance(document, baos);
			document.open();

			// --- Encabezado de Empresa y Ticket ---
			Paragraph titulo = new Paragraph("Fonda KOKORO", FONT_TITULO);
			document.add(titulo);
			document.add(new Paragraph("Av. ITCh 123, Colonia Tecnológico", FONT_TEXTO));
			document.add(new Paragraph("Tel: 55-1234-5678", FONT_TEXTO));
			document.add(new Paragraph("kokoro@fonda.com", FONT_TEXTO));

			// Ahora 'FONT_SUBTITULO' sí existe
			Paragraph subtitulo = new Paragraph("TICKET DE VENTA", FONT_SUBTITULO);
			subtitulo.setAlignment(Element.ALIGN_RIGHT);
			document.add(subtitulo);

			Paragraph ventaIdPar = new Paragraph("Venta ID: " + venta.getIdVenta(), FONT_TEXTO);
			ventaIdPar.setAlignment(Element.ALIGN_RIGHT);
			document.add(ventaIdPar);

			Paragraph fecha = new Paragraph("Fecha: " + dateFormatter.format(venta.getFechaVenta()), FONT_TEXTO);
			fecha.setAlignment(Element.ALIGN_RIGHT);
			document.add(fecha);
			document.add(new Paragraph(" ")); // Espacio

			// --- 3. USAR LAS VARIABLES (Ahora 'FONT_HEADER' sí existe) ---
			document.add(new Paragraph("Atendido por: " + nombreEmpleado, FONT_HEADER));
			document.add(new Paragraph("Cliente: " + nombreCliente, FONT_HEADER));
			document.add(new Paragraph("Origen: "
					+ (venta.getIdReserva() != null ? "Reserva (ID: " + venta.getIdReserva() + ")" : "Venta en Local"),
					FONT_HEADER));

			document.add(new Paragraph(" ")); // Espacio

			// --- CORRECCIÓN 2: BLOQUE DUPLICADO ELIMINADO ---
			// (Aquí estaba el código repetido, ya se quitó)

			// --- 4. Tabla de Detalles ---
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1f, 5f, 2f, 2f });
			addTableHeader(table, "Cant.");
			addTableHeader(table, "Producto");
			addTableHeader(table, "P. Unitario");
			addTableHeader(table, "Subtotal");

			for (DetalleVenta detalle : detalles) {
				double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
				table.addCell(createCell(String.valueOf(detalle.getCantidad()), Element.ALIGN_RIGHT, FONT_TABLA_BODY));
				table.addCell(
						createCell(detalle.getProducto() != null ? detalle.getProducto().getNombre() : "Sin nombre",
								Element.ALIGN_LEFT, FONT_TABLA_BODY));
				table.addCell(createCell(currencyFormatter.format(detalle.getPrecioUnitario()), Element.ALIGN_RIGHT,
						FONT_TABLA_BODY));
				table.addCell(createCell(currencyFormatter.format(subtotal), Element.ALIGN_RIGHT, FONT_TABLA_BODY));
			}
			document.add(table);
			document.add(new Paragraph(" ")); // Espacio

			// --- 5. Totales ---
			PdfPTable totalesTable = new PdfPTable(2);
			totalesTable.setWidthPercentage(50);
			totalesTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
			totalesTable.setWidths(new float[] { 2f, 2f });

			double totalFinal = venta.getTotal();
			double subtotalDesglosado = totalFinal / 1.16;
			double ivaDesglosado = totalFinal - subtotalDesglosado;

			totalesTable.addCell(createTotalCell("Subtotal:", Element.ALIGN_RIGHT, FONT_TEXTO));
			totalesTable.addCell(
					createTotalCell(currencyFormatter.format(subtotalDesglosado), Element.ALIGN_RIGHT, FONT_TEXTO));
			totalesTable.addCell(createTotalCell("IVA (16%):", Element.ALIGN_RIGHT, FONT_TEXTO));
			totalesTable
					.addCell(createTotalCell(currencyFormatter.format(ivaDesglosado), Element.ALIGN_RIGHT, FONT_TEXTO));
			totalesTable.addCell(createTotalCell("Total:", Element.ALIGN_RIGHT, FONT_TOTAL));
			totalesTable
					.addCell(createTotalCell(currencyFormatter.format(totalFinal), Element.ALIGN_RIGHT, FONT_TOTAL));

			document.add(totalesTable);
			Paragraph gracias = new Paragraph("¡Gracias por su compra! :)", FONT_TEXTO);
			gracias.setAlignment(Element.ALIGN_CENTER);
			document.add(gracias);
			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return baos.toByteArray();
	}

	// --- Métodos Helper (sin cambios) ---
	private void addTableHeader(PdfPTable table, String headerText) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new Color(50, 50, 50));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPhrase(new Phrase(headerText, FONT_TABLA_HEADER));
		cell.setPadding(4);
		table.addCell(cell);
	}

	private PdfPCell createCell(String text, int alignment, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPadding(4);
		return cell;
	}

	private PdfPCell createTotalCell(String text, int alignment, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPadding(2);
		return cell;
	}
}