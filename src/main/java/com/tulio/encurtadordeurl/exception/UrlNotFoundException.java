package com.tulio.encurtadordeurl.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String hash) {
        super("URL encurtada não encontrada para o hash: " + hash);
    }
}

