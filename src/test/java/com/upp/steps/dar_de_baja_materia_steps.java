package com.upp.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.upp.dto.MateriaDTO;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_materia_steps {
  @Autowired private WebTestClient webTestClient;
  @Autowired private TokenHolder tokenHolder;
  private FluxExchangeResult<MateriaDTO> result;

  @Cuando("se da de baja la materia {string}")
  public void seDaDeBajaLaMateria(String codigo) {

    this.result =
        webTestClient
            .delete()
            .uri("/api/materias/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(MateriaDTO.class);
  }

  @Entonces("no existe la materia {string} en el registro")
  public void noExisteLaMateriaEnElRegistro(String codigo) {
    var resultGetMateria =
        webTestClient
            .get()
            .uri("/api/materias/{codigo}", codigo)
            .header("Authorization", "Bearer " + tokenHolder.getToken())
            .exchange()
            .returnResult(MateriaDTO.class);
    assertEquals(HttpStatus.NOT_FOUND, resultGetMateria.getStatus());
  }

  @Entonces("no se elimina la materia y se lanza error")
  public void noSeEliminaLaMateriaYSeLanzaError() {
    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
  }
}
