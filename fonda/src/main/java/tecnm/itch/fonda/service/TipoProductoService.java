package tecnm.itch.fonda.service;

import java.util.List;

import tecnm.itch.fonda.dto.TipoProductoDto;

public interface TipoProductoService {
	TipoProductoDto createTipoProducto(TipoProductoDto tipoProductoDto);

	TipoProductoDto getTipoProductoById(Integer idTipo);

	List<TipoProductoDto> getAllTipoProducto();

	TipoProductoDto updateTipoProducto(Integer idTipo, TipoProductoDto updateTipoProducto);

	void deleteTipoProducto(Integer idTipo);

	List<TipoProductoDto> getActiveTipoProductos();
}
