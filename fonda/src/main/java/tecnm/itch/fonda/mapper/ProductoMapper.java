package tecnm.itch.fonda.mapper;

import tecnm.itch.fonda.dto.ProductoDto;
import tecnm.itch.fonda.entity.Producto;
import tecnm.itch.fonda.entity.TipoProducto;

public class ProductoMapper {
	public static ProductoDto mapToProductoDto(Producto producto) {
		return new ProductoDto(producto.getId_producto(), producto.getNombre(), producto.getDescripcion(),
				producto.getPrecio(),
				producto.getTipo() != null ? TipoProductoMapper.mapToTipoProductoDto(producto.getTipo()) : null,
				producto.getEstatus(), producto.getFotoUrl());
	}

	public static Producto mapToProducto(ProductoDto productoDto) {
		Producto producto = new Producto();
		producto.setId_producto(productoDto.getId_producto());
		producto.setNombre(productoDto.getNombre());
		producto.setDescripcion(productoDto.getDescripcion());
		producto.setPrecio(productoDto.getPrecio());

		// Mapear el tipo de producto si existe en el DTO
		if (productoDto.getTipo() != null) {
			TipoProducto tipo = TipoProductoMapper.mapToTipoProducto(productoDto.getTipo());
			producto.setTipo(tipo);
		}

		producto.setEstatus(productoDto.getEstatus());
		producto.setFotoUrl(productoDto.getFotoUrl());

		return producto;
	}
}
