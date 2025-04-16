package org.example.travelexpertwebbackend.service;

import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.AgentDetailResponseDTO;
import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentCreationRequestDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentCreationResponseDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateRequestDTO;
import org.example.travelexpertwebbackend.dto.agent.AgentUpdateResponseDTO;
import org.example.travelexpertwebbackend.entity.Agency;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.auth.Role;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.AgencyRepository;
import org.example.travelexpertwebbackend.repository.AgentRepository;
import org.example.travelexpertwebbackend.repository.CustomerRepository;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.example.travelexpertwebbackend.utils.RoleUtil;
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
    private final AgencyRepository agencyRepository;
    private final UserService userService;
    private final CustomerService customerService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public AgentService(AgentRepository agentRepository, AgencyRepository agencyRepository, UserService userService, CustomerService customerService, UserRepository userRepository, CustomerRepository customerRepository) {
        this.agentRepository = agentRepository;
        this.agencyRepository = agencyRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    private static CustomerDTO getCustomerDTO(Agent savedAgent) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustfirstname(savedAgent.getAgtFirstName());
        customerDTO.setCustlastname(savedAgent.getAgtLastName());
        customerDTO.setCustbusphone(savedAgent.getAgtBusPhone());
        customerDTO.setCustemail(savedAgent.getAgtEmail());
        customerDTO.setAgentId(savedAgent.getId());

        // set empty address
        customerDTO.setCustaddress("");
        customerDTO.setCustcity("");
        customerDTO.setCustprov("");
        customerDTO.setCustpostal("");
        return customerDTO;
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
        Agent savedAgent = agentRepository.save(agent);

        // find agent record in customer table
        Customer customer = customerRepository.findByCustemail(savedAgent.getAgtEmail())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setPhotoPath(filename);
        customerRepository.save(customer);

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

    @Transactional
    public AgentCreationResponseDTO createAgent(AgentCreationRequestDTO request) {

        // find agency by id
        Agency agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IllegalArgumentException("Agency not found"));

        // create new agent
        Agent agent = new Agent();
        agent.setAgtFirstName(request.getAgtFirstName());
        agent.setAgtMiddleInitial(request.getAgtMiddleInitial());
        agent.setAgtLastName(request.getAgtLastName());
        agent.setAgtBusPhone(request.getAgtBusPhone());
        agent.setAgtEmail(request.getAgtEmail());
        agent.setAgtPosition("Agent"); // default position`
        agent.setAgency(agency);

        // save agent
        Agent savedAgent = agentRepository.save(agent);

        // create user
        User user = userService.createAgentUser(
                savedAgent.getAgtEmail(),
                request.getPassword(),
                savedAgent
        );
        user.setRole(RoleUtil.combineRoles(Role.AGENT.name(), Role.CUSTOMER.name())); // set role to agent and customer
        // save user
        User savedUser = userRepository.save(user);

        // create customer record
        CustomerDTO customerDTO = getCustomerDTO(savedAgent);

        CustomerDTO savedCustomerDTO = customerService.registerCustomer(customerDTO, true);

        // set customer to user table
        // TODO: this should be done in different way
        Customer customer = customerRepository.findById(savedCustomerDTO.getCustomerid())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        savedUser.setCustomer(customer);
        savedUser = userRepository.save(savedUser);

        return new AgentCreationResponseDTO(
                savedAgent.getId(),
                savedAgent.getAgtFirstName(),
                savedAgent.getAgtMiddleInitial(),
                savedAgent.getAgtLastName(),
                savedAgent.getAgtBusPhone(),
                savedAgent.getAgtEmail(),
                savedAgent.getAgtPosition(),
                savedAgent.getAgency().getAgencyCity(),
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getRole(),
                savedCustomerDTO.getCustomerid(),
                savedCustomerDTO.getCustfirstname(),
                savedCustomerDTO.getCustlastname(),
                savedCustomerDTO.getCustbusphone(),
                savedCustomerDTO.getCustemail()
        );
    }
}
