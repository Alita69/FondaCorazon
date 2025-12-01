package tecnm.itch.fonda.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tecnm.itch.fonda.dto.ProductoDto;
import tecnm.itch.fonda.entity.Producto;
import tecnm.itch.fonda.entity.TipoProducto;
import tecnm.itch.fonda.mapper.ProductoMapper;
import tecnm.itch.fonda.repository.ProductoRepository;
import tecnm.itch.fonda.repository.TipoProductoRepository;
import tecnm.itch.fonda.service.ProductoService;

@Service
public class ProductoServiceImplement implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private TipoProductoRepository tipoProductoRepository;

	@Override
	public ProductoDto createProducto(ProductoDto productoDto) {
		Producto producto = ProductoMapper.mapToProducto(productoDto);

		// Verificar y cargar el tipo de producto si se proporciona un ID
		if (productoDto.getTipo() != null && productoDto.getTipo().getId_tipo() > 0) {
			TipoProducto tipo = tipoProductoRepository.findById(productoDto.getTipo().getId_tipo())
					.orElseThrow(() -> new RuntimeException(
							"TipoProducto no encontrado con id: " + productoDto.getTipo().getId_tipo()));
			producto.setTipo(tipo);
		}

		Producto savedProducto = productoRepository.save(producto);
		return ProductoMapper.mapToProductoDto(savedProducto);
	}

	@Override
	public ProductoDto getProductoById(Integer id_producto) {
		Producto producto = productoRepository.findById(id_producto)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id_producto));
		return ProductoMapper.mapToProductoDto(producto);
	}

	@Override
	public List<ProductoDto> getAllProducto() {
		List<Producto> productos = productoRepository.findByEstatus(1);
		return productos.stream().map(ProductoMapper::mapToProductoDto).collect(Collectors.toList());
	}

	@Override
	public List<ProductoDto> getProductosByTipo(Integer id_tipo) {
		List<Producto> productos = productoRepository.findProductosByTipoIdAndEstatus(id_tipo, 1);
		return productos.stream().map(ProductoMapper::mapToProductoDto).collect(Collectors.toList());
	}

	@Override
	public List<ProductoDto> getProductosByNombre(String nombre) {
		List<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseAndEstatus(nombre, 1);
		return productos.stream().map(ProductoMapper::mapToProductoDto).collect(Collectors.toList());
	}

	@Override
	public ProductoDto updateProducto(Integer id_producto, ProductoDto updateProducto) {
		Producto existingProducto = productoRepository.findById(id_producto)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id_producto));

		existingProducto.setNombre(updateProducto.getNombre());
		existingProducto.setDescripcion(updateProducto.getDescripcion());
		existingProducto.setPrecio(updateProducto.getPrecio());
		existingProducto.setFotoUrl(updateProducto.getFotoUrl());

		// Actualizar el tipo si se proporciona
		if (updateProducto.getTipo() != null && updateProducto.getTipo().getId_tipo() > 0) {
			TipoProducto tipo = tipoProductoRepository.findById(updateProducto.getTipo().getId_tipo())
					.orElseThrow(() -> new RuntimeException(
							"TipoProducto no encontrado con id: " + updateProducto.getTipo().getId_tipo()));
			existingProducto.setTipo(tipo);
		}

		Producto updatedProducto = productoRepository.save(existingProducto);
		return ProductoMapper.mapToProductoDto(updatedProducto);
	}

	@Override
	public void deleteProducto(Integer id_producto) {
		Producto producto = productoRepository.findById(id_producto)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id_producto));
		producto.setEstatus(0); // marcar como inactivo
		productoRepository.save(producto);
	}
}
