package tecnm.itch.fonda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDto {
	private Integer id_empleado;
	private String nombre;
	private String apellido;
	private String email;
	private String telefono;
	private String puesto;

}