package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Carrera {
  @Id @NotBlank private String codigoDeCarrera;

  @NotBlank private String nombre;

  @NotBlank private String titulo;

  @Column(columnDefinition = "TEXT")
  private String incumbencias;

  @OneToMany(mappedBy = "carrera", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private List<PlanDeEstudios> planesDeEstudio;

  public Carrera(
      String codigoDeCarrera,
      String nombre,
      String titulo,
      String incumbencias,
      List<PlanDeEstudios> planesDeEstudio) {
    this.codigoDeCarrera = codigoDeCarrera;
    this.nombre = nombre;
    this.titulo = titulo;
    this.incumbencias = incumbencias;
    this.setPlanesDeEstudio(planesDeEstudio);
  }

  public Carrera() {}

  public void setPlanesDeEstudio(List<PlanDeEstudios> planesDeEstudio) {
    // Limpiar relaciones bidireccionales existentes
    if (this.planesDeEstudio != null) {
      this.planesDeEstudio.forEach(planDeEstudios -> planDeEstudios.setCarrera(null));
    }
    this.planesDeEstudio = planesDeEstudio;
    // Establecer la relación bidireccional
    if (planesDeEstudio != null) {
      planesDeEstudio.forEach(planDeEstudios -> planDeEstudios.setCarrera(this));
    }
  }
}
