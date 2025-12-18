package com.upp.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.upp.dto.CuatrimestreDTO;
import com.upp.dto.CursoDTO;
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
public class dar_de_baja_cuatrimestre_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<Map> result;
  private String codigoCuatrimestreOriginal;

  @Cuando("se da de baja el cuatrimestre con código {string}")
  public void seDaDeBajaElCuatrimestreConCodigo(String codigo) {
    this.codigoCuatrimestreOriginal = codigo;
    this.result =
        webTestClient
            .delete()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(Map.class);
  }

  @Entonces("no existe el cuatrimestre {string} en el registro")
  public void noExisteElCuatrimestreEnElRegistro(String codigo) {
    var resultGetCuatrimestre =
        webTestClient
            .get()
            .uri("/api/cuatrimestres/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(CuatrimestreDTO.class);
    assertEquals(HttpStatus.NOT_FOUND, resultGetCuatrimestre.getStatus());
  }

  @Entonces("los cursos {string} ya no tienen el cuatrimestre {string} asignado")
  public void losCursosYaNoTienenElCuatrimestreAsignado(String cursos, String codigoCuatrimestre) {
    List<String> codigosCursos =
        Arrays.stream(cursos.split(",")).map(String::trim).collect(Collectors.toList());

    for (String codigoCurso : codigosCursos) {
      var resultGetCurso =
          webTestClient
              .get()
              .uri("/api/cursos/{codigo}", codigoCurso)
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .exchange()
              .returnResult(CursoDTO.class);

      assertEquals(HttpStatus.OK, resultGetCurso.getStatus());

      CursoDTO curso = resultGetCurso.getResponseBody().blockFirst();
      if (curso.getCodigosCuatrimestres() != null) {
        assertFalse(
            curso.getCodigosCuatrimestres().contains(codigoCuatrimestre),
            "El curso "
                + codigoCurso
                + " no debería tener el cuatrimestre "
                + codigoCuatrimestre
                + " asignado");
      }
    }
  }

  @Entonces("no se elimina el cuatrimestre y se lanza error")
  public void noSeEliminaElCuatrimestreYSeLanzaError() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());

    // Verificar que el cuatrimestre original aún existe si había sido creado previamente
    if (codigoCuatrimestreOriginal != null
        && !codigoCuatrimestreOriginal.equals("CUATRIMESTRE-INEXISTENTE")) {
      var resultGetCuatrimestreOriginal =
          webTestClient
              .get()
              .uri("/api/cuatrimestres/{codigo}", codigoCuatrimestreOriginal)
              .header("Authorization", "Bearer " + tokenHolder.getToken())
              .exchange()
              .returnResult(CuatrimestreDTO.class);
      assertEquals(HttpStatus.OK, resultGetCuatrimestreOriginal.getStatus());
    }
  }
}
