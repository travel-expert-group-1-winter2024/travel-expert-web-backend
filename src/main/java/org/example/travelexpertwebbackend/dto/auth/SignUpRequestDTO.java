package org.example.travelexpertwebbackend.dto.auth;

import jakarta.validation.constraints.NotNull;

public class SignUpRequestDTO {
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Agent ID is required")
    private Integer agentId;

    public SignUpRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}
