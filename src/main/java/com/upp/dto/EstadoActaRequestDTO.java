package com.upp.dto;

import com.upp.model.EstadoActa;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoActaRequestDTO {

  @NotNull(message = "El estado es obligatorio")
  private EstadoActa estado;
}