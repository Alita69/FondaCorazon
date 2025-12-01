package tecnm.itch.fonda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
	private int id_producto;
	private String nombre;
	private String descripcion;
	private double precio;
	private TipoProductoDto tipo;
	private int estatus;
	private String fotoUrl;
}
