package org.example.travelexpertwebbackend.dto.auth;

public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
