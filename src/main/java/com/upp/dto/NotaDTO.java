package com.upp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {

  private Long id;

  private Integer valor;

  private Long alumnoId;

  private String nombreAlumno;

  private String apellidoAlumno;

  private Long matriculaAlumno;

  private Long numeroCorrelativoActa;
}
