package org.example.travelexpertwebbackend.controller;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.AgentDetailResponseDTO;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.agent.AgentCreationRequestDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentCreationResponseDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateRequestDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateResponseDTO;
import org.example.travelexpertwebbackend.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ResponseEntity<GenericApiResponse<AgentCreationResponseDTO>> createAgent(
            @Valid @RequestBody AgentCreationRequestDTO request) {
        try {
            AgentCreationResponseDTO createdAgent = agentService.createAgent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericApiResponse<>(createdAgent));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error creating agent");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericApiResponse<>(List.of(new ErrorInfo("Invalid input"))));
        } catch (Exception e) {
            Logger.error(e, "Error creating agent");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to create agent"))));
        }
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadAgentPhoto(
            @PathVariable int id,
            @RequestParam("image") MultipartFile image) {
        try {
            String savedFilename = agentService.uploadAgentPhoto(id, image);
            return ResponseEntity.ok("Image uploaded successfully: " + savedFilename);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<GenericApiResponse<Map<String, String>>> downloadAgentPhoto(@PathVariable int id) {
        try {
            String imageUrl = agentService.getAgentPhoto(id);
            if (imageUrl == null) {
                return ResponseEntity.ok().body(new GenericApiResponse<>(null));
            }
            return ResponseEntity.ok()
                    .body(new GenericApiResponse<>(Map.of("imageURL", imageUrl)));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error retrieving agent photo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            Logger.error(e, "Error retrieving agent photo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<GenericApiResponse<AgentDetailResponseDTO>> getCurrentAgent(Authentication authentication, String authorizationHeader) {
        try {
            Logger.debug("Authorization Header: " + authorizationHeader);
            String username = (String) authentication.getPrincipal();
            AgentDetailResponseDTO responseDTO = agentService.getCurrentAgent(username);
            return ResponseEntity.ok(new GenericApiResponse<>(responseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericApiResponse<>(List.of(new ErrorInfo("Agent not found"))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to retrieve agent"))));
        }
    }

    // update agent
    @PutMapping("/{id}")
    public ResponseEntity<GenericApiResponse<AgentUpdateResponseDTO>> updateAgent(
            @PathVariable int id,
            @RequestBody AgentUpdateRequestDTO request) {
        try {
            AgentUpdateResponseDTO updatedAgent = agentService.updateAgent(id, request);
            return ResponseEntity.ok(new GenericApiResponse<>(updatedAgent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericApiResponse<>(List.of(new ErrorInfo("Agent not found"))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to update agent"))));
        }
    }
}
