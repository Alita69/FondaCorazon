package tecnm.itch.fonda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tecnm.itch.fonda.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
	@Query("SELECT v FROM Venta v WHERE DATE(v.fechaVenta) = DATE(:fecha) ORDER BY v.idVenta DESC")
	List<Venta> findVentasByFecha(@Param("fecha") String fecha);

	List<Venta> findAllByOrderByIdVentaDesc();
}
