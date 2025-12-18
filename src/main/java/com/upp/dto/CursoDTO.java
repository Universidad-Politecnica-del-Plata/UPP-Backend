package com.upp.dto;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CursoDTO {

  private String codigo;

  private Integer maximoDeAlumnos;

  private String codigoMateria;

  private List<String> codigosCuatrimestres;

  public CursoDTO(
      String codigo,
      Integer maximoDeAlumnos,
      String codigoMateria,
      List<String> codigosCuatrimestres) {
    this.codigo = codigo;
    this.maximoDeAlumnos = maximoDeAlumnos;
    this.codigoMateria = codigoMateria;
    this.codigosCuatrimestres = codigosCuatrimestres;
  }
}
