package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.CuatrimestreDTO;
import com.upp.dto.CursoDTO;
import com.upp.repository.CursoRepository;
import com.upp.steps.shared.AuthHelper;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_curso_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  @Autowired private CursoRepository cursoRepository;
  @Autowired private AuthHelper authHelper;

  private FluxExchangeResult<CursoDTO> result;

  @Dado("que no existe un curso con código {string}")
  public void queNoExisteUnCursoConCodigo(String codigo) {
    authHelper.loginGestorPlanificacion();

    // Verificamos que no exista el curso
    var resultGet =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertNotEquals(
        HttpStatus.OK, resultGet.getStatus(), "No debería existir un curso con código " + codigo);
  }

  @Dado("que existe un cuatrimestre con código {string}")
  public void queExisteUnCuatrimestreConCodigo(String codigo) {
    authHelper.loginGestorPlanificacion();
    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 2, 28),
            LocalDate.of(2024, 7, 16),
            LocalDate.of(2024, 7, 31));

    webTestClient
        .post()
        .uri("/api/cuatrimestres")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(cuatrimestreEnviado)
        .exchange();
  }

  @Cuando(
      "se registra un nuevo curso con código {string}, máximo de alumnos {int} y materia {string}")
  public void darDeAltaCurso(String codigo, Integer maximoAlumnos, String codigoMateria) {
    authHelper.loginGestorPlanificacion();
    CursoDTO cursoEnviado = new CursoDTO(codigo, maximoAlumnos, codigoMateria, null);

    this.result =
        webTestClient
            .post()
            .uri("/api/cursos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoEnviado)
            .exchange()
            .returnResult(CursoDTO.class);
  }

  @Cuando(
      "se registra un nuevo curso con código {string}, máximo de alumnos {int}, materia {string} y cuatrimestres {string}")
  public void darDeAltaCursoConCuatrimestres(
      String codigo, Integer maximoAlumnos, String codigoMateria, String cuatrimestres) {
    authHelper.loginGestorPlanificacion();
    List<String> codigosCuatrimestres = null;
    if (cuatrimestres != null && !cuatrimestres.trim().isEmpty()) {
      codigosCuatrimestres =
          Arrays.stream(cuatrimestres.split(",")).map(String::trim).collect(Collectors.toList());
    }

    CursoDTO cursoEnviado =
        new CursoDTO(codigo, maximoAlumnos, codigoMateria, codigosCuatrimestres);

    this.result =
        webTestClient
            .post()
            .uri("/api/cursos")
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoEnviado)
            .exchange()
            .returnResult(CursoDTO.class);
  }

  @Entonces("se registra el curso {string} exitosamente")
  public void seRegistraElCursoExitosamente(String codigo) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());
  }

  @Entonces("el curso {string} está asignado al cuatrimestre {string}")
  public void elCursoEstaAsignadoAlCuatrimestre(String codigoCurso, String codigoCuatrimestre) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigoCurso)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());

    CursoDTO curso = resultGetCurso.getResponseBody().blockFirst();
    assertTrue(
        curso.getCodigosCuatrimestres() != null
            && curso.getCodigosCuatrimestres().contains(codigoCuatrimestre),
        "El curso no está asignado al cuatrimestre esperado");
  }

  @Entonces("el curso {string} está asignado a los cuatrimestres {string}")
  public void elCursoEstaAsignadoALosCuatrimestres(String codigoCurso, String cuatrimestres) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigoCurso)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());

    CursoDTO curso = resultGetCurso.getResponseBody().blockFirst();
    List<String> cuatrimestresList =
        Arrays.stream(cuatrimestres.split(",")).map(String::trim).collect(Collectors.toList());

    assertTrue(
        curso.getCodigosCuatrimestres() != null
            && curso.getCodigosCuatrimestres().containsAll(cuatrimestresList),
        "El curso no está asignado a todos los cuatrimestres esperados");
  }

  @Entonces("no se registra el curso exitosamente")
  public void noSeRegistraElCursoExitosamente() {
    // Puede ser CONFLICT (409) para código duplicado o BAD_REQUEST (400) para validaciones
    assertNotEquals(HttpStatus.OK, result.getStatus());
  }
}
