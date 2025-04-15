package org.example.travelexpertwebbackend.service.auth;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.auth.GetUserByIdResponseDTO;
import org.example.travelexpertwebbackend.dto.auth.SignUpResponseDTO;
import org.example.travelexpertwebbackend.dto.auth.UserInfoDTO;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.auth.Role;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.AgentRepository;
import org.example.travelexpertwebbackend.repository.CustomerRepository;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.Instant;
import java.util.Optional;

import static org.example.travelexpertwebbackend.utils.RestUtil.buildPhotoUrl;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AgentRepository agentRepository;
    private final CustomerRepository customerRepository;

    public UserService(UserRepository userRepository, AgentRepository agentRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> myUser = userRepository.findByUsername(username);
        if (myUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = myUser.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRoles())
                .build();
    }

    public SignUpResponseDTO saveUser(CustomerDTO customerData) {
        // Create a new user
        User user = new User();
        user.setUsername(customerData.getCustemail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(customerData.getPassword()));
        user.setRole(Role.CUSTOMER.name());
        user.setCreatedAt(Instant.now());

        // Fetch Customer entity using CustomerId before setting it
        if (customerData.getCustomerid() != null) {
            Optional<Customer> customerOpt = customerRepository.findById(customerData.getCustomerid());
            if (customerOpt.isPresent()) {
                user.setCustomer(customerOpt.get());
            } else {
                throw new EntityNotFoundException("Customer not found with ID: " + customerData.getCustomerid());
            }
        }

        // Save user
        User savedUser = userRepository.save(user);

        return new SignUpResponseDTO(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    public SignUpResponseDTO saveAgent(String username, String password, Integer agentId) {
        // find Agent by agentId
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new EntityNotFoundException("Agent not found with id: " + agentId));

        User user = createAgentUser(username, password, agent);

        User savedUser = userRepository.save(user);
        return new SignUpResponseDTO(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    public User createAgentUser(String username, String password, Agent agent) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setAgent(agent);
        user.setRole(Role.AGENT.name()); // default role
        return user;
    }

    public SignUpResponseDTO updateUser(CustomerDTO customerData) {
        // Find the existing user by email
        Optional<User> existingUserOpt = userRepository.findByUsername(customerData.getCustemail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Update user details
            existingUser.setPasswordHash(new BCryptPasswordEncoder().encode(customerData.getPassword()));
            existingUser.setRole(Role.CUSTOMER.name());

            // Save updated user
            User updatedUser = userRepository.save(existingUser);
            Logger.info("Successfully updated user: " + customerData.getCustemail());

            return new SignUpResponseDTO(
                    updatedUser.getId().toString(),
                    updatedUser.getUsername(),
                    updatedUser.getRole()
            );
        } else {
            throw new EntityNotFoundException("User not found with email: " + customerData.getCustemail());
        }
    }

    public void deleteUserByEmail(String email) {
        Optional<User> user = userRepository.findByUsername(email);
        user.ifPresent(userRepository::delete);
    }

    public Customer getCustomerByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getCustomer() == null) {
            throw new IllegalStateException("User is not registered as a customer");
        }

        return user.getCustomer();
    }

    public UserInfoDTO getUserInfo(String username, HttpServletRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String[] roles = user.getRoles();
        if (roles == null || roles.length == 0) {
            throw new IllegalStateException("User has no roles assigned");
        }

        // Check if the user is a customer or agent
        if (user.getCustomer() == null && user.getAgent() == null) {
            throw new IllegalStateException("User is not registered as a customer or agent");
        }

        String fullName = null;
        Integer customerId = null;
        Integer agentId = null;
        String photoUrl = null;

        if (user.getCustomer() != null) {
            customerId = user.getCustomer().getId();
            fullName = getCustomerFullName(user);
            photoUrl = buildPhotoUrl(user.getCustomer().getPhotoPath(), request);
        } else if (user.getAgent() != null) {
            agentId = user.getAgent().getId();
            fullName = getAgentFullName(user);
            photoUrl = buildPhotoUrl(user.getAgent().getPhotoPath(), request);
        }

        return new UserInfoDTO(
                user.getId(),
                fullName,
                user.getUsername(),
                user.getRoles(),
                customerId,
                agentId,
                photoUrl
        );
    }

    private String getCustomerFullName(User user) {
        return user.getCustomer().getCustfirstname() + " " + user.getCustomer().getCustlastname();
    }

    private String getAgentFullName(User user) {
        return user.getAgent().getAgtFirstName() + " " + user.getAgent().getAgtLastName();
    }

    public GetUserByIdResponseDTO getUserIdByReference(Integer customerId, Integer agentId) {

        if (customerId == null && agentId == null) {
            throw new IllegalArgumentException("Either customerId or agentId must be provided");
        }

        if (customerId != null && agentId != null) {
            throw new IllegalArgumentException("Both customerId and agentId cannot be provided at the same time");
        }

        User user = null;
        if (customerId != null) {
            user = userRepository.findByCustomer_Id(customerId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        } else {
            user = userRepository.findByAgentId(agentId)
                    .orElseThrow(() -> new EntityNotFoundException("Agent not found with ID: " + agentId));
        }

        return new GetUserByIdResponseDTO(user.getId());
    }
}
