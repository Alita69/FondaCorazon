package tecnm.itch.fonda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import tecnm.itch.fonda.dto.ProductoDto;
import tecnm.itch.fonda.dto.TipoProductoDto;
import tecnm.itch.fonda.service.ProductoService;
import tecnm.itch.fonda.storage.FileStorageService;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping
	public ResponseEntity<ProductoDto> crearProducto(@RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion, @RequestParam("precio") Double precio,
			@RequestParam("idTipo") Integer idTipo, // Recibimos el ID del tipo
			@RequestParam(value = "foto", required = false) MultipartFile foto) {

		String fotoUrl = null;
		if (foto != null && !foto.isEmpty()) {
			fotoUrl = fileStorageService.store(foto);
		}

		TipoProductoDto tipoDto = new TipoProductoDto();
		tipoDto.setId_tipo(idTipo);

		ProductoDto productoDto = new ProductoDto(0, nombre, descripcion, precio, tipoDto, 1, fotoUrl);

		ProductoDto guardarProducto = productoService.createProducto(productoDto);
		return new ResponseEntity<>(guardarProducto, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<ProductoDto> getProductoById(@PathVariable("id") Integer id_producto) {
		ProductoDto productoDto = productoService.getProductoById(id_producto);
		return ResponseEntity.ok(productoDto);
	}

	@GetMapping
	public ResponseEntity<List<ProductoDto>> getAllProductos() {
		List<ProductoDto> productos = productoService.getAllProducto();
		return ResponseEntity.ok(productos);
	}

	@GetMapping("/tipo/{id_tipo}")
	public ResponseEntity<List<ProductoDto>> getProductosByTipo(@PathVariable("id_tipo") Integer id_tipo) {
		List<ProductoDto> productos = productoService.getProductosByTipo(id_tipo);
		return ResponseEntity.ok(productos);
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<ProductoDto>> getProductosByNombre(@RequestParam String nombre) {
		List<ProductoDto> productos = productoService.getProductosByNombre(nombre);
		return ResponseEntity.ok(productos);
	}

	@PostMapping("{id}")
	public ResponseEntity<ProductoDto> updateProducto(@PathVariable("id") Integer id_producto,
			@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
			@RequestParam("precio") Double precio, @RequestParam("idTipo") Integer idTipo,
			@RequestParam(value = "foto", required = false) MultipartFile foto) {

		ProductoDto productoExistente = productoService.getProductoById(id_producto);
		String fotoUrlActual = productoExistente.getFotoUrl();

		if (foto != null && !foto.isEmpty()) {
			if (fotoUrlActual != null) {
				fileStorageService.delete(fotoUrlActual);
			}
			fotoUrlActual = fileStorageService.store(foto);
		}

		TipoProductoDto tipoDto = new TipoProductoDto();
		tipoDto.setId_tipo(idTipo);

		ProductoDto updateProducto = new ProductoDto(id_producto, nombre, descripcion, precio, tipoDto,
				productoExistente.getEstatus(), fotoUrlActual);

		ProductoDto productoDto = productoService.updateProducto(id_producto, updateProducto);
		return ResponseEntity.ok(productoDto);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteProducto(@PathVariable("id") Integer id_producto) {
		try {
			productoService.deleteProducto(id_producto);
			return ResponseEntity.ok("✅ Producto desactivado correctamente (estatus = 0).");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("⚠️ No se encontró el producto con id: " + id_producto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("❌ Error al desactivar producto: " + e.getMessage());
		}
	}

}
