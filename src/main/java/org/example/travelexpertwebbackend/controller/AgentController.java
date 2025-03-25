package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
