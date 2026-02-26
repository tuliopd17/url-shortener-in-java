package com.tulio.encurtadordeurl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShortenUrlResponse {

    private final String hash;
    private final String shortUrl;
    private final String originalUrl;
}

