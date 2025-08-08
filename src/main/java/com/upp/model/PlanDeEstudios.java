package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlanDeEstudios {
  @Id @NotBlank private String codigoDePlanDeEstudios;

  @NotNull
  @Min(0)
  private Integer creditosObligatorios;

  @NotNull
  @Min(0)
  private Integer creditosElectivos;

  private LocalDate fechaEntradaEnVigencia;
  private LocalDate fechaVencimiento;

  @OneToMany(
      mappedBy = "planDeEstudios",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Materia> materias;

  @ManyToOne
  @JoinColumn(name = "carrera_codigo")
  private Carrera carrera;

  public PlanDeEstudios(
      String codigoDePlanDeEstudios,
      Integer creditosElectivos,
      LocalDate fechaEntradaEnVigencia,
      List<Materia> materias,
      LocalDate fechaVencimiento) {
    this.codigoDePlanDeEstudios = codigoDePlanDeEstudios;
    this.creditosElectivos = creditosElectivos;
    this.fechaEntradaEnVigencia = fechaEntradaEnVigencia;
    this.setMaterias(materias); // Usar setter para establecer relación bidireccional
    this.fechaVencimiento = fechaVencimiento;
  }

  public PlanDeEstudios() {}

  public void setMaterias(List<Materia> materias) {
    // Limpiar relaciones bidireccionales existentes
    if (this.materias != null) {
      this.materias.forEach(materia -> materia.setPlanDeEstudios(null));
    }

    this.materias = materias;
    // Establecer la relación bidireccional
    if (materias != null) {
      materias.forEach(materia -> materia.setPlanDeEstudios(this));
    }
    this.creditosObligatorios = _calcularCreditosObligatorios();
  }

  private int _calcularCreditosObligatorios() {
    if (materias == null) return 0;
    return materias.stream()
        .filter(Materia::esObligatoria)
        .mapToInt(Materia::getCreditosQueOtorga)
        .sum();
  }

  public List<String> getCodigosMaterias() {
    if (materias == null) return List.of();
    return materias.stream().map(Materia::getCodigoDeMateria).collect(Collectors.toList());
  }
}
