package com.upp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Alumno {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long matricula;

  @Getter
  @Setter
  private String nombre;
  @Getter
  @Setter
  private String apellido;
  @Getter
  @Setter
  private Long dni;
  @Getter
  @Setter
  private String email;
  @Getter
  @Setter
  private String direccion;
  @Getter
  @Setter
  private LocalDate fechaNacimiento;
  @Getter
  @Setter
  private LocalDate fechaIngreso;
  @Getter
  @Setter
  private LocalDate fechaEgreso;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "telefono")
  private List<String> telefonos;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "carrera")
  private List<String> carreras;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "planDeEstudio")
  private List<String> planesDeEstudio;

  public Alumno() {}

  public Alumno(
      Long dni,
      String nombre,
      String apellido,
      String email,
      String direccion,
      LocalDate fechaNacimiento,
      LocalDate fechaIngreso,
      List<String> telefonos) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.email = email;
    this.direccion = direccion;
    this.fechaNacimiento = fechaNacimiento;
    this.fechaIngreso = fechaIngreso;
    this.telefonos = telefonos;
  }

  //inscripcionesService
//
//inscribirAlumnoEnMateria(materiaId, alumnoId):
//alumno = alumnoRepo.find(alumnoId)
//materia = materiaRepo.find(materiaId)
//validarInscripcion(materia,alumno)
//alumno.inscribirEnMateria(materia)
//
//
//-->
//inscribirEnMateria(materia)
//
//materias = this.getMaterias
//materias.append(materia)
//
//
//
//
//
}
