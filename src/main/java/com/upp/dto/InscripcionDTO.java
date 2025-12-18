package com.upp.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDTO {

  private Long codigoDeInscripcion;

  private LocalDate fecha;

  private LocalTime horario;

  @NotBlank(message = "El código del curso es obligatorio")
  private String codigoCurso;

  @NotBlank(message = "El código del cuatrimestre es obligatorio")
  private String codigoCuatrimestre;

  private Long alumnoId;
}
