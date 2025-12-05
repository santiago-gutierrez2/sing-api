package com.sign.signApi.common.web;

import com.sign.signApi.common.dto.ErrorResponseDTO;
import com.sign.signApi.user.service.exceptions.InvalidCredentialsException;
import com.sign.signApi.user.service.exceptions.UsernameAlreadyInUseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ErrorResponseDTO buildErrorResponse(HttpStatus status,
                                                String message,
                                                WebRequest request) {
        return ErrorResponseDTO.builder()
                .status(status.value())
                .error(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleInvalidCredentials(
            InvalidCredentialsException ex,
            WebRequest request) {
        log.warn("Invalid login attempt: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleUsernameExists(
            UsernameAlreadyInUseException ex,
            WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleGeneralException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }
}
