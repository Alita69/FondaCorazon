package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tecnm.itch.fonda.dto.AtenderDto;

@FeignClient(name = "reservaciones", contextId = "atenderClient")
public interface AtenderClient {
	@PostMapping("/api/atender")
	void crearAtender(@RequestBody AtenderDto atenderDto);
}
