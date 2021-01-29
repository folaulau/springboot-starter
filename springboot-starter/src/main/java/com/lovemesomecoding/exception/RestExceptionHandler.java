package com.lovemesomecoding.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.TransientPropertyValueException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lovemesomecoding.utils.ObjMapperUtils;
import com.lovemesomecoding.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiError> handleGetException(ApiException ex) {
        log.info("handleApiException(..)");

        ApiError apiError = ex.getError();

        log.debug("Friendly Msg: {}", apiError.getMessage());
        String errors = StringUtils.join(apiError.getErrors(), ",");
        log.debug("API response detailed message: {}", errors);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if (null != apiError.getStatus()) {
            httpStatus = apiError.getStatus();
        }

        return new ResponseEntity<>(ex.getError(), httpStatus);
    }

    /**
     * Access Denied Exception
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        log.info("handleAccessDeniedException(..)");
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "You do not have access to this api.", null, Collections.singletonList(ex.getLocalizedMessage()));

        log.debug("Friendly Msg: {}", apiError.getMessage());
        String errors = StringUtils.join(apiError.getErrors(), ",");
        log.debug("API response detailed message: {}", errors);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if (null != apiError.getStatus()) {
            httpStatus = apiError.getStatus();
        }

        return new ResponseEntity<>(apiError, httpStatus);
    }

    /**
     * Fall back exception handler - if all fails, I WILL CATCH YOU!
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleOther(Exception ex) {
        log.error("handleOther(..)", ex);
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;

        StringBuilder errorString = new StringBuilder();
        try {
            errorString.append(ex.getClass().getName()).append(" ");
            StackTraceElement[] traceArray = ex.getStackTrace();
            if (null != traceArray && 0 < traceArray.length) {
                for (StackTraceElement stackTraceElement : traceArray) {
                    errorString.append(stackTraceElement.toString());
                    if (StringUtils.contains(stackTraceElement.getClassName(), "sidecarhealth")) {
                        break;
                    } else {
                        errorString.append(",");
                    }
                }
            }
        } catch (Exception e) {
            log.warn("ignore");
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(errorString.toString()));
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if (null != apiError.getStatus()) {
            httpStatus = apiError.getStatus();
        }

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(JsonMappingException.class)
    protected ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex) {
        log.info("handleJsonMappingException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));

        HttpStatus httpStatus = apiError.getStatus();

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ApiError> handleJsonProcessingException(JsonProcessingException ex) {
        log.info("handleJsonProcessingException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));

        HttpStatus httpStatus = apiError.getStatus();

        return new ResponseEntity<>(apiError, httpStatus);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(PersistenceException.class)
    protected ResponseEntity<ApiError> handleDatabaseException(PersistenceException ex) {
        log.info("handleDatabaseException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        HttpStatus httpStatus = apiError.getStatus();

        return new ResponseEntity<>(apiError, httpStatus);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(TransientPropertyValueException.class)
    protected ResponseEntity<ApiError> handleTransientPropertyValueException(TransientPropertyValueException ex) {
        log.info("handleTransientPropertyValueException(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(ex.getLocalizedMessage()));
        HttpStatus httpStatus = apiError.getStatus();

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleMissingServletRequestParameter(..)");
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleMissingServletRequestPart(..)");
        return super.handleMissingServletRequestPart(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleConversionNotSupported(..)");
        return super.handleConversionNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleServletRequestBindingException(..)");
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleBindException(..)");
        return super.handleBindException(ex, headers, status, request);

    }

    /**
     * Stripe Exception
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("handleIllegalArgumentException(..)");

        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, ex.getMessage());

        log.debug("Friendly Msg: {}", apiError.getMessage());
        String errors = StringUtils.join(apiError.getErrors(), ",");
        log.debug("API response detailed message: {}", errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleExceptionInternal(..)");
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable(..)", ex);
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Arrays.asList("Invalid request", "Invalid Payload", ex.getLocalizedMessage()));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MSG, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("handleMethodArgumentNotValid(..)");

        List<ApiSubError> errors = getFormErrors(ex.getBindingResult());
        log.debug("form errors: {}", errors);

        ApiError apiError = new ApiError("Something went wrong", errors, ex);

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleMissingPathVariable(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpRequestMethodNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleTypeMismatch(..)");
        log.error(ex.getMessage());
        String errorMessage = null != ex.getLocalizedMessage() ? ex.getLocalizedMessage() : ApiError.DEFAULT_MSG;
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Collections.singletonList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    private List<ApiSubError> getFormErrors(BindingResult bindingResult) {

        List<ApiSubError> errors = bindingResult.getFieldErrors().stream().map(fieldError -> {

            String field = fieldError.getField();

            String errorMsg = null;

            if (fieldError.getDefaultMessage() != null) {
                errorMsg = fieldError.getDefaultMessage();
            }

            ApiSubError error = new ApiSubError();
            error.setObject(fieldError.getObjectName());
            error.setField(field);
            error.setRejectedValue(fieldError.getRejectedValue());
            error.setMessage(errorMsg);

            return error;
        }).collect(Collectors.toList());
        return errors;
    }

}
