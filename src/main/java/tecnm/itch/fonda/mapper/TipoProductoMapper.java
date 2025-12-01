package tecnm.itch.fonda.mapper;

import tecnm.itch.fonda.dto.TipoProductoDto;
import tecnm.itch.fonda.entity.TipoProducto;

public class TipoProductoMapper {

	public static TipoProductoDto mapToTipoProductoDto(TipoProducto tipoProducto) {
		return new TipoProductoDto(tipoProducto.getId_tipo(), tipoProducto.getTipo(), tipoProducto.getDescripcion(),
				tipoProducto.getEstatus());
	}

	public static TipoProducto mapToTipoProducto(TipoProductoDto tipoProductoDto) {
		return new TipoProducto(tipoProductoDto.getId_tipo(), tipoProductoDto.getTipo(),
				tipoProductoDto.getDescripcion(), tipoProductoDto.getEstatus());
	}
}
