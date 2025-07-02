package com.upp.steps;

import com.upp.repository.RolRepository;
import com.upp.repository.UsuarioRepository;
import com.upp.steps.shared.TokenHolder;
import io.cucumber.java.ast.Cuando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class dar_de_alta_plan_de_estudios_steps {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private TokenHolder tokenHolder;

    @Cuando(
            "se registra un nuevo plan de estudios en el sistema con codigo {string}, fecha de entrada en vigencia {string}, fecha de vencimiento {string}, materias en el plan {string}, {string} y {string} y total de créditos optativos {int}")
    public void darDeAltaPlanDeEstudios(){

    }
}
