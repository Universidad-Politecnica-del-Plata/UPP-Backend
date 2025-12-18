package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.upp.dto.CuatrimestreDTO;
import com.upp.dto.CursoDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
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
public class modificar_datos_de_cuatrimestre_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<CuatrimestreDTO> result;

  @Cuando(
      "se modifica el cuatrimestre con código {string}, fecha de inicio de clases {string}, fecha de fin de clases {string}, fecha de inicio de inscripción {string}, fecha de fin de inscripción {string}, fecha de inicio de integradores {string} y fecha de fin de integradores {string}")
  public void seModificaElCuatrimestre(
      String codigo,
      String fechaInicioClases,
      String fechaFinClases,
      String fechaInicioInscripcion,
      String fechaFinInscripcion,
      String fechaInicioIntegradores,
      String fechaFinIntegradores) {

    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.parse(fechaInicioClases),
            LocalDate.parse(fechaFinClases),
            LocalDate.parse(fechaInicioInscripcion),
            LocalDate.parse(fechaFinInscripcion),
            LocalDate.parse(fechaInicioIntegradores),
            LocalDate.parse(fechaFinIntegradores));

    this.result =
        webTestClient
            .put()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuatrimestreEnviado)
            .exchange()
            .returnResult(CuatrimestreDTO.class);
  }

  @Entonces("se actualiza la información del cuatrimestre {string} exitosamente")
  public void seActualizaLaInformacionDelCuatrimestreExitosamente(String codigo) {
    assertEquals(HttpStatus.OK, result.getStatus());

    var resultGetCuatrimestre =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, resultGetCuatrimestre.getStatus());
  }

  @Entonces("el cuatrimestre {string} tiene fecha de inicio {string} y fecha de fin {string}")
  public void elCuatrimestreTieneFechaDeInicioYFin(
      String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaDeInicioClases());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaDeFinClases());
  }

  @Entonces("el cuatrimestre {string} tiene período de inscripción desde {string} hasta {string}")
  public void elCuatrimestreTienePeriodoDeInscripcion(
      String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaInicioPeriodoDeInscripcion());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaFinPeriodoDeInscripcion());
  }

  @Entonces("el cuatrimestre {string} tiene período de integradores desde {string} hasta {string}")
  public void elCuatrimestreTienePeriodoDeIntegradores(
      String codigo, String fechaInicio, String fechaFin) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);
    assertEquals(LocalDate.parse(fechaInicio), cuatrimestre.getFechaInicioPeriodoIntegradores());
    assertEquals(LocalDate.parse(fechaFin), cuatrimestre.getFechaFinPeriodoIntegradores());
  }

  @Entonces("no se actualiza la información del cuatrimestre exitosamente")
  public void noSeActualizaLaInformacionDelCuatrimestreExitosamente() {
    assertTrue(
        result.getStatus() == HttpStatus.NOT_FOUND || result.getStatus() == HttpStatus.BAD_REQUEST,
        "Expected NOT_FOUND or BAD_REQUEST but got: " + result.getStatus());
  }

  @Dado("que existe un curso con código {string} para la materia {string}")
  public void queExisteUnCursoConCodigoParaLaMateria(String codigoCurso, String codigoMateria) {
    CursoDTO cursoEnviado = new CursoDTO(codigoCurso, 30, codigoMateria, null);

    webTestClient
        .post()
        .uri("/api/cursos")
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(cursoEnviado)
        .exchange();
  }

  @Cuando(
      "se modifica el cuatrimestre con código {string}, fecha de inicio de clases {string}, fecha de fin de clases {string}, fecha de inicio de inscripción {string}, fecha de fin de inscripción {string}, fecha de inicio de integradores {string}, fecha de fin de integradores {string} y los cursos {string}")
  public void seModificaElCuatrimestreConCursos(
      String codigo,
      String fechaInicioClases,
      String fechaFinClases,
      String fechaInicioInscripcion,
      String fechaFinInscripcion,
      String fechaInicioIntegradores,
      String fechaFinIntegradores,
      String cursos) {

    List<String> codigosCursos =
        Arrays.stream(cursos.split(",")).map(String::trim).collect(Collectors.toList());

    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.parse(fechaInicioClases),
            LocalDate.parse(fechaFinClases),
            LocalDate.parse(fechaInicioInscripcion),
            LocalDate.parse(fechaFinInscripcion),
            LocalDate.parse(fechaInicioIntegradores),
            LocalDate.parse(fechaFinIntegradores),
            codigosCursos);

    this.result =
        webTestClient
            .put()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuatrimestreEnviado)
            .exchange()
            .returnResult(CuatrimestreDTO.class);
  }

  @Cuando(
      "se modifica el cuatrimestre con código {string}, fecha de inicio de clases {string}, fecha de fin de clases {string}, fecha de inicio de inscripción {string}, fecha de fin de inscripción {string}, fecha de inicio de integradores {string}, fecha de fin de integradores {string} y sin cursos")
  public void seModificaElCuatrimestreSinCursos(
      String codigo,
      String fechaInicioClases,
      String fechaFinClases,
      String fechaInicioInscripcion,
      String fechaFinInscripcion,
      String fechaInicioIntegradores,
      String fechaFinIntegradores) {

    CuatrimestreDTO cuatrimestreEnviado =
        new CuatrimestreDTO(
            codigo,
            LocalDate.parse(fechaInicioClases),
            LocalDate.parse(fechaFinClases),
            LocalDate.parse(fechaInicioInscripcion),
            LocalDate.parse(fechaFinInscripcion),
            LocalDate.parse(fechaInicioIntegradores),
            LocalDate.parse(fechaFinIntegradores),
            Arrays.asList()); // Lista vacía

    this.result =
        webTestClient
            .put()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuatrimestreEnviado)
            .exchange()
            .returnResult(CuatrimestreDTO.class);
  }

  @Dado("que el cuatrimestre {string} tiene asociados los cursos {string}")
  public void queElCuatrimestreTieneAsociadosLosCursos(String codigoCuatrimestre, String cursos) {
    List<String> codigosCursos =
        Arrays.stream(cursos.split(",")).map(String::trim).collect(Collectors.toList());

    // Primero obtener el cuatrimestre actual
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);

    // Crear un nuevo DTO con los cursos
    CuatrimestreDTO cuatrimestreConCursos =
        new CuatrimestreDTO(
            cuatrimestre.getCodigo(),
            cuatrimestre.getFechaDeInicioClases(),
            cuatrimestre.getFechaDeFinClases(),
            cuatrimestre.getFechaInicioPeriodoDeInscripcion(),
            cuatrimestre.getFechaFinPeriodoDeInscripcion(),
            cuatrimestre.getFechaInicioPeriodoIntegradores(),
            cuatrimestre.getFechaFinPeriodoIntegradores(),
            codigosCursos);

    // Actualizar el cuatrimestre con los cursos
    webTestClient
        .put()
        .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
        .header("Authorization", "Bearer " + tokenHolder.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(cuatrimestreConCursos)
        .exchange();
  }

  @Entonces("el cuatrimestre {string} tiene los cursos {string}")
  public void elCuatrimestreTieneLosCursos(String codigoCuatrimestre, String cursosEsperados) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);

    List<String> cursosEsperadosList =
        Arrays.stream(cursosEsperados.split(",")).map(String::trim).collect(Collectors.toList());

    assertNotNull(cuatrimestre.getCodigosCursos());
    assertTrue(
        cuatrimestre.getCodigosCursos().containsAll(cursosEsperadosList),
        "El cuatrimestre no contiene todos los cursos esperados. Esperados: "
            + cursosEsperadosList
            + ", Actuales: "
            + cuatrimestre.getCodigosCursos());
  }

  @Entonces("el cuatrimestre {string} no tiene el curso {string}")
  public void elCuatrimestreNoTieneElCurso(String codigoCuatrimestre, String codigoCurso) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);

    if (cuatrimestre.getCodigosCursos() != null) {
      assertTrue(
          !cuatrimestre.getCodigosCursos().contains(codigoCurso),
          "El cuatrimestre aún contiene el curso " + codigoCurso);
    }
  }

  @Entonces("el cuatrimestre {string} no tiene cursos asociados")
  public void elCuatrimestreNoTieneCursosAsociados(String codigoCuatrimestre) {
    var response =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    CuatrimestreDTO cuatrimestre = response.getResponseBody().blockFirst();
    assertNotNull(cuatrimestre);

    assertTrue(
        cuatrimestre.getCodigosCursos() == null || cuatrimestre.getCodigosCursos().isEmpty(),
        "El cuatrimestre aún tiene cursos asociados: " + cuatrimestre.getCodigosCursos());
  }
}
