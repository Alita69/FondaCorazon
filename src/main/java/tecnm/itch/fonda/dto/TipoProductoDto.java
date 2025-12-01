package tecnm.itch.fonda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoProductoDto {
	private int id_tipo;
	private String tipo;
	private String descripcion;
	private int estatus;
}
