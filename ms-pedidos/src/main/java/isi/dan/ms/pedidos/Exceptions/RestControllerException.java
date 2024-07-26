package isi.dan.ms.pedidos.Exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerException {

	private static final Logger logger = LoggerFactory.getLogger(RestControllerException.class);

	@ExceptionHandler(PedidoNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleClienteNotFoundException(PedidoNotFoundException ex) {
		logger.error("ERROR Buscando Pedido ", ex);
		String detalle = ex.getCause() == null ? "Pedido no encontrado" : ex.getCause().getMessage();

		return new ResponseEntity<ErrorInfo>(
				new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.NOT_FOUND.value()),
				HttpStatus.NOT_FOUND);
	}

	
}
