package org.example.travelexpertwebbackend.dto.auth;

public class SignUpResponseDTO {
    private String id;
    private String username;
    private String role;

    public SignUpResponseDTO(String id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
