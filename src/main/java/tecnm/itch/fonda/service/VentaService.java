package tecnm.itch.fonda.service;

import java.util.List;

import tecnm.itch.fonda.dto.EmpleadoDto;
import tecnm.itch.fonda.dto.VentaDto;
import tecnm.itch.fonda.dto.VentaResponseDto;

public interface VentaService {

	VentaDto createVenta(VentaDto ventaDto);

	VentaDto getVentaById(Integer ventaId);

	List<VentaResponseDto> getAllVentas();

	List<VentaResponseDto> findVentasByFecha(String fecha);

	VentaDto updateVenta(Integer ventaId, VentaDto updateVenta);

	void deleteVenta(Integer ventaId);

	VentaDto updateEstado(Integer id, String nuevoEstado);

	EmpleadoDto getEmpleadoById(Integer idEmpleado);

}
