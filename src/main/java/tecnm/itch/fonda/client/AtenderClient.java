package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tecnm.itch.fonda.dto.AtenderDto;

// CORRECCIÓN: Agregamos url = "${service.url.reservaciones}" para conectar directo a Google Cloud
@FeignClient(name = "reservaciones", url = "${service.url.reservaciones}", contextId = "atenderClient")
public interface AtenderClient {
	
	@PostMapping("/api/atender")
	void crearAtender(@RequestBody AtenderDto atenderDto);
}