package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.model.Alumno;
import com.upp.repository.AlumnoRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_alumno_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private AlumnoRepository alumnoRepository;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<Map> result;

  @Cuando("se da de baja el alumno con matrícula {long}")
  public void seDaDeBajaElAlumnoConMatricula(Long matricula) {
    this.result =
        webTestClient
            .delete()
            .uri("/api/alumnos/{matricula}", matricula)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Cuando("se intenta dar de baja el alumno con matrícula {long}")
  public void seIntentaDarDeBajaElAlumnoConMatricula(Long matricula) {
    this.result =
        webTestClient
            .delete()
            .uri("/api/alumnos/{matricula}", matricula)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("se da de baja el alumno exitosamente")
  public void seDaDeBajaElAlumnoExitosamente() {
    assertEquals(HttpStatus.OK, result.getStatus());
    Map<String, String> response = result.getResponseBody().blockFirst();
    assertNotNull(response);
    assertEquals("Alumno dado de baja exitosamente", response.get("message"));
  }

  @Y("el alumno con matrícula {long} está inhabilitado")
  public void elAlumnoConMatriculaEstaInhabilitado(Long matricula) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByMatricula(matricula);
    assertTrue(alumnoOpt.isPresent());
    Alumno alumno = alumnoOpt.get();
    assertFalse(alumno.isHabilitado());
  }

  @Entonces("el alumno con matrícula {long} sigue existiendo en el sistema pero inhabilitado")
  public void elAlumnoConMatriculaSigueExistiendoEnElSistemaPeroInhabilitado(Long matricula) {
    Optional<Alumno> alumnoOpt = alumnoRepository.findByMatricula(matricula);
    assertTrue(alumnoOpt.isPresent(), "El alumno debería seguir existiendo en el sistema");
    Alumno alumno = alumnoOpt.get();
    assertFalse(alumno.isHabilitado(), "El alumno debería estar inhabilitado");
    assertEquals(matricula, alumno.getMatricula(), "La matrícula debería mantenerse");
  }

  @Entonces("no se puede dar de baja el alumno")
  public void noSePuedeDarDeBajaElAlumno() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }
}
