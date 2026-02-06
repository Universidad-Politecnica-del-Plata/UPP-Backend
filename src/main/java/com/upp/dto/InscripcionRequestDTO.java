package com.upp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Se utiliza para inscribir un alumno a un curso */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionRequestDTO {

  @NotBlank(message = "El código del curso es obligatorio")
  private String codigoCurso;
}
