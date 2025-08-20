package com.upp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Alumno extends Usuario {

  @Column(unique = true, nullable = false)
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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "alumno_carrera",
      joinColumns = @JoinColumn(name = "alumno_id"),
      inverseJoinColumns = @JoinColumn(name = "carrera_codigo"))
  private List<Carrera> carreras;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "alumno_plan_estudios",
      joinColumns = @JoinColumn(name = "alumno_id"),
      inverseJoinColumns = @JoinColumn(name = "plan_codigo"))
  private List<PlanDeEstudios> planesDeEstudio;

  public Alumno() {
    super();
  }

  public Alumno(
      String username,
      String password,
      Long dni,
      String nombre,
      String apellido,
      String email,
      String direccion,
      LocalDate fechaNacimiento,
      LocalDate fechaIngreso,
      List<String> telefonos) {
    super();
    this.setUsername(username);
    this.setPassword(password);
    this.dni = dni;
    this.nombre = nombre;
    this.apellido = apellido;
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
