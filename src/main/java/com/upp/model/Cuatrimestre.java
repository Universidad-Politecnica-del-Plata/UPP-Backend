package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cuatrimestre {
  @Id
  @NotNull
  private String codigo;

  @NotNull
  private LocalDate fechaDeInicioClases;

  @NotNull
  private LocalDate fechaDeFinClases;

  @NotNull
  private LocalDate fechaInicioPeriodoDeInscripcion;

  @NotNull
  private LocalDate fechaFinPeriodoDeInscripcion;

  @NotNull
  private LocalDate fechaInicioPeriodoIntegradores;

  @NotNull
  private LocalDate fechaFinPeriodoIntegradores;

  @ManyToMany(mappedBy = "cuatrimestres")
  private List<Curso> cursos = new ArrayList<>();

  public Cuatrimestre(
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

  public Cuatrimestre() {}
}