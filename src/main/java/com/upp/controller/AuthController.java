package com.upp.controller;

import com.upp.dto.UsuarioDTO;
import com.upp.security.JwtTokenProvider;
import com.upp.service.CustomUserDetailsService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailsService userDetailsService;

  public AuthController(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      CustomUserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
    try {
      String username = loginData.get("username");
      String password = loginData.get("password");

      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password));

      String token = jwtTokenProvider.generarToken(username);
      return ResponseEntity.ok(Map.of("token", token));

    } catch (AuthenticationException e) {
      return ResponseEntity.status(401).body("Credenciales inválidas");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UsuarioDTO dto) {
    try {
      userDetailsService.crearUsuario(dto);
      return ResponseEntity.status(201).body("Usuario creado exitosamente");
    } catch (RuntimeException e) {
      return ResponseEntity.status(400).body(e.getMessage());
    }
  }
}
