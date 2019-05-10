package com.lovemesomecoding.error;

public class DeniedCoverageException extends RuntimeException {

    private static final long serialVersionUID = 109439593614359558L;

    public DeniedCoverageException(String message) {
        super(message);
    }

}
