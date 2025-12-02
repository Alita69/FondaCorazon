package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tecnm.itch.fonda.dto.EmpleadoDto;

@FeignClient(name = "reservaciones", url = "${service.url.reservaciones}", contextId = "empleadoClient")
public interface EmpleadoClient {

	@GetMapping("/api/empleado/venta/{idVenta}")
	EmpleadoDto findEmpleadoByVentaId(@PathVariable("idVenta") Integer idVenta);

	@GetMapping("/api/empleado/{id}")
	EmpleadoDto getEmpleadoById(@PathVariable("id") Integer idEmpleado);
}