package tecnm.itch.fonda.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import tecnm.itch.fonda.dto.DetalleVentaDto;
import tecnm.itch.fonda.entity.DetalleVenta;
import tecnm.itch.fonda.mapper.DetalleVentaMapper;
import tecnm.itch.fonda.repository.DetalleVentaRepository;
import tecnm.itch.fonda.service.DetalleVentaService;

@AllArgsConstructor
@Service
public class DetalleVentaServiceImplement implements DetalleVentaService {

	private final DetalleVentaRepository detalleVentaRepository;

	@Transactional(readOnly = true)
	@Override
	public DetalleVentaDto getDetalleById(Integer detalleId) {
		DetalleVenta dv = detalleVentaRepository.findById(detalleId)
				.orElseThrow(() -> new IllegalArgumentException("Detalle de venta no encontrado"));
		return DetalleVentaMapper.mapToDetalleVentaDto(dv);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DetalleVentaDto> getAllDetallesVenta() {
		return detalleVentaRepository.findAll().stream().map(DetalleVentaMapper::mapToDetalleVentaDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<DetalleVentaDto> getDetallesByVenta(Integer idVenta) {
		return detalleVentaRepository.findByVenta_IdVenta(idVenta).stream()
				.map(DetalleVentaMapper::mapToDetalleVentaDto).collect(Collectors.toList());
	}
}
