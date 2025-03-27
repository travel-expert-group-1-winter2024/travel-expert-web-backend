package org.example.travelexpertwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class GenericApiResponse<T> {
    private T data;
    private ErrorInfo error;

    public GenericApiResponse() {}

    public GenericApiResponse(T data) {
        this.data = data;
    }

    public GenericApiResponse(ErrorInfo error) {
        this.data = null;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }
}