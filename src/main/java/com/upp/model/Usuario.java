package com.upp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {
  @Id @GeneratedValue private Long id;
  private String username;
  private String password;
  private boolean habilitado = true;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Rol> roles = new HashSet<>();
}
