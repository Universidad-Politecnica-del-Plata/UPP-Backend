package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.upp.dto.CursoDTO;
import com.upp.dto.CuatrimestreDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_curso_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<Map> result;
  private String codigoCursoOriginal;

  @Cuando("se da de baja el curso con código {string}")
  public void seDaDeBajaElCursoConCodigo(String codigo) {
    this.codigoCursoOriginal = codigo;
    this.result =
        webTestClient
            .delete()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("no existe el curso {string} en el registro")
  public void noExisteElCursoEnElRegistro(String codigo) {
    var resultGetCurso =
        webTestClient
            .get()
            .uri("/api/cursos/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CursoDTO.class);
    assertEquals(HttpStatus.NOT_FOUND, resultGetCurso.getStatus());
  }

  @Entonces("los cuatrimestres {string} ya no tienen el curso {string} asignado")
  public void losCuatrimestreYaNoTienenElCursoAsignado(String cuatrimestres, String codigoCurso) {
    List<String> codigosCuatrimestres = Arrays.stream(cuatrimestres.split(","))
        .map(String::trim)
        .collect(Collectors.toList());

    for (String codigoCuatrimestre : codigosCuatrimestres) {
      var resultGetCuatrimestre =
          webTestClient
              .get()
              .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestre)
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .exchange()
              .returnResult(CuatrimestreDTO.class);

      assertEquals(HttpStatus.OK, resultGetCuatrimestre.getStatus());

      CuatrimestreDTO cuatrimestre = resultGetCuatrimestre.getResponseBody().blockFirst();
      if (cuatrimestre.getCodigosCursos() != null) {
        assertFalse(cuatrimestre.getCodigosCursos().contains(codigoCurso),
                   "El cuatrimestre " + codigoCuatrimestre + " no debería tener el curso " + codigoCurso + " asignado");
      }
    }
  }

  @Entonces("no se elimina el curso y se lanza error")
  public void noSeEliminaElCursoYSeLanzaError() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    
    // Verificar que el curso original aún existe si había sido creado previamente
    if (codigoCursoOriginal != null && !codigoCursoOriginal.equals("CURSO-INEXISTENTE")) {
      var resultGetCursoOriginal =
          webTestClient
              .get()
              .uri("/api/cursos/{codigo}", codigoCursoOriginal)
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .exchange()
              .returnResult(CursoDTO.class);
      assertEquals(HttpStatus.OK, resultGetCursoOriginal.getStatus());
    }
  }
}