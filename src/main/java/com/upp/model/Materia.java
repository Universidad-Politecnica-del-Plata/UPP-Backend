package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Materia {
  @Id @NotBlank private String codigoDeMateria;
  @NotBlank private String nombre;

  @Column(columnDefinition = "TEXT", nullable = false)
  @NotBlank
  private String contenidos;

  @NotNull
  @Min(0)
  private Integer creditosQueOtorga;

  @NotNull
  @Min(0)
  private Integer creditosNecesarios;

  @Enumerated(EnumType.STRING)
  @NotNull
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

  public String getCodigoDeMateria() {
    return codigoDeMateria;
  }

  public String getNombre() {
    return nombre;
  }

  public String getContenidos() {
    return contenidos;
  }

  public Integer getCreditosQueOtorga() {
    return creditosQueOtorga;
  }

  public Integer getCreditosNecesarios() {
    return creditosNecesarios;
  }

  public TipoMateria getTipo() {
    return tipo;
  }

  public List<Materia> getCorrelativas() {
    return correlativas;
  }

  public void setCodigoDeMateria(String codigoDeMateria) {
    this.codigoDeMateria = codigoDeMateria;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setContenidos(String contenidos) {
    this.contenidos = contenidos;
  }

  public void setCreditosQueOtorga(Integer creditosQueOtorga) {
    this.creditosQueOtorga = creditosQueOtorga;
  }

  public void setCreditosNecesarios(Integer creditosNecesarios) {
    this.creditosNecesarios = creditosNecesarios;
  }

  public void setTipo(TipoMateria tipo) {
    this.tipo = tipo;
  }

  public void setCorrelativas(List<Materia> correlativas) {
    this.correlativas = correlativas;
  }
}
