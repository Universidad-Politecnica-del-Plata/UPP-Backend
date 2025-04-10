package com.upp.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MateriaTest {
    @Test
    void crearSinParametros() {

        new Materia();
    }
    @Test
    void crearConParametros() {

        String codigoDeMateria = "123-M";
        String nombre = "Analisis II";
        String contenidos = "Funciones, Derivadas e Integrales";
        Integer creditosQueOtorga = 8;
        Integer creditosNecesarios = 4;
        TipoMateria tipo = TipoMateria.OBLIGATORIA;
        Materia materia =
                new Materia(codigoDeMateria,nombre,contenidos,creditosQueOtorga,creditosNecesarios,tipo);
        assertNotNull(materia);
    }
    @Test
    void agregarCorrelativaAMateria(){
        Materia analisisI = new Materia();
        analisisI.setCodigoDeMateria("123-M");
        analisisI.setNombre("Analisis I");

        Materia analisisII = new Materia();
        analisisII.setCodigoDeMateria("124-M");
        analisisII.setNombre("Analisis II");
        List<Materia> correlativas = new ArrayList<>();
        correlativas.add(analisisI);
        analisisII.setCorrelativas(correlativas);

        assertNotNull(analisisII.getCorrelativas());
        assertEquals("123-M", analisisII.getCorrelativas().get(0).getCodigoDeMateria());

    }

}
