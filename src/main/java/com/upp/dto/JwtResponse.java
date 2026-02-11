package com.upp.dto;

/** Se usa cuando la respuesta de autenticación es exitosa y contiene el token JWT. */
public record JwtResponse(String token) {}
