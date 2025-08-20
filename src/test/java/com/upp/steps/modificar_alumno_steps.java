package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.AlumnoDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class modificar_alumno_steps {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<AlumnoDTO> result;
  private FluxExchangeResult<Map> errorResult;


  @Cuando("se modifican los datos del alumno con matrícula {long} con nombre {string}, apellido {string}, dni {long}, email {string}, dirección {string}, teléfonos {string}, fecha de nacimiento {string}, fecha de ingreso {string} y fecha de egreso {string}")
  public void seModificanLosDatosDelAlumno(
      Long matricula,
      String nombre,
      String apellido,
      Long dni,
      String email,
      String direccion,
      String telefonos,
      String fechaNacimiento,
      String fechaIngreso,
      String fechaEgreso) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setMatricula(matricula);
    alumnoDTO.setNombre(nombre);
    alumnoDTO.setApellido(apellido);
    alumnoDTO.setDni(dni);
    alumnoDTO.setEmail(email);
    alumnoDTO.setDireccion(direccion);
    alumnoDTO.setTelefonos(List.of(telefonos));
    alumnoDTO.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
    alumnoDTO.setFechaIngreso(LocalDate.parse(fechaIngreso, formatter));
    alumnoDTO.setFechaEgreso(LocalDate.parse(fechaEgreso, formatter));

    this.result =
        webTestClient
            .put()
            .uri("/api/alumnos/{matricula}", matricula)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(alumnoDTO)
            .exchange()
            .returnResult(AlumnoDTO.class);
  }

  @Cuando("se intenta modificar los datos del alumno con matrícula {long} con nombre {string}, apellido {string}, dni {long}, email {string}, dirección {string}, teléfonos {string}, fecha de nacimiento {string}, fecha de ingreso {string} y fecha de egreso {string}")
  public void seIntentaModificarLosDatosDelAlumno(
      Long matricula,
      String nombre,
      String apellido,
      Long dni,
      String email,
      String direccion,
      String telefonos,
      String fechaNacimiento,
      String fechaIngreso,
      String fechaEgreso) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    AlumnoDTO alumnoDTO = new AlumnoDTO();
    alumnoDTO.setMatricula(matricula);
    alumnoDTO.setNombre(nombre);
    alumnoDTO.setApellido(apellido);
    alumnoDTO.setDni(dni);
    alumnoDTO.setEmail(email);
    alumnoDTO.setDireccion(direccion);
    alumnoDTO.setTelefonos(List.of(telefonos));
    alumnoDTO.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
    alumnoDTO.setFechaIngreso(LocalDate.parse(fechaIngreso, formatter));
    alumnoDTO.setFechaEgreso(LocalDate.parse(fechaEgreso, formatter));

    this.errorResult =
        webTestClient
            .put()
            .uri("/api/alumnos/{matricula}", matricula)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(alumnoDTO)
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("se actualizan los datos del alumno exitosamente")
  public void seActualizanLosDatosDelAlumnoExitosamente() {
    assertEquals(HttpStatus.OK, result.getStatus());
  }

  @Y("el alumno con matrícula {long} tiene los nuevos datos")
  public void elAlumnoConMatriculaTieneLosDatosNuevos(Long matricula) {
    var getResult = webTestClient
        .get()
        .uri("/api/alumnos/{matricula}", matricula)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .exchange()
        .returnResult(AlumnoDTO.class);
    
    assertEquals(HttpStatus.OK, getResult.getStatus());
    AlumnoDTO alumno = getResult.getResponseBody().blockFirst();
    assertNotNull(alumno);
    assertEquals("Carlos", alumno.getNombre());
    assertEquals("Rodriguez", alumno.getApellido());
  }

  @Entonces("se obtiene el error {string}")
  public void seObtieneElError(String errorMessage) {
    assertEquals(HttpStatus.NOT_FOUND, errorResult.getStatus());
    Map<String, String> errorResponse = errorResult.getResponseBody().blockFirst();
    assertNotNull(errorResponse);
    assertEquals(errorMessage, errorResponse.get("error"));
  }

  @Entonces("la matrícula del alumno permanece como {long}")
  public void laMatriculaDelAlumnoPermaneceComo(Long matricula) {
    var getResult = webTestClient
        .get()
        .uri("/api/alumnos/{matricula}", matricula)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .exchange()
        .returnResult(AlumnoDTO.class);
    
    assertEquals(HttpStatus.OK, getResult.getStatus());
    AlumnoDTO alumno = getResult.getResponseBody().blockFirst();
    assertNotNull(alumno);
    assertEquals(matricula, alumno.getMatricula());
  }
}