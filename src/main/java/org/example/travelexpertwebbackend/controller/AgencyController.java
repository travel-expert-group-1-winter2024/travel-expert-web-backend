package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.AgencyDTO;
import org.example.travelexpertwebbackend.entity.Agency;
import org.example.travelexpertwebbackend.repository.AgencyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agencies")
@CrossOrigin(origins = "*") // Allow frontend calls
public class AgencyController {

    private final AgencyRepository agencyRepository;

    public AgencyController(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @GetMapping
    public List<AgencyDTO> getAllAgencies() {
        return agencyRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AgencyDTO toDTO(Agency agency) {
        AgencyDTO dto = new AgencyDTO();
        dto.setId(agency.getId());
        dto.setAgencyAddress(agency.getAgencyAddress());
        dto.setAgencyCity(agency.getAgencyCity());
        dto.setAgencyProvince(agency.getAgencyProvince());
        dto.setAgencyPostal(agency.getAgencyPostal());
        dto.setAgencyCountry(agency.getAgencyCountry());
        dto.setAgencyPhone(agency.getAgencyPhone());
        dto.setAgencyFax(agency.getAgencyFax());
        return dto;
    }
}
