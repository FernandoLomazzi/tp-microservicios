package isi.dan.msclientes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.model.Usuario;
import isi.dan.msclientes.servicios.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	@LogExecutionTime
	public List<Usuario> getAll() {
		return usuarioService.findAll();
	}

	@GetMapping("/{id}")
	@LogExecutionTime
	public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
		Optional<Usuario> usuario = usuarioService.findById(id);
		return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public Usuario create(@RequestBody Usuario usuario) {
		return usuarioService.save(usuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario usuario) {
		if (!usuarioService.findById(id).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		usuario.setId(id);
		return ResponseEntity.ok(usuarioService.update(usuario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		if (!usuarioService.findById(id).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
