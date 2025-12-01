package tecnm.itch.fonda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tecnm.itch.fonda.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
	// Para listar los detalles de una venta específica
	List<DetalleVenta> findByVenta_IdVenta(Integer idVenta);

}
