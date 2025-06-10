package com.upp.security;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class JwtUtils {

  private static final String TOKEN_PREFIX = "Bearer ";

  public static String extraerToken(String header) {
    if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
      return header.substring(TOKEN_PREFIX.length());
    }
    return null;
  }

  public static String extraerTokenDeHeaders(HttpHeaders headers) {
    return extraerToken(headers.getFirst(HttpHeaders.AUTHORIZATION));
  }
}
