package tecnm.itch.fonda.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import feign.FeignException;
import lombok.AllArgsConstructor;
import tecnm.itch.fonda.ResourceNotFoundException;
import tecnm.itch.fonda.client.AtenderClient;
import tecnm.itch.fonda.client.ClienteClient;
import tecnm.itch.fonda.client.EmpleadoClient;
import tecnm.itch.fonda.client.ReservaClient;
import tecnm.itch.fonda.dto.AtenderDto;
import tecnm.itch.fonda.dto.VentaDto;
import tecnm.itch.fonda.dto.VentaResponseDto;
import tecnm.itch.fonda.entity.DetalleVenta;
import tecnm.itch.fonda.entity.Venta;
import tecnm.itch.fonda.mapper.VentaMapper;
import tecnm.itch.fonda.repository.ProductoRepository;
import tecnm.itch.fonda.repository.VentaRepository;
import tecnm.itch.fonda.service.VentaService;
// CAMBIO: Import local
import tecnm.itch.fonda.dto.EmpleadoDto;

@AllArgsConstructor
@Service
public class VentaServiceImplement implements VentaService {

	private final VentaRepository ventaRepository;
	private final ProductoRepository productoRepository;
	private final ClienteClient cliente;
	private final EmpleadoClient empleadoClient;
	private final AtenderClient atenderClient;
	private final ReservaClient reservaClient;

	@Override
	@Transactional
	public VentaDto createVenta(VentaDto ventaDto) {
		if (ventaDto.getId_cliente() == null) {
			throw new IllegalArgumentException("El idCliente es obligatorio");
		}

		try {
			cliente.getClienteById(ventaDto.getId_cliente()); 
		} catch (FeignException.NotFound nf) {
			throw new ResourceNotFoundException("Cliente no existe: " + ventaDto.getId_cliente());
		} catch (FeignException fe) {
			throw new IllegalStateException("Error al consultar el servicio de Cliente: " + fe.getMessage());
		}

		Venta venta = VentaMapper.mapToVenta(ventaDto);

		if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
			throw new IllegalArgumentException("La venta debe tener al menos un detalle de producto.");
		}

		double total = 0.0;
		for (DetalleVenta det : venta.getDetalles()) {
			det.setVenta(venta);
			var producto = productoRepository.findById(det.getProducto().getId_producto()).orElseThrow(
					() -> new ResourceNotFoundException("Producto no existe: " + det.getProducto().getId_producto()));

			if (det.getCantidad() == null || det.getCantidad() <= 0)
				det.setCantidad(1);
			if (det.getPrecioUnitario() == null || det.getPrecioUnitario() <= 0)
				det.setPrecioUnitario(producto.getPrecio());

			total += det.getCantidad() * det.getPrecioUnitario();
		}

		venta.setTotal(total);
		Venta guardada = ventaRepository.save(venta);

		if (ventaDto.getId_empleado() == null) {
			throw new IllegalArgumentException(
					"El ID del empleado (mesero) es obligatorio para registrar la atención.");
		}

		AtenderDto atenderInfo = new AtenderDto();
		atenderInfo.setIdEmpleado(ventaDto.getId_empleado());
		atenderInfo.setIdVenta(guardada.getIdVenta());

		try {
			atenderClient.crearAtender(atenderInfo);
		} catch (FeignException e) {
			throw new IllegalStateException(
					"La venta se guardó, pero falló al asignar el mesero. Error: " + e.getMessage());
		}

		if (guardada.getIdReserva() != null) {
			try {
				System.out.println("Confirmando reserva con ID: " + guardada.getIdReserva());
				reservaClient.confirmarReserva(guardada.getIdReserva());
				System.out.println("Reserva confirmada.");
			} catch (FeignException e) {
				throw new IllegalStateException(
						"La venta se guardó, pero falló al CONFIRMAR la reserva. La venta se cancelará. Error: "
								+ e.getMessage());
			}
		}

