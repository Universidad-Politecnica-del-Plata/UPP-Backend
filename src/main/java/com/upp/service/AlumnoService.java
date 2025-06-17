package com.upp.service;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.model.Alumno;
import com.upp.model.Rol;
import com.upp.repository.AlumnoRepository;
import com.upp.repository.RolRepository;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {
  private final AlumnoRepository alumnoRepository;
  private final RolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;
  private static final String ID_ROL_ALUMNO = "ROLE_ALUMNO";

  public AlumnoService(
      AlumnoRepository alumnoRepository,
      RolRepository rolRepository,
      PasswordEncoder passwordEncoder) {
    this.alumnoRepository = alumnoRepository;
    this.rolRepository = rolRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AlumnoDTO crearAlumno(AlumnoDTO alumnoDTO) {
    if (alumnoRepository.existsByDniOrEmail(alumnoDTO.getDni(), alumnoDTO.getEmail())) {
      throw new AlumnoExisteException("Ya existe un alumno con ese DNI o email.");
    }
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
    //        TODO: mapear a objeto Carrera
    alumno.setCarreras(alumnoDTO.getCodigosCarreras());
    //        TODO: mapear a objeto Plan de estudios
    alumno.setPlanesDeEstudio(alumnoDTO.getCodigosPlanesDeEstudio());

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
            alumnoGuardado.getCarreras(),
            alumnoGuardado.getPlanesDeEstudio());

    return alumnoGuardadoDTO;
  }

  private Long obtenerUltimaMatricula() {
    Long ultimaMatricula = alumnoRepository.findMaxMatricula();
    if (ultimaMatricula == null) {
      ultimaMatricula = 100000L;
    }
    return ultimaMatricula;
  }

  private Rol obtenerRolAlumno() {
    return rolRepository
        .findById(ID_ROL_ALUMNO)
        .orElseGet(
            () -> {
              Rol nuevo = new Rol(ID_ROL_ALUMNO);
              return rolRepository.save(nuevo);
            });
  }
}
