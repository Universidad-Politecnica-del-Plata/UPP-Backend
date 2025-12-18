package com.upp.dto;

import java.util.List;

public class EstadoAcademicoDTO {
  private Long alumnoId;
  private Integer creditosAcumulados;
  private Integer creditosTotalesRequeridos;
  private List<String> correlativasNoAprobadas;
  private boolean puedeInscribirse;
  private String motivoNoInscripcion;

  public EstadoAcademicoDTO() {}

  public EstadoAcademicoDTO(
      Long alumnoId,
      Integer creditosAcumulados,
      Integer creditosTotalesRequeridos,
      List<String> correlativasNoAprobadas,
      boolean puedeInscribirse,
      String motivoNoInscripcion) {
    this.alumnoId = alumnoId;
    this.creditosAcumulados = creditosAcumulados;
    this.creditosTotalesRequeridos = creditosTotalesRequeridos;
    this.correlativasNoAprobadas = correlativasNoAprobadas;
    this.puedeInscribirse = puedeInscribirse;
    this.motivoNoInscripcion = motivoNoInscripcion;
  }

  public Long getAlumnoId() {
    return alumnoId;
  }

  public void setAlumnoId(Long alumnoId) {
    this.alumnoId = alumnoId;
  }

  public Integer getCreditosAcumulados() {
    return creditosAcumulados;
  }

  public void setCreditosAcumulados(Integer creditosAcumulados) {
    this.creditosAcumulados = creditosAcumulados;
  }

  public Integer getCreditosTotalesRequeridos() {
    return creditosTotalesRequeridos;
  }

  public void setCreditosTotalesRequeridos(Integer creditosTotalesRequeridos) {
    this.creditosTotalesRequeridos = creditosTotalesRequeridos;
  }

  public List<String> getCorrelativasNoAprobadas() {
    return correlativasNoAprobadas;
  }

  public void setCorrelativasNoAprobadas(List<String> correlativasNoAprobadas) {
    this.correlativasNoAprobadas = correlativasNoAprobadas;
  }

  public boolean isPuedeInscribirse() {
    return puedeInscribirse;
  }

  public void setPuedeInscribirse(boolean puedeInscribirse) {
    this.puedeInscribirse = puedeInscribirse;
  }

  public String getMotivoNoInscripcion() {
    return motivoNoInscripcion;
  }

  public void setMotivoNoInscripcion(String motivoNoInscripcion) {
    this.motivoNoInscripcion = motivoNoInscripcion;
  }
}
