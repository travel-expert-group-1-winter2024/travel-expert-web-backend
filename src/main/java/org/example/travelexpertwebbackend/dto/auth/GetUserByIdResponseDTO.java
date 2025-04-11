package org.example.travelexpertwebbackend.dto.auth;

import java.util.UUID;

public class GetUserByIdResponseDTO {
    private UUID userId;

    public GetUserByIdResponseDTO() {
    }

    public GetUserByIdResponseDTO(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
