package com.upp.dto;

import com.upp.model.TipoMateria;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class MateriaDTO {

  private String codigoDeMateria;

  private String nombre;

  private String contenidos;

  private Integer creditosQueOtorga;

  private Integer creditosNecesarios;

  private TipoMateria tipo;
  
  private Integer cuatrimestre;
  
  private String codigoPlanDeEstudios;
  
  private List<String> codigosCorrelativas;

  public String getCodigoDeMateria() {
    return codigoDeMateria;
  }

  public String getNombre() {
    return nombre;
  }

  public String getContenidos() {
    return contenidos;
  }

  public Integer getCreditosQueOtorga() {
    return creditosQueOtorga;
  }

  public TipoMateria getTipo() {
    return tipo;
  }

  public Integer getCreditosNecesarios() {
    return creditosNecesarios;
  }

  public List<String> getCodigosCorrelativas() {
    return codigosCorrelativas;
  }

  public MateriaDTO(
      String codigoDeMateria,
      String nombre,
      String contenidos,
      Integer creditosQueOtorga,
      Integer creditosNecesarios,
      TipoMateria tipo,
      Integer cuatrimestre,
      String codigoPlanDeEstudios,
      List<String> codigosCorrelativas) {
    this.codigoDeMateria = codigoDeMateria;
    this.nombre = nombre;
    this.contenidos = contenidos;
    this.creditosQueOtorga = creditosQueOtorga;
    this.creditosNecesarios = creditosNecesarios;
    this.tipo = tipo;
    this.cuatrimestre = cuatrimestre;
    this.codigoPlanDeEstudios = codigoPlanDeEstudios;
    this.codigosCorrelativas = codigosCorrelativas;
  }
}
