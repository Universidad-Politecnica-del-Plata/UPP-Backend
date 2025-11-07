package com.upp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Acta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long numeroCorrelativo;

  @Enumerated(EnumType.STRING)
  @NotNull
  private TipoDeActa tipoDeActa;

  @NotNull private LocalDateTime fechaDeCreacion;

  @Enumerated(EnumType.STRING)
  @NotNull
  private EstadoActa estado;

  @OneToMany(mappedBy = "acta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Nota> notas = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "curso_codigo")
  @NotNull
  private Curso curso;

  public Acta(TipoDeActa tipoDeActa, EstadoActa estado, Curso curso) {
    this.tipoDeActa = tipoDeActa;
    this.fechaDeCreacion = LocalDateTime.now();
    this.estado = estado;
    this.curso = curso;
    this.notas = new ArrayList<>();
  }

  public Acta() {}
}
