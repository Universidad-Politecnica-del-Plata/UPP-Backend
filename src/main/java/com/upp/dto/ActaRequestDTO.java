package com.upp.dto;

import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActaRequestDTO {

  @NotNull(message = "El tipo de acta es obligatorio")
  private TipoDeActa tipoDeActa;

  @NotNull(message = "El estado es obligatorio")
  private EstadoActa estado;

  @NotBlank(message = "El código del curso es obligatorio")
  private String codigoCurso;
}
