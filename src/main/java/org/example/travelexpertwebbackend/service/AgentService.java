package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.AgentDetailResponseDTO;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.repository.AgentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public String uploadAgentPhoto(int agentId, MultipartFile image) throws IOException {
        Optional<Agent> optionalAgent = agentRepository.findById(agentId);
        if (optionalAgent.isEmpty()) {
            throw new IllegalArgumentException("Agent not found");
        }

        Agent agent = optionalAgent.get();
        String uploadDir = "uploads/";
        String filename = "agent_" + agentId + "_" + image.getOriginalFilename();

        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(filename);
        Files.write(filePath, image.getBytes());

        agent.setPhotoPath(filename);
        agentRepository.save(agent);

        return filename;
    }

    public AgentDetailResponseDTO getCurrentAgent(String username) {
        // find agent by username
        Optional<Agent> agent = agentRepository.findByAgtEmail(username);
        if (agent.isEmpty()) {
            throw new IllegalArgumentException("Agent not found");
        }

        Agent result = agent.get();

        return new AgentDetailResponseDTO(
                result.getId(),
                result.getAgtFirstName(),
                result.getAgtMiddleInitial(),
                result.getAgtLastName(),
                result.getAgtBusPhone(),
                result.getAgtEmail(),
                result.getAgtPosition(),
                result.getAgency().getId()
        );
    }

    public byte[] getAgentPhoto(int id) {
        // find agent by id
        Optional<Agent> optionalAgent = agentRepository.findById(id);
        if (optionalAgent.isEmpty()) {
            throw new IllegalArgumentException("Agent not found");
        }

        Agent agent = optionalAgent.get();
        String photoPath = agent.getPhotoPath();
        if (photoPath == null || photoPath.isEmpty()) {
            throw new IllegalArgumentException("Agent photo not found");
        }

        Path path = Paths.get("uploads/").resolve(photoPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading photo file", e);
        }
    }
}
