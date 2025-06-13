package com.upp.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AlumnoTest {
  @Test
  void crearSinParametros() {

    new Alumno();
  }

  @Test
  void crearConParametros() {
    String username = "juan";
    String password = "123456";
    String nombre = "Juan";
    String apellido = "Perez";
    Long dni = 123456789L;
    String email = "a@a.com";
    String direccion = "calle 123";
    LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
    LocalDate fechaIngreso = LocalDate.of(2020, 1, 1);
    List<String> telefonos = new ArrayList<>();
    telefonos.add("123456789");
    telefonos.add("987654321");

    Alumno alumno =
        new Alumno(
            username,
            password,
            dni,
            nombre,
            apellido,
            email,
            direccion,
            fechaNacimiento,
            fechaIngreso,
            telefonos);
    assertNotNull(alumno);
  }
}
