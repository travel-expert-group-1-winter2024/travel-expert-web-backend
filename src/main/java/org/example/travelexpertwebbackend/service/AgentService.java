package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.AgentDetailResponseDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateRequestDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateResponseDTO;
import org.example.travelexpertwebbackend.entity.Agency;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.repository.AgencyRepository;
import org.example.travelexpertwebbackend.repository.AgentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;
    private final AgencyRepository agencyRepository;

    public AgentService(AgentRepository agentRepository, AgencyRepository agencyRepository) {
        this.agentRepository = agentRepository;
        this.agencyRepository = agencyRepository;
    }

    public String uploadAgentPhoto(int agentId, MultipartFile image) throws IOException {
        // find agent by id
        Optional<Agent> optionalAgent = agentRepository.findById(agentId);
        if (optionalAgent.isEmpty()) {
            throw new IllegalArgumentException("Agent not found");
        }

        // check if image is empty
        if (image.getOriginalFilename() == null || image.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Agent agent = optionalAgent.get();
        String uploadDir = "uploads/";
        String filename = image.getOriginalFilename();

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

    public AgentUpdateResponseDTO updateAgent(int id, AgentUpdateRequestDTO request) {
        // find agent by id
        Optional<Agent> optionalAgent = agentRepository.findById(id);
        if (optionalAgent.isEmpty()) {
            throw new IllegalArgumentException("Agent not found");
        }

        Agent agent = optionalAgent.get();

        // check if agency id is provided
        if (request.getAgencyId() > 0) { // if not provide, agencyId will be 0
            // find agency by id
            Optional<Agency> optionalAgency = agencyRepository.findById(request.getAgencyId());
            if (optionalAgency.isEmpty()) {
                throw new IllegalArgumentException("Agency not found");
            }
            Agency agency = optionalAgency.get();
            agent.setAgency(agency);
        }

        // update agent details
        Logger.debug("Saving agent with ID: " + agent.getId());
        agent.setAgtFirstName(request.getAgtFirstName());
        agent.setAgtMiddleInitial(request.getAgtMiddleInitial());
        agent.setAgtLastName(request.getAgtLastName());
        agent.setAgtBusPhone(request.getAgtBusPhone());
        agent.setAgtEmail(request.getAgtEmail());
        agent.setAgtPosition(request.getAgtPosition());

        // update user table
        agent.getUser().setUsername(request.getAgtEmail());

        Agent savedAgent = agentRepository.save(agent);
        return new AgentUpdateResponseDTO(
                savedAgent.getId(),
                savedAgent.getAgtFirstName(),
                savedAgent.getAgtMiddleInitial(),
                savedAgent.getAgtLastName(),
                savedAgent.getAgtBusPhone(),
                savedAgent.getAgtEmail(),
                savedAgent.getAgtPosition(),
                savedAgent.getAgency().getId()
        );
    }
}
