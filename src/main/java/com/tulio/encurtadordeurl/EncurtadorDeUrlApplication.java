package com.tulio.encurtadordeurl;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Encurtador de URLs de Alta Performance",
                version = "1.0.0",
                description = "API para encurtamento e redirecionamento de URLs usando PostgreSQL e Redis (Cache Aside)."
        )
)
public class EncurtadorDeUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncurtadorDeUrlApplication.class, args);
    }
}

