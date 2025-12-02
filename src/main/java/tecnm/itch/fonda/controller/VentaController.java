package tecnm.itch.fonda.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import tecnm.itch.fonda.dto.VentaDto;
import tecnm.itch.fonda.dto.VentaResponseDto;
import tecnm.itch.fonda.service.VentaService;
import tecnm.itch.fonda.service.implement.TicketPdfService;
// CAMBIO: Import local
import tecnm.itch.fonda.dto.EmpleadoDto;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/venta") 
public class VentaController {

	private final VentaService ventaService;
	private final TicketPdfService ticketPdfService; 
	private final EmpleadoClient empleadoClient; 

	@PostMapping
	public ResponseEntity<VentaDto> crearVenta(@RequestBody VentaDto ventaDto) {
		VentaDto guardada = ventaService.createVenta(ventaDto);
		return new ResponseEntity<>(guardada, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<VentaDto> getVentaById(@PathVariable("id") Integer ventaId) {
		VentaDto venta = ventaService.getVentaById(ventaId);
		return ResponseEntity.ok(venta);
	}

	@GetMapping
	public ResponseEntity<List<VentaResponseDto>> getAllVentas(@RequestParam(required = false) String fecha) {
		List<VentaResponseDto> ventas;
		if (fecha != null && !fecha.isEmpty()) {
			ventas = ventaService.findVentasByFecha(fecha);
		} else {
			ventas = ventaService.getAllVentas();
		}
		return ResponseEntity.ok(ventas);
	}

	@PutMapping("{id}")
	public ResponseEntity<VentaDto> updateVenta(@PathVariable("id") Integer ventaId,
			@RequestBody VentaDto updateVenta) {
		VentaDto venta = ventaService.updateVenta(ventaId, updateVenta);
		return ResponseEntity.ok(venta);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteVenta(@PathVariable("id") Integer ventaId) {
		ventaService.deleteVenta(ventaId);
		return ResponseEntity.ok("Venta eliminada");
	}

	@GetMapping("/{id}/ticket")
	public ResponseEntity<InputStreamResource> descargarTicket(@PathVariable("id") Integer id) {
		try {
			byte[] pdfBytes = ticketPdfService.generarTicket(id); 
			ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=Ticket_Venta_No_" + id + ".pdf");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		} catch (Exception e) {
			System.err.println("¡ERROR AL GENERAR EL TICKET! ---->");
			e.printStackTrace();
			System.err.println("<---- FIN DEL ERROR");
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
		return empleadoClient.getEmpleadoById(venta.getId_empleado());
	}
}