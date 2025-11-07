package com.upp.steps.shared;

import com.upp.repository.*;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LimpiadorDeBaseDeDatos {
  @Autowired private InscripcionRepository inscripcionRepository;
  @Autowired private CursoRepository cursoRepository;
  @Autowired private MateriaRepository materiaRepository;
  @Autowired private CuatrimestreRepository cuatrimestreRepository;
  @Autowired private ActaRepository actaRepository;
  @Autowired private JdbcTemplate jdbcTemplate;

  @Before
  public void limpiarBaseDeDatos() {
    jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

    inscripcionRepository.deleteAll();
    actaRepository.deleteAll();
    cursoRepository.deleteAll();
    materiaRepository.deleteAll();
    cuatrimestreRepository.deleteAll();
  }
}
