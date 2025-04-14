package org.example.travelexpertwebbackend.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
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
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<AuthResponse<UserInfoDTO>> loginUser(@Valid @RequestBody LoginRequestDTO user, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                // Get user details and generate JWT
                UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
                String jwt = jwtService.generateToken(userDetails);

                // Get user info
                UserInfoDTO userInfo = userService.getUserInfo(user.getUsername(), request);

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

    @GetMapping("/api/auth/me")
    public ResponseEntity<GenericApiResponse<UserInfoDTO>> getCurrentUser(Authentication authentication, HttpServletRequest request) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GenericApiResponse<>(List.of(new ErrorInfo("User not authenticated"))));
            }
            String username = (String) authentication.getPrincipal();
            UserInfoDTO userInfo = userService.getUserInfo(username, request);
            return ResponseEntity.ok(new GenericApiResponse<>(userInfo));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error retrieving user info");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("User not found"))));
        } catch (Exception e) {
            Logger.error(e, "Error retrieving user info");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to retrieve user info"))));
        }
    }

    @GetMapping("/api/users/by-reference")
    public ResponseEntity<GenericApiResponse<GetUserByIdResponseDTO>> getUserByReference(
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer agentId) {
        try {
            GetUserByIdResponseDTO user = userService.getUserIdByReference(customerId, agentId);
            return ResponseEntity.ok(new GenericApiResponse<>(user));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error retrieving user info");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("User not found"))));
        } catch (Exception e) {
            Logger.error(e, "Error retrieving user info");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to retrieve user info"))));
        }
    }
}
