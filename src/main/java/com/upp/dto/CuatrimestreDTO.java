package com.upp.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CuatrimestreDTO {

  private String codigo;

  private LocalDate fechaDeInicioClases;

  private LocalDate fechaDeFinClases;

  private LocalDate fechaInicioPeriodoDeInscripcion;

  private LocalDate fechaFinPeriodoDeInscripcion;

  private LocalDate fechaInicioPeriodoIntegradores;

  private LocalDate fechaFinPeriodoIntegradores;

  private List<String> codigosCursos;

  public CuatrimestreDTO(
      String codigo,
      LocalDate fechaDeInicioClases,
      LocalDate fechaDeFinClases,
      LocalDate fechaInicioPeriodoDeInscripcion,
      LocalDate fechaFinPeriodoDeInscripcion,
      LocalDate fechaInicioPeriodoIntegradores,
      LocalDate fechaFinPeriodoIntegradores) {
    this.codigo = codigo;
    this.fechaDeInicioClases = fechaDeInicioClases;
    this.fechaDeFinClases = fechaDeFinClases;
    this.fechaInicioPeriodoDeInscripcion = fechaInicioPeriodoDeInscripcion;
    this.fechaFinPeriodoDeInscripcion = fechaFinPeriodoDeInscripcion;
    this.fechaInicioPeriodoIntegradores = fechaInicioPeriodoIntegradores;
    this.fechaFinPeriodoIntegradores = fechaFinPeriodoIntegradores;
  }

  public CuatrimestreDTO(
      String codigo,
      LocalDate fechaDeInicioClases,
      LocalDate fechaDeFinClases,
      LocalDate fechaInicioPeriodoDeInscripcion,
      LocalDate fechaFinPeriodoDeInscripcion,
      LocalDate fechaInicioPeriodoIntegradores,
      LocalDate fechaFinPeriodoIntegradores,
      List<String> codigosCursos) {
    this.codigo = codigo;
    this.fechaDeInicioClases = fechaDeInicioClases;
    this.fechaDeFinClases = fechaDeFinClases;
    this.fechaInicioPeriodoDeInscripcion = fechaInicioPeriodoDeInscripcion;
    this.fechaFinPeriodoDeInscripcion = fechaFinPeriodoDeInscripcion;
    this.fechaInicioPeriodoIntegradores = fechaInicioPeriodoIntegradores;
    this.fechaFinPeriodoIntegradores = fechaFinPeriodoIntegradores;
    this.codigosCursos = codigosCursos;
  }
}