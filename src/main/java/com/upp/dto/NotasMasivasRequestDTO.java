package com.upp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class NotasMasivasRequestDTO {

  @NotEmpty(message = "La lista de notas no puede estar vacía")
  @Valid
  private List<NotaRequestDTO> notas;

  public NotasMasivasRequestDTO() {}

  public NotasMasivasRequestDTO(List<NotaRequestDTO> notas) {
    this.notas = notas;
  }

  public List<NotaRequestDTO> getNotas() {
    return notas;
  }

  public void setNotas(List<NotaRequestDTO> notas) {
    this.notas = notas;
  }
}
