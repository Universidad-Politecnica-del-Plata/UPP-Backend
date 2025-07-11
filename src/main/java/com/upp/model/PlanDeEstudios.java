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
  @OneToMany private List<Materia> materias;

  public PlanDeEstudios(
      String codigoDePlanDeEstudios,
      Integer creditosElectivos,
      LocalDate fechaEntradaEnVigencia,
      List<Materia> materias,
      LocalDate fechaVencimiento) {
    this.codigoDePlanDeEstudios = codigoDePlanDeEstudios;
    this.creditosElectivos = creditosElectivos;
    this.fechaEntradaEnVigencia = fechaEntradaEnVigencia;
    this.materias = materias;
    this.creditosObligatorios = _calcularCreditosObligatorios();
    this.fechaVencimiento = fechaVencimiento;
  }

  public PlanDeEstudios() {}

  public void setMaterias(List<Materia> materias) {
    this.materias = materias;
    this.creditosObligatorios = _calcularCreditosObligatorios();
  }

  private int _calcularCreditosObligatorios() {
    return materias.stream()
        .filter(Materia::esObligatoria)
        .mapToInt(Materia::getCreditosQueOtorga)
        .sum();
  }

  public List<String> getCodigosMaterias() {
    return materias.stream().map(Materia::getCodigoDeMateria).collect(Collectors.toList());
  }
}
