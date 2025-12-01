package tecnm.itch.fonda.service;

import java.util.List;

import tecnm.itch.fonda.dto.ProductoDto;

public interface ProductoService {
	ProductoDto createProducto(ProductoDto productoDto);

	ProductoDto getProductoById(Integer id_producto);

	List<ProductoDto> getAllProducto();

	// Opcional: Buscar productos por tipo
	List<ProductoDto> getProductosByTipo(Integer id_tipo);

	// Opcional: Buscar productos por nombre (búsqueda)
	List<ProductoDto> getProductosByNombre(String nombre);

	ProductoDto updateProducto(Integer id_producto, ProductoDto updateProducto);

	void deleteProducto(Integer id_producto);
}
