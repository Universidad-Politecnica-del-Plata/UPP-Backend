package com.upp.dto;

import java.util.List;

public class HistoriaAcademicaDTO {
  private Long alumnoId;
  private String nombreCompleto;
  private Long matricula;
  private Integer creditosAcumulados;
  private List<MateriaAprobadaDTO> materiasAprobadas;

  public HistoriaAcademicaDTO() {}

  public HistoriaAcademicaDTO(
      Long alumnoId,
      String nombreCompleto,
      Long matricula,
      Integer creditosAcumulados,
      List<MateriaAprobadaDTO> materiasAprobadas) {
    this.alumnoId = alumnoId;
    this.nombreCompleto = nombreCompleto;
    this.matricula = matricula;
    this.creditosAcumulados = creditosAcumulados;
    this.materiasAprobadas = materiasAprobadas;
  }

  public Long getAlumnoId() {
    return alumnoId;
  }

  public void setAlumnoId(Long alumnoId) {
    this.alumnoId = alumnoId;
  }

  public String getNombreCompleto() {
    return nombreCompleto;
  }

  public void setNombreCompleto(String nombreCompleto) {
    this.nombreCompleto = nombreCompleto;
  }

  public Long getMatricula() {
    return matricula;
  }

  public void setMatricula(Long matricula) {
    this.matricula = matricula;
  }

  public Integer getCreditosAcumulados() {
    return creditosAcumulados;
  }

  public void setCreditosAcumulados(Integer creditosAcumulados) {
    this.creditosAcumulados = creditosAcumulados;
  }

  public List<MateriaAprobadaDTO> getMateriasAprobadas() {
    return materiasAprobadas;
  }

  public void setMateriasAprobadas(List<MateriaAprobadaDTO> materiasAprobadas) {
    this.materiasAprobadas = materiasAprobadas;
  }
}