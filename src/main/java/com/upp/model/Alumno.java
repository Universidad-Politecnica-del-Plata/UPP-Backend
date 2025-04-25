package com.upp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alumno {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long matricula;

  private String nombre;
  private String apellido;
  private Long dni;
  private String email;
  private String direccion;
  private LocalDate fechaNacimiento;
  private LocalDate fechaIngreso;
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

  // inscripcionesService
  //
  // inscribirAlumnoEnMateria(materiaId, alumnoId):
  // alumno = alumnoRepo.find(alumnoId)
  // materia = materiaRepo.find(materiaId)
  // validarInscripcion(materia,alumno)
  // alumno.inscribirEnMateria(materia)
  //
  //
  // -->
  // inscribirEnMateria(materia)
  //
  // materias = this.getMaterias
  // materias.append(materia)
  //
  //
  //
  //
  //
}
