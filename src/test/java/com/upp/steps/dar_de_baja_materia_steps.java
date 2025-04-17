package com.upp.steps;

import com.upp.dto.MateriaDTO;
import com.upp.model.TipoMateria;
import io.cucumber.java.an.Cuan;
import io.cucumber.java.ast.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_baja_materia_steps {
    @Autowired
    private WebTestClient webTestClient;
    private FluxExchangeResult<MateriaDTO> result;

    @Cuando("se da de baja la materia {string}")
    public void seDaDeBajaLaMateria(String codigo) {

        this.result = webTestClient.delete()
                .uri("/api/materias/{codigo}", codigo)
                .exchange()
                .returnResult(MateriaDTO.class);
    }

    @Entonces("no existe la materia {string} en el registro")
        public void noExisteLaMateriaEnElRegistro(String codigo) {
            var resultGetMateria = webTestClient.get()
                    .uri("/api/materias/{codigo}", codigo)
                    .exchange()
                    .returnResult(MateriaDTO.class);
            assertEquals(HttpStatus.NOT_FOUND, resultGetMateria.getStatus());
    }
}
