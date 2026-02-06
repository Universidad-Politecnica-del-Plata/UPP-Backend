package com.upp.service;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.exception.AlumnoNoExisteException;
import com.upp.exception.FechasInvalidasException;
import com.upp.exception.PlanNoCorrespondeACarreraException;
import com.upp.model.Alumno;
import com.upp.model.Carrera;
import com.upp.model.PlanDeEstudios;
import com.upp.model.Rol;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.CarreraRepository;
import com.upp.repository.PlanDeEstudiosRepository;
import com.upp.repository.RolRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {
  private final AlumnoRepository alumnoRepository;
  private final RolRepository rolRepository;
  private final CarreraRepository carreraRepository;
  private final PlanDeEstudiosRepository planDeEstudiosRepository;
  private final PasswordEncoder passwordEncoder;
  private static final String ID_ROL_ALUMNO = "ROLE_ALUMNO";

  public AlumnoService(
      AlumnoRepository alumnoRepository,
      RolRepository rolRepository,
      CarreraRepository carreraRepository,
      PlanDeEstudiosRepository planDeEstudiosRepository,
      PasswordEncoder passwordEncoder) {
    this.alumnoRepository = alumnoRepository;
    this.rolRepository = rolRepository;
    this.carreraRepository = carreraRepository;
    this.planDeEstudiosRepository = planDeEstudiosRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AlumnoDTO crearAlumno(AlumnoDTO alumnoDTO) {
    if (alumnoRepository.existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail())) {
      throw new AlumnoExisteException("Ya existe un alumno con ese DNI o email.");
    }
    validarFechas(alumnoDTO);
    Alumno alumno = new Alumno();
    alumno.setNombre(alumnoDTO.getNombre());
    alumno.setApellido(alumnoDTO.getApellido());
    alumno.setDni(alumnoDTO.getDni());
    alumno.setEmail(alumnoDTO.getEmail());
    alumno.setDireccion(alumnoDTO.getDireccion());
    alumno.setFechaNacimiento(alumnoDTO.getFechaNacimiento());
    alumno.setFechaIngreso(alumnoDTO.getFechaIngreso());
    alumno.setFechaEgreso(alumnoDTO.getFechaEgreso());
    alumno.setTelefonos(alumnoDTO.getTelefonos());

    List<Carrera> carreras = obtenerCarreras(alumnoDTO.getCodigosCarreras());
    List<PlanDeEstudios> planes = obtenerPlanesDeEstudio(alumnoDTO.getCodigosPlanesDeEstudio());
    validarPlanesCorrespondenACarreras(carreras, planes);

    alumno.setCarreras(carreras);
    alumno.setPlanesDeEstudio(planes);

    Long ultimaMatricula = obtenerUltimaMatricula();
    alumno.setMatricula(ultimaMatricula + 1);
    alumno.setUsername(alumnoDTO.getDni().toString());
    alumno.setPassword(passwordEncoder.encode(alumnoDTO.getDni().toString()));

    alumno.setRoles(Set.of(obtenerRolAlumno()));
    Alumno alumnoGuardado = alumnoRepository.save(alumno);

    AlumnoDTO alumnoGuardadoDTO =
        new AlumnoDTO(
            alumnoGuardado.getMatricula(),
            alumnoGuardado.getNombre(),
            alumnoGuardado.getApellido(),
            alumnoGuardado.getDni(),
            alumnoGuardado.getEmail(),
            alumnoGuardado.getDireccion(),
            alumnoGuardado.getFechaNacimiento(),
            alumnoGuardado.getFechaIngreso(),
            alumnoGuardado.getFechaEgreso(),
            alumnoGuardado.getTelefonos(),
            obtenerCodigosCarreras(alumnoGuardado.getCarreras()),
            obtenerCodigosPlanesDeEstudio(alumnoGuardado.getPlanesDeEstudio()));

    return alumnoGuardadoDTO;
  }

  private void validarFechas(AlumnoDTO alumnoDTO) {
    if (alumnoDTO.getFechaNacimiento() != null && alumnoDTO.getFechaIngreso() != null) {
      if (!alumnoDTO.getFechaIngreso().isAfter(alumnoDTO.getFechaNacimiento())) {
        throw new FechasInvalidasException(
            "La fecha de ingreso debe ser posterior a la fecha de nacimiento.");
      }
    }
    if (alumnoDTO.getFechaIngreso() != null && alumnoDTO.getFechaEgreso() != null) {
      if (!alumnoDTO.getFechaEgreso().isAfter(alumnoDTO.getFechaIngreso())) {
        throw new FechasInvalidasException(
            "La fecha de egreso debe ser posterior a la fecha de ingreso.");
      }
    }
  }

  private void validarPlanesCorrespondenACarreras(
      List<Carrera> carreras, List<PlanDeEstudios> planes) {
    if (planes == null || planes.isEmpty()) {
      return;
    }
    Set<String> codigosCarreras =
        carreras.stream().map(Carrera::getCodigoDeCarrera).collect(Collectors.toSet());

    for (PlanDeEstudios plan : planes) {
      if (plan.getCarrera() == null
          || !codigosCarreras.contains(plan.getCarrera().getCodigoDeCarrera())) {
        throw new PlanNoCorrespondeACarreraException(
            "El plan de estudios '"
                + plan.getCodigoDePlanDeEstudios()
                + "' no corresponde a ninguna carrera asignada al alumno.");
      }
    }
  }

  private Long obtenerUltimaMatricula() {
    Long ultimaMatricula = alumnoRepository.findMaxMatricula();
    if (ultimaMatricula == null) {
      ultimaMatricula = 100000L;
    }
    return ultimaMatricula;
  }

  public AlumnoDTO obtenerAlumnoPorMatricula(Long matricula) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByMatricula(matricula);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con esa matrícula.");
    }
    Alumno alumno = alumnoOpt.get();
    return new AlumnoDTO(
        alumno.getMatricula(),
        alumno.getNombre(),
        alumno.getApellido(),
        alumno.getDni(),
        alumno.getEmail(),
        alumno.getDireccion(),
        alumno.getFechaNacimiento(),
        alumno.getFechaIngreso(),
        alumno.getFechaEgreso(),
        alumno.getTelefonos(),
        obtenerCodigosCarreras(alumno.getCarreras()),
        obtenerCodigosPlanesDeEstudio(alumno.getPlanesDeEstudio()));
  }

  public List<AlumnoDTO> obtenerTodosLosAlumnos() {
    List<Alumno> alumnos = alumnoRepository.findAll();
    return alumnos.stream()
        .map(
            alumno ->
                new AlumnoDTO(
                    alumno.getMatricula(),
                    alumno.getNombre(),
                    alumno.getApellido(),
                    alumno.getDni(),
                    alumno.getEmail(),
                    alumno.getDireccion(),
                    alumno.getFechaNacimiento(),
                    alumno.getFechaIngreso(),
                    alumno.getFechaEgreso(),
                    alumno.getTelefonos(),
                    obtenerCodigosCarreras(alumno.getCarreras()),
                    obtenerCodigosPlanesDeEstudio(alumno.getPlanesDeEstudio())))
        .collect(Collectors.toList());
  }

  public List<AlumnoDTO> obtenerAlumnosActivos() {
    List<Alumno> alumnosActivos = alumnoRepository.findByHabilitadoTrue();
    return alumnosActivos.stream()
        .map(
            alumno ->
                new AlumnoDTO(
                    alumno.getMatricula(),
                    alumno.getNombre(),
                    alumno.getApellido(),
                    alumno.getDni(),
                    alumno.getEmail(),
                    alumno.getDireccion(),
                    alumno.getFechaNacimiento(),
                    alumno.getFechaIngreso(),
                    alumno.getFechaEgreso(),
                    alumno.getTelefonos(),
                    obtenerCodigosCarreras(alumno.getCarreras()),
                    obtenerCodigosPlanesDeEstudio(alumno.getPlanesDeEstudio())))
        .collect(Collectors.toList());
  }

  public AlumnoDTO modificarAlumno(Long matricula, AlumnoDTO alumnoDTO) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByMatricula(matricula);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con esa matrícula.");
    }
    validarFechas(alumnoDTO);

    Alumno alumno = alumnoOpt.get();

    // Actualizar todos los campos excepto matricula
    alumno.setNombre(alumnoDTO.getNombre());
    alumno.setApellido(alumnoDTO.getApellido());
    alumno.setDni(alumnoDTO.getDni());
    alumno.setEmail(alumnoDTO.getEmail());
    alumno.setDireccion(alumnoDTO.getDireccion());
    alumno.setFechaNacimiento(alumnoDTO.getFechaNacimiento());
    alumno.setFechaIngreso(alumnoDTO.getFechaIngreso());
    alumno.setFechaEgreso(alumnoDTO.getFechaEgreso());
    alumno.setTelefonos(alumnoDTO.getTelefonos());

    List<Carrera> carreras = obtenerCarreras(alumnoDTO.getCodigosCarreras());
    List<PlanDeEstudios> planes = obtenerPlanesDeEstudio(alumnoDTO.getCodigosPlanesDeEstudio());
    validarPlanesCorrespondenACarreras(carreras, planes);

    alumno.setCarreras(carreras);
    alumno.setPlanesDeEstudio(planes);

    alumnoRepository.save(alumno);
    return alumnoDTO;
  }

  public void eliminarAlumno(Long matricula) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByMatricula(matricula);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con esa matrícula.");
    }

    Alumno alumno = alumnoOpt.get();
    // Baja lógica
    alumno.setHabilitado(false);
    alumnoRepository.save(alumno);
  }

  private List<Carrera> obtenerCarreras(List<String> codigosCarreras) {
    if (codigosCarreras == null) {
      return new ArrayList<>();
    }
    return codigosCarreras.stream()
        .map(
            codigo ->
                carreraRepository
                    .findByCodigoDeCarrera(codigo)
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada: " + codigo)))
        .collect(Collectors.toList());
  }

  private List<PlanDeEstudios> obtenerPlanesDeEstudio(List<String> codigosPlanesDeEstudio) {
    if (codigosPlanesDeEstudio == null) {
      return new ArrayList<>();
    }
    return codigosPlanesDeEstudio.stream()
        .map(
            codigo ->
                planDeEstudiosRepository
                    .findByCodigoDePlanDeEstudios(codigo)
                    .orElseThrow(
                        () -> new RuntimeException("Plan de estudios no encontrado: " + codigo)))
        .collect(Collectors.toList());
  }

  private List<String> obtenerCodigosCarreras(List<Carrera> carreras) {
    if (carreras == null) {
      return new ArrayList<>();
    }
    return carreras.stream().map(Carrera::getCodigoDeCarrera).collect(Collectors.toList());
  }

  private List<String> obtenerCodigosPlanesDeEstudio(List<PlanDeEstudios> planesDeEstudio) {
    if (planesDeEstudio == null) {
      return new ArrayList<>();
    }
    return planesDeEstudio.stream()
        .map(PlanDeEstudios::getCodigoDePlanDeEstudios)
        .collect(Collectors.toList());
  }

  public AlumnoDTO obtenerAlumnoActual(String username) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByUsername(username);
    if (alumnoOpt.isEmpty()) {
      throw new AlumnoNoExisteException("No existe un alumno con ese username.");
    }
    Alumno alumno = alumnoOpt.get();
    return new AlumnoDTO(
        alumno.getMatricula(),
        alumno.getNombre(),
        alumno.getApellido(),
        alumno.getDni(),
        alumno.getEmail(),
        alumno.getDireccion(),
        alumno.getFechaNacimiento(),
        alumno.getFechaIngreso(),
        alumno.getFechaEgreso(),
        alumno.getTelefonos(),
        obtenerCodigosCarreras(alumno.getCarreras()),
        obtenerCodigosPlanesDeEstudio(alumno.getPlanesDeEstudio()));
  }

  private Rol obtenerRolAlumno() {
    return rolRepository
        .findById(ID_ROL_ALUMNO)
        .orElseThrow(() -> new RuntimeException("ROLE_ALUMNO no encontrado"));
  }
}
