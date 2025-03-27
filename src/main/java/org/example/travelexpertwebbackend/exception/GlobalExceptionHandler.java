package org.example.travelexpertwebbackend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, Map<String, String>>>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, Map<String, String>>> errorList = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> detailMap = new HashMap<>();
            detailMap.put("field", error.getField());
            detailMap.put("detail", error.getDefaultMessage());

            Map<String, Map<String, String>> errorWrapper = new HashMap<>();
            errorWrapper.put("error", detailMap);

            errorList.add(errorWrapper);
        });

        Map<String, List<Map<String, Map<String, String>>>> response = new HashMap<>();
        response.put("errors", errorList);

        return ResponseEntity.badRequest().body(response);
    }
}