		return VentaMapper.mapToVentaDto(guardada);
	}

	@Override
	@Transactional
	public VentaDto updateVenta(Integer ventaId, VentaDto updateVenta) {
		Venta venta = ventaRepository.findById(ventaId)
				.orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));

		if (updateVenta.getId_cliente() != null && !updateVenta.getId_cliente().equals(venta.getIdCliente())) {
			try {
				cliente.getClienteById(updateVenta.getId_cliente());
			} catch (FeignException.NotFound nf) {
				throw new ResourceNotFoundException("Cliente no existe: " + updateVenta.getId_cliente());
			} catch (FeignException fe) {
				throw new IllegalStateException("Error al consultar el servicio de Cliente: " + fe.getMessage());
			}
			venta.setIdCliente(updateVenta.getId_cliente());
		}

		ventaRepository.save(venta);
		return VentaMapper.mapToVentaDto(venta);
	}

	@Override
	@Transactional(readOnly = true)
	public VentaDto getVentaById(Integer ventaId) {
		Venta venta = ventaRepository.findById(ventaId)
				.orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));

		VentaDto ventaDto = VentaMapper.mapToVentaDto(venta);

		try {
			var clienteDto = cliente.getClienteById(venta.getIdCliente()); 
			ventaDto.setCliente(clienteDto);
		} catch (FeignException e) {
			System.err.println("No se pudo obtener el cliente " + venta.getIdCliente() + ": " + e.getMessage());
		}

		try {
			var empleadoDto = empleadoClient.getEmpleadoById(venta.getIdEmpleado());
			ventaDto.setEmpleado(empleadoDto);
		} catch (FeignException e) {
			System.err.println(
					"No se pudo obtener el mesero para la venta " + venta.getIdVenta() + ": " + e.getMessage());
			ventaDto.setEmpleado(null);
		}

		return ventaDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<VentaResponseDto> getAllVentas() {
		List<Venta> ventas = ventaRepository.findAll();
		return ventas.stream().map(this::mapToVentaResponseDto).collect(Collectors.toList());
	}

	private VentaResponseDto mapToVentaResponseDto(Venta venta) {
		VentaResponseDto dto = new VentaResponseDto();
		dto.setId_venta(venta.getIdVenta());
		dto.setFecha_venta(venta.getFechaVenta());
		dto.setTotal(venta.getTotal());
		dto.setId_reserva(venta.getIdReserva());
		dto.setEstado(venta.getEstado());

		try {
			var clienteData = cliente.getClienteById(venta.getIdCliente());
			if (clienteData != null) {
				VentaResponseDto.ClienteInfo clienteInfo = new VentaResponseDto.ClienteInfo();
				clienteInfo.setNombre_cliente(clienteData.getNombreCliente());
				dto.setCliente(clienteInfo);
			}
		} catch (FeignException e) {
			System.err.println("No se pudo obtener el cliente " + venta.getIdCliente() + ": " + e.getMessage());
		}

		try {
			var empleadoDto = empleadoClient.getEmpleadoById(venta.getIdEmpleado());
			if (empleadoDto != null) {
				VentaResponseDto.EmpleadoInfo empleadoInfo = new VentaResponseDto.EmpleadoInfo();
				empleadoInfo.setNombre(empleadoDto.getNombre());
				dto.setEmpleado(empleadoInfo);
			}
		} catch (FeignException e) {
			System.err.println(
					"No se pudo obtener el mesero para la venta " + venta.getIdVenta() + ": " + e.getMessage());
		}

		return dto;
	}

	@Override
	@Transactional
	public void deleteVenta(Integer ventaId) {
		if (!ventaRepository.existsById(ventaId)) {
			throw new ResourceNotFoundException("Venta no encontrada con id: " + ventaId);
		}
		ventaRepository.deleteById(ventaId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VentaResponseDto> findVentasByFecha(String fecha) {
		List<Venta> ventas = ventaRepository.findVentasByFecha(fecha);
		return ventas.stream().map(this::mapToVentaResponseDto).collect(Collectors.toList());
	}

	@Override
	public VentaDto updateEstado(Integer id, String nuevoEstado) {
		Venta venta = ventaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
		venta.setEstado(nuevoEstado);
		Venta ventaGuardada = ventaRepository.save(venta);
		return VentaMapper.mapToVentaDto(ventaGuardada);
	}

	@Override
	public EmpleadoDto getEmpleadoById(Integer idEmpleado) {
		return empleadoClient.getEmpleadoById(idEmpleado);
	}
}