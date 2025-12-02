package tecnm.itch.fonda.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tecnm.itch.fonda.client.ClienteClient.ClienteDto;
// CAMBIO: Import local
import tecnm.itch.fonda.dto.EmpleadoDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaDto {
	private Integer id_venta;

	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime fecha_venta;

	private Integer id_cliente;
	private Integer id_empleado;

	private Integer id_reserva;
	private List<DetalleVentaDto> detalles;

	private ClienteDto cliente;
	private EmpleadoDto empleado;

	@JsonProperty(access = Access.READ_ONLY)
	private Double total;
}