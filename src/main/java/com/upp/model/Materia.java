package com.upp.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Materia {
  @Id private String codigoDeMateria;

  private String nombre;
  private String contenidos;
  private Integer creditosQueOtorga;
  private Integer creditosNecesarios;

  @Enumerated(EnumType.STRING)
  private TipoMateria tipo;

  @ManyToMany
  @JoinTable(
      name = "materia_correlativas",
      joinColumns = @JoinColumn(name = "materia_codigo_de_materia"),
      inverseJoinColumns = @JoinColumn(name = "correlativa_codigo_de_materia"))
  private List<Materia> correlativas;

  public Materia(
      String codigoDeMateria,
      String nombre,
      String contenidos,
      Integer creditosQueOtorga,
      Integer creditosNecesarios,
      TipoMateria tipo) {
    this.codigoDeMateria = codigoDeMateria;
    this.nombre = nombre;
    this.contenidos = contenidos;
    this.creditosQueOtorga = creditosQueOtorga;
    this.creditosNecesarios = creditosNecesarios;
    this.tipo = tipo;
  }

  public Materia() {}

  public List<String> getCodigosCorrelativas() {
    return correlativas.stream().map(Materia::getCodigoDeMateria).collect(Collectors.toList());
  }
}
