package com.lovemesomecoding.error;

import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.TransientPropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lovemesomecoding.utils.ValidationUtils;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(GetException.class)
    protected ResponseEntity<ApiErrorResponse> handleGetException(GetException ex) {
        log.info("handleGetException(..)");
        //log.debug(ObjectUtils.toJson(ex));
        return buildResponseEntity(ex.getError());
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(ProcessException.class)
    protected ResponseEntity<ApiErrorResponse> handleProcessException(ProcessException ex) {
        log.info("handleProcessException(..)");
        return buildResponseEntity(ex.getError());
    }

    /**
     * Fall back exception handler - if all fails, I WILL CATCH YOU!
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiErrorResponse> handleOther(Exception ex) {
		log.error("handleOther(..)", ex);
		String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage()
				: ApiErrorResponse.DEFAULT_MSG;
		
		StringBuilder errorString = new StringBuilder();
		try {
			errorString.append(ex.getClass().getName()).append(" ");
			StackTraceElement[] traceArray = ex.getStackTrace();
			if (traceArray != null && traceArray.length > 0) {
				for (int i = 0; i < traceArray.length; i++) {
					errorString.append(traceArray[i].toString());
					if (StringUtils.contains(traceArray[i].getClassName(), "sidecarhealth")) {
						break;
					} else {
						errorString.append(",");
					}
				}
			}
		} catch (Exception e) {
			// ignore
		}

		ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG,
				errorMessage, Arrays.asList(errorString.toString()));
		return buildResponseEntity(apiError);
	}

    @ExceptionHandler(JsonMappingException.class)
    protected ResponseEntity<ApiErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        log.info("handleJsonMappingException(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,  ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ApiErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.info("handleJsonProcessingException(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,  ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(PersistenceException.class)
    protected ResponseEntity<ApiErrorResponse> handleDatabaseException(PersistenceException ex) {
        log.info("handleDatabaseException(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,  ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(TransientPropertyValueException.class)
    protected ResponseEntity<ApiErrorResponse> handleTransientPropertyValueException(TransientPropertyValueException ex) {
        log.info("handleTransientPropertyValueException(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,  ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(ex.getLocalizedMessage()));
        return buildResponseEntity(apiError);
    }

    /**
     * Stripe Exception
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.info("handleIllegalArgumentException(..)");
		String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage()
				: ApiErrorResponse.DEFAULT_MSG;
		ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG,
				errorMessage, Arrays.asList(ex.getMessage()));
		return buildResponseEntity(apiError);
	}

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST,  ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotAcceptable(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleExceptionInternal(..)");
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable(..)",ex);
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList("Invalid request", "Invalid Payload", ex.getLocalizedMessage()));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMediaTypeNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ApiErrorResponse.DEFAULT_MSG, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("handleMethodArgumentNotValid(..)");

        List<String> errors = ValidationUtils.getErrors(ex.getBindingResult());
        log.debug("errors: {}", errors);
        StringBuffer bindingErrors = null;
        if (errors != null && !errors.isEmpty()) {
            bindingErrors = new StringBuffer();
            for (String error : errors) {
                bindingErrors.append(error + "\n");
            }
        }
        
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Something went wrong", errors);

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleMissingPathVariable(..)");
        log.error(ex.getMessage());
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpRequestMethodNotSupported(..)");
        log.error(ex.getMessage());
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleTypeMismatch(..)");
        log.error(ex.getMessage());
        String errorMessage = ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ApiErrorResponse.DEFAULT_MSG;
        ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, errorMessage, Arrays.asList(errorMessage));
        return new ResponseEntity<>(apiError, status);
    }

    /**
     * Get - error code and string of message Delete - error code and string of message Post, Put, Patch - error code
     * and list of messages
     * 
     * @param apiError
     * @return ResponseEntity
     */
	private ResponseEntity<ApiErrorResponse> buildResponseEntity(ApiErrorResponse apiErrorResponse) {
		log.error("Friendly Error Msg: {}", apiErrorResponse.getMessage());
		log.error("Errors: {}", apiErrorResponse.getErrors());
		return new ResponseEntity<>(apiErrorResponse,
				apiErrorResponse.getStatus() != null ? apiErrorResponse.getStatus() : HttpStatus.BAD_REQUEST);
	}

}
