package com.upp.service;

import com.upp.dto.AlumnoDTO;
import com.upp.exception.AlumnoExisteException;
import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {
  private final AlumnoRepository alumnoRepository;

  public AlumnoService(AlumnoRepository alumnoRepository) {
    this.alumnoRepository = alumnoRepository;
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
}
