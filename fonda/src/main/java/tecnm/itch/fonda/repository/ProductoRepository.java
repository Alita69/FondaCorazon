package tecnm.itch.fonda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tecnm.itch.fonda.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	@Query("SELECT p FROM Producto p WHERE p.tipo.id_tipo = :idTipo AND p.estatus = :estatus")
	List<Producto> findProductosByTipoIdAndEstatus(@Param("idTipo") Integer id_tipo, @Param("estatus") int estatus);

	List<Producto> findByNombreContainingIgnoreCaseAndEstatus(String nombre, int estatus);

	List<Producto> findByEstatus(int estatus);
}
