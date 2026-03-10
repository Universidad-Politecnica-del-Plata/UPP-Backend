package com.upp.steps;

import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import com.upp.service.AlumnoService;
import com.upp.steps.shared.AuthHelper;
import io.cucumber.java.es.Dado;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class alumno_shared_steps {

  @Autowired private AlumnoRepository alumnoRepository;
  @Autowired private AlumnoService alumnoService;
  @Autowired private AuthHelper authHelper;

  @Dado("existe un alumno con matrícula {long}")
  public void existeUnAlumnoConMatricula(Long matricula) {
    authHelper.loginGestorEstudiantil();
    // Limpiar alumno existente
    alumnoRepository.findByMatricula(matricula).ifPresent(alumnoRepository::delete);

    // Crear alumno de prueba
    Alumno alumno = new Alumno();
    alumno.setMatricula(matricula);
    alumno.setNombre("Juan");
    alumno.setApellido("Perez");
    alumno.setDni(12345678L);
    alumno.setEmail("juan.perez@example.com");
    alumno.setDireccion("Calle Falsa 123");
    alumno.setFechaNacimiento(LocalDate.of(1990, 1, 1));
    alumno.setFechaIngreso(LocalDate.of(2023, 3, 1));
    alumno.setTelefonos(List.of("123456789"));
    alumno.setUsername("12345678");
    alumno.setPassword("12345678");
    alumno.setHabilitado(true);

    alumnoRepository.save(alumno);
  }
}
