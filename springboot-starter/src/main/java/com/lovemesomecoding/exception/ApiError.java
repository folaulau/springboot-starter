package com.lovemesomecoding.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.dto.ApiDefaultResponseDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author folaukaveinga
 *
 */

@Setter
@Getter
@ToString
@JsonInclude(value = Include.NON_NULL)
public class ApiError extends ApiDefaultResponseDTO {

    public static final String DEFAULT_MSG = "Something went wrong. Please try again.";

    /**
     * Runtime exception message
     */
    private String             debugMessage;

    /**
     * Error messages
     */
    private List<ApiSubError>  errors;

    /**
     * In case, you want to customize error status.<br>
     * It's default to 400 or BAD_REQUEST
     */
    @JsonIgnore
    private HttpStatus         status;

    public ApiError() {
        this(null, null);
    }

    public ApiError(String message) {
        this(null, message);
    }

    public ApiError(HttpStatus status, String message) {
        this(status, message, Arrays.asList());
    }

    public ApiError(HttpStatus status, String message, String... subErrors) {
        this(status, message, null, Arrays.asList(subErrors));
    }

    public ApiError(HttpStatus status, String message, List<String> subErrors) {
        this(status, message, null, subErrors);
    }

    public ApiError(HttpStatus status, String message, String debugMessage, List<String> subErrors) {
        this(status, message, debugMessage, subErrors, null);
    }


    /**
     * Form validation errors
     */

    public ApiError(String message, List<ApiSubError> errors, Throwable ex) {
        super(message);
        this.debugMessage = (debugMessage != null) ? debugMessage : (ex != null) ? ex.getLocalizedMessage() : null;
        this.errors = errors;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ApiError(String message, Throwable ex, String... subErrors) {
        super(message);
        this.debugMessage = (debugMessage != null) ? debugMessage : (ex != null) ? ex.getLocalizedMessage() : null;
        this.status = HttpStatus.BAD_REQUEST;

        if (null != subErrors) {
            this.errors = Arrays.asList(subErrors).stream().map(subError -> new ApiSubError(subError)).collect(Collectors.toList());
        }
    }

    public ApiError(HttpStatus status, String message, String debugMessage, List<String> subErrors, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = (debugMessage != null) ? debugMessage : (ex != null) ? ex.getLocalizedMessage() : null;

        if (subErrors != null && subErrors.size() > 0) {
            this.errors = subErrors.stream().map(subError -> new ApiSubError(subError)).collect(Collectors.toList());
        }
    }

    public void addSubError(String subError) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ApiSubError(subError));
    }

}
