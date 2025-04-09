package org.example.travelexpertwebbackend.controller.auth;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.auth.*;
import org.example.travelexpertwebbackend.security.JwtService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

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
    // TODO: Commenting this as we have separate SIGN UP for customer and Agent
//    @PostMapping("/api/signup")
//    public ResponseEntity<SignUpResponseDTO> signup(@Valid @RequestBody SignUpRequestDTO request) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(userService.saveUser(request.getUsername(), request.getPassword()));
//    }

    // for agent registration
    @PostMapping("/api/signup/agent")
    public ResponseEntity<SignUpResponseDTO> signupAgent(@Valid @RequestBody SignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveAgent(request.getUsername(), request.getPassword(), request.getAgentId()));
    }

    @PostMapping("/api/login")
    public ResponseEntity<AuthResponse<LoginUserDTO>> loginUser(@Valid @RequestBody LoginRequestDTO user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                // Get user details and generate JWT
                UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
                String jwt = jwtService.generateToken(userDetails);

                // Get user info
                LoginUserDTO userInfo = userService.getLoginUserInfo(user.getUsername());

                return ResponseEntity.ok(new AuthResponse<>(userInfo, jwt));
            }
        } catch (BadCredentialsException ex) {
            Logger.warn("Bad credentials for user: " + user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse<>(List.of(new ErrorInfo("Username or password is incorrect"))));
        }

        // other error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse<>(List.of(new ErrorInfo("Authentication failed"))));
    }
}
