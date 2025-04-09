package org.example.travelexpertwebbackend.dto.auth;

import java.util.UUID;

public class UserInfoDTO {
    private UUID id;
    private String name;
    private String email;
    private String[] role;

    public UserInfoDTO() {
    }

    public UserInfoDTO(UUID id, String name, String email, String[] role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String[] getRole() {
        return role;
    }
}
