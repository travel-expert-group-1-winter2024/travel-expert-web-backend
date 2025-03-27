package org.example.travelexpertwebbackend.controller.auth;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.auth.LoginRequestDTO;
import org.example.travelexpertwebbackend.dto.auth.LoginResponseDTO;
import org.example.travelexpertwebbackend.dto.auth.SignUpResponseDTO;
import org.example.travelexpertwebbackend.dto.auth.SignUpRequestDTO;
import org.example.travelexpertwebbackend.security.JwtService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // for user registration
    @PostMapping("/api/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@Valid @RequestBody SignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(request.getUsername(), request.getPassword()));
    }

    // for agent registration
    @PostMapping("/api/signup/agent")
    public ResponseEntity<SignUpResponseDTO> signupAgent(@Valid @RequestBody SignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveAgent(request.getUsername(), request.getPassword(), request.getAgentId()));
    }

    @PostMapping("/api/login")
    public ResponseEntity<GenericApiResponse<LoginResponseDTO>> loginUser(@Valid @RequestBody LoginRequestDTO user) {
        // use AuthenticationManager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        // check if authentication is successful
        if (authentication.isAuthenticated()) {
            Logger.debug("Logging in user: " + user.getUsername());
            // return user token that was created
            return ResponseEntity.ok(new GenericApiResponse<>(new LoginResponseDTO(jwtService.generateToken(userService.loadUserByUsername(user.getUsername())))));
        } else {
            Logger.error("Authentication failed for user: " + user.getUsername());
            return ResponseEntity.status(401).body(new GenericApiResponse<>(new ErrorInfo("Authentication failed")));
        }
    }
}
