package org.example.travelexpertwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class GenericApiResponse<T> {
    private T data;
    private List<ErrorInfo> error;

    public GenericApiResponse() {}

    public GenericApiResponse(T data) {
        this.data = data;
    }

    public GenericApiResponse(List<ErrorInfo> error) {
        this.data = null;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ErrorInfo> getError() {
        return error;
    }

    public void setError(List<ErrorInfo> error) {
        this.error = error;
    }
}