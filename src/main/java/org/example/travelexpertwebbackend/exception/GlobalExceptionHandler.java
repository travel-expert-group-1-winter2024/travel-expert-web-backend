package org.example.travelexpertwebbackend.exception;

import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .reduce((msg1, msg2) -> msg1 + "; " + msg2)
            .orElse("Validation failed");

        ErrorInfo errorInfo = new ErrorInfo(errorMessage);
        GenericApiResponse<?> response = new GenericApiResponse<>(errorInfo);
        return ResponseEntity.badRequest().body(response);
    }
}
