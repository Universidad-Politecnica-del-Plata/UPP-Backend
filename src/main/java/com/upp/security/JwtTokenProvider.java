package com.upp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt.secret}")
  private String secretKeyString;

  @Value("${app.jwt.expiration-ms}")
  private long validityInMilliseconds;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
  }

  public String generarToken(String username) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(expiry)
        .signWith(secretKey)
        .compact();
  }

  public String generarToken(String username, Collection<String> roles) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .subject(username)
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(expiry)
        .signWith(secretKey)
        .compact();
  }

  public String obtenerUsername(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public List<String> obtenerRoles(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    
    @SuppressWarnings("unchecked")
    List<String> roles = (List<String>) claims.get("roles");
    return roles != null ? roles : List.of();
  }

  public boolean validarToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return !claims.getPayload().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
