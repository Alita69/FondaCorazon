package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// CORRECCIÓN: Conectar directo a Reservaciones
@FeignClient(name = "reservaciones", url = "${service.url.reservaciones}", contextId = "reservaClient")
public interface ReservaClient {

	@PutMapping("/api/reserva/{id}/confirmar")
	void confirmarReserva(@PathVariable("id") Integer idReserva);
}