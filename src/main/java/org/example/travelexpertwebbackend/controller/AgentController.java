package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.AgentDetailResponseDTO;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
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

    @GetMapping("/me")
    public ResponseEntity<GenericApiResponse<AgentDetailResponseDTO>> getCurrentAgent(Authentication authentication, String authorizationHeader) {
        try {
            Logger.debug("Authorization Header: " + authorizationHeader);
            Logger.debug("Here is the authentication: " + authentication);
            String username = (String) authentication.getPrincipal();
            Logger.debug("Here is the username: " + username);
            AgentDetailResponseDTO responseDTO = agentService.getCurrentAgent(username);
            return ResponseEntity.ok(new GenericApiResponse<>(responseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericApiResponse<>(List.of(new ErrorInfo("Agent not found"))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to retrieve agent"))));
        }
    }
}
