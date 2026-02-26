package com.tulio.encurtadordeurl.repository;

import com.tulio.encurtadordeurl.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByHash(String hash);
}

