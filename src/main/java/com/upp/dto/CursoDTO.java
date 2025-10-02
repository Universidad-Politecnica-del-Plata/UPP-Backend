package com.upp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CursoDTO {

  private String codigo;

  private Integer maximoDeAlumnos;

  private String codigoMateria;

  public CursoDTO(String codigo, Integer maximoDeAlumnos, String codigoMateria) {
    this.codigo = codigo;
    this.maximoDeAlumnos = maximoDeAlumnos;
    this.codigoMateria = codigoMateria;
  }
}