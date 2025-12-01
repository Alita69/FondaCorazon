package tecnm.itch.fonda.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tecnm.itch.fonda.dto.TipoProductoDto;
import tecnm.itch.fonda.entity.TipoProducto;
import tecnm.itch.fonda.mapper.TipoProductoMapper;
import tecnm.itch.fonda.repository.TipoProductoRepository;
import tecnm.itch.fonda.service.TipoProductoService;

@Service
public class TipoProductoServiceImplement implements TipoProductoService {

	@Autowired
	private TipoProductoRepository tipoProductoRepository;

	@Override
	public TipoProductoDto createTipoProducto(TipoProductoDto tipoProductoDto) {

		tipoProductoDto.setEstatus(1);

		TipoProducto tipoProducto = TipoProductoMapper.mapToTipoProducto(tipoProductoDto);
		TipoProducto savedTipoProducto = tipoProductoRepository.save(tipoProducto);
		return TipoProductoMapper.mapToTipoProductoDto(savedTipoProducto);
	}

	@Override
	public TipoProductoDto getTipoProductoById(Integer idTipo) {
		TipoProducto tipoProducto = tipoProductoRepository.findById(idTipo)
				.orElseThrow(() -> new RuntimeException("TipoProducto no encontrado con id: " + idTipo));
		return TipoProductoMapper.mapToTipoProductoDto(tipoProducto);
	}

	@Override
	public List<TipoProductoDto> getAllTipoProducto() {
		List<TipoProducto> tipos = tipoProductoRepository.findAll();
		return tipos.stream().map(TipoProductoMapper::mapToTipoProductoDto).collect(Collectors.toList());
	}

	@Override
	public TipoProductoDto updateTipoProducto(Integer idTipo, TipoProductoDto updateTipoProducto) {
		TipoProducto existingTipo = tipoProductoRepository.findById(idTipo)
				.orElseThrow(() -> new RuntimeException("TipoProducto no encontrado con id: " + idTipo));

		existingTipo.setTipo(updateTipoProducto.getTipo());
		existingTipo.setDescripcion(updateTipoProducto.getDescripcion());

		existingTipo.setEstatus(updateTipoProducto.getEstatus());

		TipoProducto updatedTipo = tipoProductoRepository.save(existingTipo);
		return TipoProductoMapper.mapToTipoProductoDto(updatedTipo);
	}

	@Override
	public void deleteTipoProducto(Integer idTipo) {
		tipoProductoRepository.findById(idTipo)
				.orElseThrow(() -> new RuntimeException("TipoProducto no encontrado con id: " + idTipo));

		tipoProductoRepository.softDeleteById(idTipo);
	}

	@Override
	public List<TipoProductoDto> getActiveTipoProductos() {
		// Llama al repositorio pasándole el 1
		List<TipoProducto> tiposActivos = tipoProductoRepository.findByEstatus(1);
		return tiposActivos.stream().map(TipoProductoMapper::mapToTipoProductoDto).collect(Collectors.toList());
	}
}