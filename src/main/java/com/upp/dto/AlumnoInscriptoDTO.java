package com.upp.dto;

public class AlumnoInscriptoDTO {

  private Long id;
  private String nombre;
  private String apellido;
  private Long matricula;
  private String email;

  public AlumnoInscriptoDTO() {}

  public AlumnoInscriptoDTO(Long id, String nombre, String apellido, Long matricula, String email) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.matricula = matricula;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Long getMatricula() {
    return matricula;
  }

  public void setMatricula(Long matricula) {
    this.matricula = matricula;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}