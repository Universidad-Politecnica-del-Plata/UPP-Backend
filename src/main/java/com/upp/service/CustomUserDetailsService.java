package com.upp.service;

import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository repo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario u =
        repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    return User.builder()
        .username(u.getUsername())
        .password(u.getPassword())
        .disabled(!u.isHabilitado())
        .authorities(u.getRoles().stream().map(Rol::getNombre).toArray(String[]::new))
        .build();
  }
}
