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

  @Column private Integer cuatrimestre;

  @ManyToOne
  @JoinColumn(name = "plan_de_estudios_codigo")
  private PlanDeEstudios planDeEstudios;

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

  public Boolean esObligatoria() {
    return this.tipo == TipoMateria.OBLIGATORIA;
  }
}
