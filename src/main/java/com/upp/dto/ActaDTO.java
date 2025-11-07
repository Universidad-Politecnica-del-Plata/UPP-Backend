package com.upp.dto;

import com.upp.model.EstadoActa;
import com.upp.model.TipoDeActa;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActaDTO {

  private Long numeroCorrelativo;

  private TipoDeActa tipoDeActa;

  private LocalDateTime fechaDeCreacion;

  private EstadoActa estado;

  private String codigoCurso;

  private List<NotaDTO> notas;
}