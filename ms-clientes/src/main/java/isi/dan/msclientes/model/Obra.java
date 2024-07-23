package isi.dan.msclientes.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "MS_CLI_OBRA")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Obra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@NotNull(message = "La direccion es obligatoria")
	String direccion;

	@Column(name = "ES_REMODELACION")
	Boolean esRemodelacion;

	float lat;

	float lng;
	
	@NotNull(message = "El cliente es obligatorio")
	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE")
	Cliente cliente;

	@NotNull(message = "El presupuesto es obligatorio")
	@Min(value = 100, message = "El presupuesto debe ser al menos de 100")
	BigDecimal presupuesto;
	
	@Enumerated(EnumType.STRING)
	EstadoObra estado = EstadoObra.PENDIENTE;
}
