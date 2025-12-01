package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// 1. Apunta al servicio correcto: "reservaciones"
@FeignClient(name = "reservaciones", contextId = "reservaClient")
public interface ReservaClient {

	// 2. Usa el path correcto del ReservarController:
	// @RequestMapping("/api/reserva") + @PutMapping("/{id}/confirmar")
	@PutMapping("/api/reserva/{id}/confirmar")
	void confirmarReserva(@PathVariable("id") Integer idReserva);
}