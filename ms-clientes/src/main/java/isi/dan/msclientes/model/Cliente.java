package isi.dan.msclientes.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "MS_CLI_CLIENTE")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "El nombre es obligatorio")
	String nombre;

	@Column(name = "CORREO_ELECTRONICO")
	@Email(message = "El email debe ser valido")
	@NotBlank(message = "El email es obligatorio")
	String correoElectronico;

	String cuit;

	@Column(name = "MAXIMO_DESCUBIERTO")
	@Min(value = 10000, message = "El descubierto maximo debe ser al menos 10000")
	BigDecimal maximoDescubierto;

	@Min(value = 0, message = "No se pueden tener cantidades negativas de obras disponibles a realizar")
	Integer cantObrasDisponibles;
	
	public void tomarObra() throws Exception {
		if(cantObrasDisponibles==0)
			throw new Exception("El cliente "+nombre+" ha superado su limite de obras habilitadas en simultaneo");
		cantObrasDisponibles--;
	}

	public void liberarObra() throws Exception {
		cantObrasDisponibles++;
	}
}
