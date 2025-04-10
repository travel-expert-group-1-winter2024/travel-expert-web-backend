package org.example.travelexpertwebbackend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDTO {
    private UUID id;
    private String name;
    private String email;
    private String[] role;
    private Integer customerId;
    private Integer agentId;

    public UserInfoDTO() {
    }

    public UserInfoDTO(UUID id, String name, String email, String[] role, Integer customerId, Integer agentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.customerId = customerId;
        this.agentId = agentId;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getAgentId() {
        return agentId;
    }
}
