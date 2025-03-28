package org.example.travelexpertwebbackend.service.auth;

import jakarta.persistence.EntityNotFoundException;
import org.example.travelexpertwebbackend.dto.auth.SignUpResponseDTO;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.entity.auth.Role;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.AgentRepository;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AgentRepository agentRepository;


    public UserService(UserRepository userRepository, AgentRepository agentRepository) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
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

    public SignUpResponseDTO saveUser(String username, String password) {

        // create a new user
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setRole(Role.CUSTOMER.name()); // default role

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

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setAgentid(agent);
        user.setRole(Role.AGENT.name()); // default role

        User savedUser = userRepository.save(user);
        return new SignUpResponseDTO(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }
}
