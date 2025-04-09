package org.example.travelexpertwebbackend.dto.auth;

import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;

import java.util.List;

public class AuthResponse<T> extends GenericApiResponse<T> {
    private String token;

    public AuthResponse(T data, String token) {
        super(data);
        this.token = token;
    }

    public AuthResponse(List<ErrorInfo> error) {
        super(error);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
