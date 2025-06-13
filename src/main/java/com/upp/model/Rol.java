package com.upp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rol {
  @Id private String nombre;

  public Rol(String nombre) {
        this.nombre = nombre;
}

  public Rol() {

  }
}
