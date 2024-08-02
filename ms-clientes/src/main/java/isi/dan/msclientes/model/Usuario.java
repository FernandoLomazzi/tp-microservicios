package isi.dan.msclientes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "MS_CLI_USUARIO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "El nombre es obligatorio")
	String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	String apellido;
	
	String dni;
	
	@Email(message = "El email debe ser valido")
	@NotBlank(message = "El email es obligatorio")
	String correoElectronico;
	
	@ManyToMany
	List<Cliente> clientes;
}
