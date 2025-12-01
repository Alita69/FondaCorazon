package tecnm.itch.fonda.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tecnm.itch.fonda.dto.DetalleVentaDto;
import tecnm.itch.fonda.dto.VentaDto;
import tecnm.itch.fonda.entity.DetalleVenta;
import tecnm.itch.fonda.entity.Venta;

public class VentaMapper {

	public static VentaDto mapToVentaDto(Venta venta) {
		if (venta == null)
			return null;

		// 1. Mapea los detalles (esto ya lo tenías bien)
		List<DetalleVentaDto> detallesDto = (venta.getDetalles() == null) ? new ArrayList<>()
				: venta.getDetalles().stream().map(DetalleVentaMapper::mapToDetalleVentaDto)
						.collect(Collectors.toList());

		// --- ✅ SOLUCIÓN: USA SETTERS ---
		// Esto es seguro y a prueba de errores de orden.
		VentaDto dto = new VentaDto();
		dto.setId_venta(venta.getIdVenta());
		dto.setFecha_venta(venta.getFechaVenta());
		dto.setId_cliente(venta.getIdCliente());
		dto.setId_empleado(venta.getIdEmpleado());
		dto.setId_reserva(venta.getIdReserva()); // <-- El campo que añadimos
		dto.setTotal(venta.getTotal());
		dto.setDetalles(detallesDto); // <-- Asigna la lista de detalles

		// 'cliente' y 'empleado' se dejan null para que el servicio los llene después
		return dto;
	}

	public static Venta mapToVenta(VentaDto dto) {
		if (dto == null)
			return null;

		List<DetalleVenta> detalles = (dto.getDetalles() == null) ? new ArrayList<>()
				: dto.getDetalles().stream().map(DetalleVentaMapper::mapToDetalleVenta).collect(Collectors.toList());

		// --- ✅ USA SETTERS TAMBIÉN AQUÍ ---
		Venta v = new Venta();
		v.setIdVenta(dto.getId_venta());
		v.setFechaVenta(dto.getFecha_venta());
		v.setIdCliente(dto.getId_cliente());
		v.setIdEmpleado(dto.getId_empleado());
		v.setTotal(dto.getTotal());
		v.setDetalles(detalles);
		v.setIdReserva(dto.getId_reserva()); // <-- El campo que añadimos

		// Esto está perfecto
		for (DetalleVenta d : detalles) {
			d.setVenta(v);
		}

		return v;
	}
}