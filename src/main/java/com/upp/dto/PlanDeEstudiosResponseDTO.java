package com.upp.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanDeEstudiosResponseDTO extends PlanDeEstudiosRequestDTO {
  private Integer creditosObligatorios;
  private String codigoCarrera;

  public PlanDeEstudiosResponseDTO(
      String codigoDePlanDeEstudios,
      Integer creditosElectivos,
      LocalDate fechaEntradaEnVigencia,
      LocalDate fechaVencimiento,
      List<String> codigosMaterias,
      Integer creditosObligatorios,
      String codigoCarrera) {
    super(
        codigoDePlanDeEstudios,
        creditosElectivos,
        fechaEntradaEnVigencia,
        fechaVencimiento,
        codigosMaterias);
    this.creditosObligatorios = creditosObligatorios;
    this.codigoCarrera = codigoCarrera;
  }
}
