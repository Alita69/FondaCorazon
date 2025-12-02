package tecnm.itch.fonda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// CORRECCIÓN: Conectar directo a Restaurante (Railway) usando la variable
@FeignClient(name = "restaurante", url = "${service.url.restaurante}", path = "/api/cliente")
public interface ClienteClient {

	@GetMapping("/{id}")
	ClienteDto getClienteById(@PathVariable("id") Integer id);

	@JsonIgnoreProperties(ignoreUnknown = true)
	class ClienteDto {
		@JsonProperty("id_cliente")
		private Integer id_cliente;

		@JsonProperty("nombre_cliente")
		private String nombreCliente;

		@JsonProperty("correo_cliente")
		private String correoCliente;

		@JsonProperty("telefono_cliente")
		private String telefonoCliente;

		// Getters y Setters
		public Integer getId_cliente() { return id_cliente; }
		public void setId_cliente(Integer id_cliente) { this.id_cliente = id_cliente; }
		public String getNombreCliente() { return nombreCliente; }
		public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
		public String getCorreoCliente() { return correoCliente; }
		public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
		public String getTelefonoCliente() { return telefonoCliente; }
		public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
	}
}