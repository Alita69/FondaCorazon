package tecnm.itch.fonda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tecnm.itch.fonda.dto.TipoProductoDto;
import tecnm.itch.fonda.service.TipoProductoService;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/tipoproducto")
public class TipoProductoController {

	private TipoProductoService tipoProductoService;

	@PostMapping
	public ResponseEntity<TipoProductoDto> crearTipoProducto(@RequestBody TipoProductoDto tipoProductoDto) {
		TipoProductoDto guardarTipoProducto = tipoProductoService.createTipoProducto(tipoProductoDto);
		return new ResponseEntity<>(guardarTipoProducto, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<TipoProductoDto> getTipoProductoById(@PathVariable("id") Integer idTipo) {
		TipoProductoDto tipoProductoDto = tipoProductoService.getTipoProductoById(idTipo);
		return ResponseEntity.ok(tipoProductoDto);
	}

	@GetMapping
	public ResponseEntity<List<TipoProductoDto>> getAllTipoProductos() {
		List<TipoProductoDto> tiposProducto = tipoProductoService.getAllTipoProducto();
		return ResponseEntity.ok(tiposProducto);
	}

	@GetMapping("/activos")
	public ResponseEntity<List<TipoProductoDto>> getActiveTipoProductos() {
		// Llama al servicio SIN parámetros
		List<TipoProductoDto> tiposActivos = tipoProductoService.getActiveTipoProductos();
		return ResponseEntity.ok(tiposActivos);
	}

	@PutMapping("{id}")
	public ResponseEntity<TipoProductoDto> updateTipoProducto(@PathVariable("id") Integer idTipo,
			@RequestBody TipoProductoDto updateTipoProducto) {
		TipoProductoDto tipoProductoDto = tipoProductoService.updateTipoProducto(idTipo, updateTipoProducto);
		return ResponseEntity.ok(tipoProductoDto);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteTipoProducto(@PathVariable("id") Integer idTipo) {
		tipoProductoService.deleteTipoProducto(idTipo);
		return ResponseEntity.ok("Tipo de producto eliminado correctamente.");
	}
}
