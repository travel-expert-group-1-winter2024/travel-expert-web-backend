package org.example.travelexpertwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class GenericApiResponse<T> {
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorInfo> errors; // optional

    public GenericApiResponse() {
    }

    public GenericApiResponse(T data) {
        this.data = data;
    }

    public GenericApiResponse(List<ErrorInfo> error) {
        this.data = null;
        this.errors = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public void addError(ErrorInfo error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}