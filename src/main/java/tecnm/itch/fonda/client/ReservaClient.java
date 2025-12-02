package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // <--- NO OLVIDES ESTE IMPORT

@FeignClient(name = "reservaciones", url = "${service.url.reservaciones}", contextId = "reservaClient")
public interface ReservaClient {

    // Agregamos @RequestBody String body para forzar el envío de datos y evitar el error 411
	@PutMapping("/api/reserva/{id}/confirmar")
	void confirmarReserva(@PathVariable("id") Integer idReserva, @RequestBody String body);
}