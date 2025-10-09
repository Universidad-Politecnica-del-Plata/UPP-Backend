package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

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

  @ManyToMany
  @JoinTable(
      name = "curso_cuatrimestre",
      joinColumns = @JoinColumn(name = "curso_codigo"),
      inverseJoinColumns = @JoinColumn(name = "cuatrimestre_codigo"))
  private List<Cuatrimestre> cuatrimestres = new ArrayList<>();

  public Curso(String codigo, Integer maximoDeAlumnos, Materia materia, List<Cuatrimestre> cuatrimestres) {
    this.codigo = codigo;
    this.maximoDeAlumnos = maximoDeAlumnos;
    this.materia = materia;
    this.cuatrimestres = cuatrimestres != null ? cuatrimestres : new ArrayList<>();
  }
  
  public Curso(String codigo, Integer maximoDeAlumnos, Materia materia) {
    this(codigo, maximoDeAlumnos, materia, new ArrayList<>());
  }

  public Curso() {}
}