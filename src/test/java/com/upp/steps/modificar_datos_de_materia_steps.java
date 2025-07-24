package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.MateriaDTO;
import com.upp.model.TipoMateria;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class modificar_datos_de_materia_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;

  private FluxExchangeResult<MateriaDTO> result;

  @Cuando(
      "a la materia con código de materia {string} se modifica nombre {string}, contenidos {string}, tipo de materia {string}, con correlativa {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void modificarDatosDeMateria(
      String codigo,
      String nombre,
      String contenidos,
      String tipoMateria,
      String correlativas,
      Integer creditosOtorga,
      Integer creditosNecesarios) {
    List<String> listaDeCorrelativas =
        correlativas == null || correlativas.isBlank()
            ? List.of()
            : Arrays.stream(correlativas.split(",")).map(String::trim).toList();

    MateriaDTO materiaEnviada =
        new MateriaDTO(
            codigo,
            nombre,
            contenidos,
            creditosOtorga,
            creditosNecesarios,
            TipoMateria.valueOf(tipoMateria.toUpperCase()),
            listaDeCorrelativas);

    this.result =
        webTestClient
            .put()
            .uri("/api/materias/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(materiaEnviada)
            .exchange()
            .returnResult(MateriaDTO.class);
  }

  @Entonces("se actualiza la información de la materia exitosamente")
  public void seActualizaLaMateriaExitosamente() {
    assertEquals(HttpStatus.OK, result.getStatus());
  }

  @Entonces("se informa que no existe la materia")
  public void laMateriaNoExiste() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }

  @Entonces(
      "la materia {string} tiene nombre {string}, contenidos {string}, cantidad de créditos que otorga {int} y créditos necesarios {int}")
  public void laMateriaTieneLosDatosEsperados(
      String codigo, String nombre, String contenidos, int creditosOtorga, int creditosNecesarios) {
    var response =
        webTestClient
            .get()
            .uri("/api/materias/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(MateriaDTO.class);

    assertEquals(HttpStatus.OK, response.getStatus());

    MateriaDTO materia = response.getResponseBody().blockFirst();
    assertNotNull(materia);
    assertEquals(codigo, materia.getCodigoDeMateria());
    assertEquals(nombre, materia.getNombre());
    assertEquals(contenidos, materia.getContenidos());
    assertEquals(creditosOtorga, materia.getCreditosQueOtorga());
    assertEquals(creditosNecesarios, materia.getCreditosNecesarios());
  }
}
