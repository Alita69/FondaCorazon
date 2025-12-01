package tecnm.itch.fonda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import tecnm.itch.fonda.entity.TipoProducto;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE TipoProducto t SET t.estatus = 0 WHERE t.id_tipo = :id")
	void softDeleteById(@Param("id") Integer id);

	List<TipoProducto> findByEstatus(int estatus);
}