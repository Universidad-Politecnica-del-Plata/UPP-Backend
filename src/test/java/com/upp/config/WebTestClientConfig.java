package com.upp.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class WebTestClientConfig {

  @Bean
  @Primary
  public WebTestClient webTestClient(@Autowired WebApplicationContext context) {
    return WebTestClient.bindToApplicationContext(context)
        .configureClient()
        .responseTimeout(Duration.ofSeconds(30))
        .build();
  }
}
