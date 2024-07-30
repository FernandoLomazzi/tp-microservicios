package isi.dan.ms_productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateDTO {
	@NotNull(message = "id must not be null")
    private Long idProducto;
    @Min(value = 0, message = "Cantidad debe ser mayor o igual a 0")
    private Integer cantidad;
    @Min(value = 0, message = "Precio debe ser mayor o igual a 0")
    private Double precio;
}
