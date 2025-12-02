package tecnm.itch.fonda.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class VentaResponseDto {
	private Integer id_venta;
	private LocalDateTime fecha_venta;
	private Double total;
	private String estado;

	private ClienteInfo cliente;
	private EmpleadoInfo empleado;
	private Integer id_reserva;

	@Getter
	@Setter
	public static class ClienteInfo {
		private String nombre_cliente;
	}

	@Getter
	@Setter
	public static class EmpleadoInfo {
		private Integer id_empleado; // <--- ¡ESTE ES EL CAMPO QUE FALTABA!
		private String nombre;
	}
}