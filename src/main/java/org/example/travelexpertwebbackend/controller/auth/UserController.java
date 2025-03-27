package org.example.travelexpertwebbackend.controller.auth;

import org.example.travelexpertwebbackend.dto.auth.LoginRequestDTO;
import org.example.travelexpertwebbackend.dto.auth.SignUpResponseDTO;
import org.example.travelexpertwebbackend.dto.auth.SignUpRequestDTO;
import org.example.travelexpertwebbackend.service.auth.JwtService;
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

    @PostMapping("/api/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@Validated @RequestBody SignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequestDTO user) {
        System.out.println("Logging in user: " + user.getUsername());
        // use AuthenticationManager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        // check if authentication is successful
        if (authentication.isAuthenticated()) {
            // return user token that was created
            return ResponseEntity.ok(jwtService.generateToken(userService.loadUserByUsername(user.getUsername())));
        } else {
            System.out.println("Authentication failed for user: " + user.getUsername());
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
