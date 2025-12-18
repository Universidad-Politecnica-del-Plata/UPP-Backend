package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Nota {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Integer valor;

  @ManyToOne
  @JoinColumn(name = "alumno_id")
  @NotNull
  private Alumno alumno;

  @ManyToOne
  @JoinColumn(name = "acta_numero_correlativo")
  @NotNull
  private Acta acta;

  public Nota(Integer valor, Alumno alumno, Acta acta) {
    this.valor = valor;
    this.alumno = alumno;
    this.acta = acta;
  }

  public Nota() {}
}
