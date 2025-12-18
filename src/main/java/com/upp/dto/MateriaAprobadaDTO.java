package com.upp.dto;

import java.time.LocalDateTime;

public class MateriaAprobadaDTO {
  private String codigoMateria;
  private String nombre;
  private Integer nota;
  private Integer creditosQueOtorga;
  private LocalDateTime fechaAprobacion;
  private Long numeroCorrelativoActa;

  public MateriaAprobadaDTO() {}

  public MateriaAprobadaDTO(
      String codigoMateria,
      String nombre,
      Integer nota,
      Integer creditosQueOtorga,
      LocalDateTime fechaAprobacion,
      Long numeroCorrelativoActa) {
    this.codigoMateria = codigoMateria;
    this.nombre = nombre;
    this.nota = nota;
    this.creditosQueOtorga = creditosQueOtorga;
    this.fechaAprobacion = fechaAprobacion;
    this.numeroCorrelativoActa = numeroCorrelativoActa;
  }

  public String getCodigoMateria() {
    return codigoMateria;
  }

  public void setCodigoMateria(String codigoMateria) {
    this.codigoMateria = codigoMateria;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Integer getNota() {
    return nota;
  }

  public void setNota(Integer nota) {
    this.nota = nota;
  }

  public Integer getCreditosQueOtorga() {
    return creditosQueOtorga;
  }

  public void setCreditosQueOtorga(Integer creditosQueOtorga) {
    this.creditosQueOtorga = creditosQueOtorga;
  }

  public LocalDateTime getFechaAprobacion() {
    return fechaAprobacion;
  }

  public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
    this.fechaAprobacion = fechaAprobacion;
  }

  public Long getNumeroCorrelativoActa() {
    return numeroCorrelativoActa;
  }

  public void setNumeroCorrelativoActa(Long numeroCorrelativoActa) {
    this.numeroCorrelativoActa = numeroCorrelativoActa;
  }
}
