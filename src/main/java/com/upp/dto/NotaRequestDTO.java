package com.upp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaRequestDTO {

  @NotNull(message = "El valor de la nota es obligatorio")
  @Min(value = 1, message = "La nota mínima es 1")
  @Max(value = 10, message = "La nota máxima es 10")
  private Integer valor;

  @NotNull(message = "El ID del alumno es obligatorio")
  private Long alumnoId;
}
