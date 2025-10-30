package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Inscripcion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long codigoDeInscripcion;

  @NotNull
  private LocalDate fecha;

  @NotNull
  private LocalTime horario;

  @ManyToOne
  @JoinColumn(name = "curso_codigo")
  @NotNull
  private Curso curso;

  @ManyToOne
  @JoinColumn(name = "cuatrimestre_codigo")
  @NotNull
  private Cuatrimestre cuatrimestre;

  @ManyToOne
  @JoinColumn(name = "alumno_id")
  @NotNull
  private Alumno alumno;

  public Inscripcion(Curso curso, Cuatrimestre cuatrimestre, Alumno alumno) {
    this.curso = curso;
    this.cuatrimestre = cuatrimestre;
    this.alumno = alumno;
    this.fecha = LocalDate.now();
    this.horario = LocalTime.now();
  }

  public Inscripcion() {}
}