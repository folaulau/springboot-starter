package com.lovemesomecoding.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author folaukaveinga
 *
 */

@JsonInclude(value = Include.NON_NULL)
@Setter
@Getter
@ToString
public class ApiException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ApiError  error;

    public ApiException() {
    }

    public ApiException(ApiError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApiException(String message) {
        super(message);
        this.error = new ApiError(HttpStatus.BAD_REQUEST, message);
    }

    public ApiException(String message, List<String> subErrors) {
        super(message);
        this.error = new ApiError(HttpStatus.BAD_REQUEST, message, subErrors);
    }

    public ApiException(String message, String... subErrors) {
        super(message);
        this.error = new ApiError(HttpStatus.BAD_REQUEST, message, Arrays.asList(subErrors));
    }

    public ApiException(HttpStatus status, String message, List<String> subErrors) {
        super(message);
        this.error = new ApiError(status, message, subErrors);
    }

}
