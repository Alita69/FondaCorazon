package tecnm.itch.fonda.service;

import java.util.List;

import tecnm.itch.fonda.dto.DetalleVentaDto;

public interface DetalleVentaService {
	// Consulta un detalle por id
	DetalleVentaDto getDetalleById(Integer detalleId);

	// Lista todos los detalles
	List<DetalleVentaDto> getAllDetallesVenta();

	// Lista los detalles de una venta específica
	List<DetalleVentaDto> getDetallesByVenta(Integer id_venta);
}
