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
public class AlumnoDTO {

  private Long matricula;

  private String nombre;
  private String apellido;
  private Long dni;
  private String email;
  private String direccion;
  private LocalDate fechaNacimiento;
  private LocalDate fechaIngreso;
  private LocalDate fechaEgreso;
  private List<String> telefonos;
  private List<String> codigosCarreras;
  private List<String> codigosPlanesDeEstudio;

  public AlumnoDTO(
      Long matricula,
      String nombre,
      String apellido,
      Long dni,
      String email,
      String direccion,
      LocalDate fechaNacimiento,
      LocalDate fechaIngreso,
      LocalDate fechaEgreso,
      List<String> telefonos,
      List<String> codigosCarreras,
      List<String> codigosPlanesDeEstudio) {
    this.matricula = matricula;
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.email = email;
    this.direccion = direccion;
    this.fechaNacimiento = fechaNacimiento;
    this.fechaIngreso = fechaIngreso;
    this.fechaEgreso = fechaEgreso;
    this.telefonos = telefonos;
    this.codigosCarreras = codigosCarreras;
    this.codigosPlanesDeEstudio = codigosPlanesDeEstudio;
  }

  public Long getMatricula() {
    return matricula;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public Long getDni() {
    return dni;
  }

  public String getEmail() {
    return email;
  }

  public String getDireccion() {
    return direccion;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public LocalDate getFechaEgreso() {
    return fechaEgreso;
  }

  public List<String> getTelefonos() {
    return telefonos;
  }

  public List<String> getCodigosCarreras() {
    return codigosCarreras;
  }

  public List<String> getCodigosPlanesDeEstudio() {
    return codigosPlanesDeEstudio;
  }
}
