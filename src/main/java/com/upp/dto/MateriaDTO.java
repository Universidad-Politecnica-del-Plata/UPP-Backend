package com.upp.dto;

import com.upp.model.TipoMateria;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MateriaDTO {
  private String codigoDeMateria;
  private String nombre;
  private String contenidos;
  private Integer creditosQueOtorga;
  private Integer creditosNecesarios;
  private TipoMateria tipo;
  private List<String> codigosCorrelativas;
}
