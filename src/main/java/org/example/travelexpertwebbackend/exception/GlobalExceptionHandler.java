package org.example.travelexpertwebbackend.exception;

import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorInfo> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorInfo(error.getField(), error.getDefaultMessage()))
            .toList();

        GenericApiResponse<?> response = new GenericApiResponse<>(errors);
        return ResponseEntity.badRequest().body(response);
    }
}
