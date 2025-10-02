package com.upp.dto;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CarreraDTO {

  private String codigoDeCarrera;

  private String nombre;

  private String titulo;

  private String incumbencias;

  private List<String> codigosPlanesDeEstudio;

  public String getCodigoDeCarrera() {
    return codigoDeCarrera;
  }

  public String getNombre() {
    return nombre;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getIncumbencias() {
    return incumbencias;
  }

  public List<String> getCodigosPlanesDeEstudio() {
    return codigosPlanesDeEstudio;
  }

  public CarreraDTO(
      String codigoDeCarrera,
      String nombre,
      String titulo,
      String incumbencias,
      List<String> codigosPlanesDeEstudio) {
    this.codigoDeCarrera = codigoDeCarrera;
    this.nombre = nombre;
    this.titulo = titulo;
    this.incumbencias = incumbencias;
    this.codigosPlanesDeEstudio = codigosPlanesDeEstudio;
  }
}
