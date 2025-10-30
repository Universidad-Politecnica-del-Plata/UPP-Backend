package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.upp.dto.CursoDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
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
public class modificar_datos_de_curso_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CursoDTO> result;

  @Cuando("se modifica el curso con código {string}, máximo de alumnos {int} y materia {string}")
  public void seModificaElCurso(String codigo, Integer maximoAlumnos, String codigoMateria) {
    CursoDTO cursoEnviado = new CursoDTO(codigo, maximoAlumnos, codigoMateria, null);

    this.result =
        webTestClient
            .put()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoEnviado)
            .exchange()
            .returnResult(CursoDTO.class);
  }

  @Cuando(
      "se modifica el curso con código {string}, máximo de alumnos {int}, materia {string} y cuatrimestres {string}")
  public void seModificaElCursoConCuatrimestres(
      String codigo, Integer maximoAlumnos, String codigoMateria, String cuatrimestres) {
    List<String> codigosCuatrimestres = null;
    if (cuatrimestres != null && !cuatrimestres.trim().isEmpty()) {
      codigosCuatrimestres =
          Arrays.stream(cuatrimestres.split(",")).map(String::trim).collect(Collectors.toList());
    }

    CursoDTO cursoEnviado =
        new CursoDTO(codigo, maximoAlumnos, codigoMateria, codigosCuatrimestres);

    this.result =
        webTestClient
            .put()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cursoEnviado)
            .exchange()
            .returnResult(CursoDTO.class);
  }

  @Entonces("se actualiza la información del curso {string} exitosamente")
  public void seActualizaLaInformacionDelCursoExitosamente(String codigo) {
    assertEquals(HttpStatus.OK, result.getStatus());

    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, resultGetCurso.getStatus());
  }

  @Entonces("el curso {string} tiene máximo de alumnos {int} y materia {string}")
  public void elCursoTieneLosDatosEsperados(
      String codigo, Integer maximoAlumnos, String codigoMateria) {
    var response =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CursoDTO curso = response.getResponseBody().blockFirst();
    assertNotNull(curso);
    assertEquals(codigo, curso.getCodigo());
    assertEquals(maximoAlumnos, curso.getMaximoDeAlumnos());
    assertEquals(codigoMateria, curso.getCodigoMateria());
  }

  @Entonces("el curso {string} no tiene cuatrimestres asignados")
  public void elCursoNoTieneCuatrimestreAsignados(String codigo) {
    var response =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CursoDTO curso = response.getResponseBody().blockFirst();
    assertNotNull(curso);
    assertTrue(
        curso.getCodigosCuatrimestres() == null || curso.getCodigosCuatrimestres().isEmpty(),
        "El curso debería no tener cuatrimestres asignados");
  }

  @Entonces("no se actualiza la información del curso exitosamente")
  public void noSeActualizaLaInformacionDelCursoExitosamente() {
    assertTrue(
        result.getStatus() == HttpStatus.NOT_FOUND || result.getStatus() == HttpStatus.BAD_REQUEST,
        "Expected NOT_FOUND or BAD_REQUEST but got: " + result.getStatus());
  }
}
