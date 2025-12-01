package tecnm.itch.fonda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtenderDto {
	private Integer idVenta;
	private Integer idEmpleado;
	// No necesitas más campos para esta operación

	public AtenderDto(Integer idVenta, Integer idEmpleado) {
		this.idVenta = idVenta;
		this.idEmpleado = idEmpleado;
	}
}