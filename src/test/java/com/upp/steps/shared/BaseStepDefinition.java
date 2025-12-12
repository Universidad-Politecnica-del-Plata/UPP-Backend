package com.upp.steps.shared;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseStepDefinition {

  @Autowired private WebTestClient webTestClient;

  protected WebTestClient getWebTestClient() {
    return webTestClient.mutate().responseTimeout(Duration.ofSeconds(30)).build();
  }
}
