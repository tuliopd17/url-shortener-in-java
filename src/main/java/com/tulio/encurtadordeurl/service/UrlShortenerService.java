package com.tulio.encurtadordeurl.service;

import com.tulio.encurtadordeurl.domain.ShortUrl;
import com.tulio.encurtadordeurl.dto.ShortenUrlResponse;
import com.tulio.encurtadordeurl.exception.UrlNotFoundException;
import com.tulio.encurtadordeurl.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerService {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int HASH_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ShortUrlRepository repository;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.shortener.base-url:http://localhost:8080}")
    private String baseUrl;

    public ShortenUrlResponse shorten(String originalUrl) {
        String hash = generateUniqueHash();

        ShortUrl entity = ShortUrl.builder()
                .hash(hash)
                .originalUrl(originalUrl)
                .createdAt(OffsetDateTime.now())
                .build();

        repository.save(entity);

        // Escreve no cache imediatamente
        redisTemplate.opsForValue().set(hash, originalUrl);

        String shortUrl = baseUrl.endsWith("/") ? baseUrl + hash : baseUrl + "/" + hash;

        return ShortenUrlResponse.builder()
                .hash(hash)
                .shortUrl(shortUrl)
                .originalUrl(originalUrl)
                .build();
    }

    public String resolveOriginalUrl(String hash) {
        log.info("Log: Buscando no Cache... hash={}", hash);
        String cachedUrl = redisTemplate.opsForValue().get(hash);

        if (cachedUrl != null) {
            return cachedUrl;
        }

        log.info("Log: Cache Miss - Buscando no Banco... hash={}", hash);
        Optional<ShortUrl> entityOpt = repository.findByHash(hash);

        ShortUrl entity = entityOpt.orElseThrow(() -> new UrlNotFoundException(hash));

        // Escreve no cache (Cache Aside)
        redisTemplate.opsForValue().set(hash, entity.getOriginalUrl());

        return entity.getOriginalUrl();
    }

    private String generateUniqueHash() {
        String hash;
        do {
            hash = generateRandomBase62(HASH_LENGTH);
        } while (repository.findByHash(hash).isPresent());
        return hash;
    }

    private String generateRandomBase62(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(BASE62_CHARS.length());
            sb.append(BASE62_CHARS.charAt(index));
        }
        return sb.toString();
    }
}

