package com.tulio.encurtadordeurl.controller;

import com.tulio.encurtadordeurl.dto.ShortenUrlRequest;
import com.tulio.encurtadordeurl.dto.ShortenUrlResponse;
import com.tulio.encurtadordeurl.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "URL Shortener", description = "Operações de encurtamento e redirecionamento de URLs")
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/shorten")
    @Operation(summary = "Encurtar uma URL", description = "Recebe uma URL original e retorna a versão encurtada.")
    public ResponseEntity<ShortenUrlResponse> shorten(@Valid @RequestBody ShortenUrlRequest request) {
        log.info("Recebida requisição para encurtar URL: {}", request.getUrl());
        ShortenUrlResponse response = service.shorten(request.getUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{hash}")
    @Operation(summary = "Redirecionar para a URL original", description = "Busca a URL original a partir do hash e redireciona (HTTP 302).")
    public ResponseEntity<Void> redirect(@PathVariable String hash) {
        String originalUrl = service.resolveOriginalUrl(hash);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

