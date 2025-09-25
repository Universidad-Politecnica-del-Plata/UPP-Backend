package com.upp.security;

import com.upp.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailsService userDetailsService;

  public JwtAuthenticationFilter(
      JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = JwtUtils.extraerToken(request.getHeader("Authorization"));

    if (token != null && jwtTokenProvider.validarToken(token)) {
      String username = jwtTokenProvider.obtenerUsername(token);
      List<String> roles = jwtTokenProvider.obtenerRoles(token);

      var authorities =
          roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

      UserDetails userDetails =
          User.builder().username(username).password("").authorities(authorities).build();

      var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
