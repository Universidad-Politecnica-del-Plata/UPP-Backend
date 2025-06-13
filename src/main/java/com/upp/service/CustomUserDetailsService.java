package com.upp.service;

import com.upp.dto.UsuarioDTO;
import com.upp.model.Rol;
import com.upp.model.Usuario;
import com.upp.repository.UsuarioRepository;
import com.upp.repository.RolRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario u = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    return User.builder()
            .username(u.getUsername())
            .password(u.getPassword())
            .disabled(!u.isHabilitado())
            .authorities(u.getRoles().stream().map(Rol::getNombre).toArray(String[]::new))
            .build();
  }

  public void crearUsuario(UsuarioDTO dto) {
    if (usuarioRepository.existsByUsername(dto.username)) {
      throw new RuntimeException("El usuario ya existe");
    }

    Usuario usuario = new Usuario();
    usuario.setUsername(dto.username);
    usuario.setPassword(passwordEncoder.encode(dto.password));
    usuario.setHabilitado(true);

    var roles = dto.roles.stream()
            .map(nombre -> rolRepository.findById(nombre)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
            .collect(Collectors.toSet());

    usuario.setRoles(roles);

    usuarioRepository.save(usuario);
  }
}
