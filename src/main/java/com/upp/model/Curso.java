package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Curso {

  @Id @NotBlank private String codigo;

  @NotNull
  @Min(1)
  private Integer maximoDeAlumnos;

  @ManyToOne
  @JoinColumn(name = "materia_codigo_de_materia")
  @NotNull
  private Materia materia;

  public Curso(String codigo, Integer maximoDeAlumnos, Materia materia) {
    this.codigo = codigo;
    this.maximoDeAlumnos = maximoDeAlumnos;
    this.materia = materia;
  }

  public Curso() {}
}