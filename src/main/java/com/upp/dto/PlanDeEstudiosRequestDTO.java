package com.upp.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PlanDeEstudiosRequestDTO {

  private String codigoDePlanDeEstudios;

  private Integer creditosElectivos;

  private LocalDate fechaEntradaEnVigencia;
  private LocalDate fechaVencimiento;

  private List<String> codigosMaterias;

  public String getCodigoDePlanDeEstudios() {
    return codigoDePlanDeEstudios;
  }

  public Integer getCreditosElectivos() {
    return creditosElectivos;
  }

  public LocalDate getFechaEntradaEnVigencia() {
    return fechaEntradaEnVigencia;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public List<String> getCodigosMaterias() {
    return codigosMaterias;
  }

  public PlanDeEstudiosRequestDTO(
      String codigoDePlanDeEstudios,
      Integer creditosElectivos,
      LocalDate fechaEntradaEnVigencia,
      LocalDate fechaVencimiento,
      List<String> codigosMaterias) {
    this.codigoDePlanDeEstudios = codigoDePlanDeEstudios;
    this.creditosElectivos = creditosElectivos;
    this.fechaEntradaEnVigencia = fechaEntradaEnVigencia;
    this.fechaVencimiento = fechaVencimiento;
    this.codigosMaterias = codigosMaterias;
  }
}
