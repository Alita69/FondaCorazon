package tecnm.itch.fonda.controller;

import java.io.ByteArrayInputStream; // Importar
import java.util.List;

import org.springframework.core.io.InputStreamResource; // Importar
import org.springframework.http.HttpHeaders; // Importar
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Importar
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tecnm.itch.fonda.client.EmpleadoClient;
import tecnm.itch.fonda.dto.EmpleadoDto;
import tecnm.itch.fonda.dto.VentaDto;
import tecnm.itch.fonda.dto.VentaResponseDto;
import tecnm.itch.fonda.service.VentaService;
import tecnm.itch.fonda.service.implement.TicketPdfService; // 1. IMPORTAR EL NUEVO SERVICIO

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/venta") // Tu ruta base es "venta" (singular)
public class VentaController {

	private final VentaService ventaService;
	private final TicketPdfService ticketPdfService; // 2. INYECTAR EL SERVICIO
	private final EmpleadoClient empleadoClient; // ✅ CORRECTO

	// Crear venta (con sus detalles)
	@PostMapping
	public ResponseEntity<VentaDto> crearVenta(@RequestBody VentaDto ventaDto) {
		VentaDto guardada = ventaService.createVenta(ventaDto);
		return new ResponseEntity<>(guardada, HttpStatus.CREATED);
	}

	// Obtener venta por id
	@GetMapping("{id}")
	public ResponseEntity<VentaDto> getVentaById(@PathVariable("id") Integer ventaId) {
		VentaDto venta = ventaService.getVentaById(ventaId);
		return ResponseEntity.ok(venta);
	}

	@GetMapping
	public ResponseEntity<List<VentaResponseDto>> getAllVentas(@RequestParam(required = false) String fecha) {

		// 1. Cambia el tipo de la variable a VentaResponseDto
		List<VentaResponseDto> ventas;

		if (fecha != null && !fecha.isEmpty()) {
			// Esto ahora coincide: el servicio devuelve List<VentaResponseDto>
			ventas = ventaService.findVentasByFecha(fecha);
		} else {
			// Esto también coincide: el servicio devuelve List<VentaResponseDto>
			ventas = ventaService.getAllVentas();
		}

		// 2. El ResponseEntity.ok(ventas) ahora devuelve la lista del tipo correcto
		return ResponseEntity.ok(ventas);
	}

	// Actualizar venta (reemplaza/ajusta detalles)
	@PutMapping("{id}")
	public ResponseEntity<VentaDto> updateVenta(@PathVariable("id") Integer ventaId,
			@RequestBody VentaDto updateVenta) {
		VentaDto venta = ventaService.updateVenta(ventaId, updateVenta);
		return ResponseEntity.ok(venta);
	}

	// Eliminar venta
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteVenta(@PathVariable("id") Integer ventaId) {
		ventaService.deleteVenta(ventaId);
		return ResponseEntity.ok("Venta eliminada");
	}

	// 3. AÑADIR EL NUEVO ENDPOINT PARA EL TICKET
	@GetMapping("/{id}/ticket")
	public ResponseEntity<InputStreamResource> descargarTicket(@PathVariable("id") Integer id) {

		// 1. Añadimos un try...catch
		try {
			byte[] pdfBytes = ticketPdfService.generarTicket(id); // <-- El error está aquí dentro
			ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=Ticket_Venta_No_" + id + ".pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));

		} catch (Exception e) {
			// 2. Si algo falla, lo imprimimos en la consola
			System.err.println("¡ERROR AL GENERAR EL TICKET! ---->");
			e.printStackTrace(); // <-- ESTO FORZARÁ A QUE EL ERROR SALGA EN LA CONSOLA
			System.err.println("<---- FIN DEL ERROR");

			// 3. Devolvemos el error 500
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}/estado")
	public ResponseEntity<VentaDto> actualizarEstado(@PathVariable("id") Integer id,
			@RequestParam("estado") String nuevoEstado) {

		VentaDto ventaActualizada = ventaService.updateEstado(id, nuevoEstado);
		return ResponseEntity.ok(ventaActualizada);
	}

	@GetMapping("/{idVenta}/empleado")
	public EmpleadoDto obtenerEmpleadoDeVenta(@PathVariable Integer idVenta) {

		VentaDto venta = ventaService.getVentaById(idVenta);

		if (venta.getId_empleado() == null) {
			throw new RuntimeException("La venta no tiene mesero asignado");
		}

		// LLAMAR AL SERVICIO LOCAL
		return empleadoClient.getEmpleadoById(venta.getId_empleado());
	}

}