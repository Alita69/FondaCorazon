package tecnm.itch.fonda.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venta")
public class Venta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_venta")
	private Integer idVenta;

	@Column(name = "fechaventa", nullable = false)
	private LocalDateTime fechaVenta;

	@Column(name = "id_cliente", nullable = false)
	private Integer idCliente;

	@Column(name = "id_empleado", nullable = true)
	private Integer idEmpleado;

	@Column(name = "total", nullable = false)
	private Double total;

	// 🔥 AGREGAR ESTE CAMPO 🔥
	// Definimos "pendiente" por defecto para que las nuevas ventas nazcan así
	@Column(name = "estado")
	private String estado = "pendiente";

	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleVenta> detalles = new ArrayList<>();

	@Column(name = "id_reserva", nullable = true)
	private Integer idReserva;

	@PrePersist
	protected void onCreate() {
		this.fechaVenta = LocalDateTime.now().withSecond(0).withNano(0);
		// Opcional: asegurar que el estado no sea null al crear
		if (this.estado == null) {
			this.estado = "pendiente";
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.fechaVenta = LocalDateTime.now().withSecond(0).withNano(0);
	}
}