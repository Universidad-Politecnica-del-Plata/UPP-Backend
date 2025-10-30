package com.upp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionRequestDTO {

    @NotBlank(message = "El código del curso es obligatorio")
    private String codigoCurso;

    @NotBlank(message = "El código del cuatrimestre es obligatorio")
    private String codigoCuatrimestre;

}