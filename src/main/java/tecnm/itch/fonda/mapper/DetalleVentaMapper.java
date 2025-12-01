package tecnm.itch.fonda.mapper;

import tecnm.itch.fonda.dto.DetalleVentaDto;
import tecnm.itch.fonda.dto.ProductoDto;
import tecnm.itch.fonda.entity.DetalleVenta;
import tecnm.itch.fonda.entity.Producto;

public class DetalleVentaMapper {
	public static DetalleVentaDto mapToDetalleVentaDto(DetalleVenta dv) {
		if (dv == null)
			return null;

		ProductoDto productoDto = ProductoMapper.mapToProductoDto(dv.getProducto());

		return new DetalleVentaDto(dv.getId_detalle(), productoDto != null ? productoDto.getId_producto() : null,
				dv.getCantidad(), dv.getPrecioUnitario(), productoDto);
	}

	public static DetalleVenta mapToDetalleVenta(DetalleVentaDto dto) {
		if (dto == null)
			return null;

		// Producto "placeholder" con solo el ID (no cargamos toda la entidad)
		Producto p = null;
		if (dto.getId_producto() != null) {
			p = new Producto();
			p.setId_producto(dto.getId_producto());
		}

		// venta se setea después (en VentaMapper o en el Service)
		return new DetalleVenta(dto.getId_detalle(), null, p, dto.getCantidad(), dto.getPrecio_unitario());
	}

}
