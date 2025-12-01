package tecnm.itch.fonda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDto {
	private Integer id_detalle;
	private Integer id_producto;
	private Integer cantidad;
	private Double precio_unitario;
	private ProductoDto producto;
}
