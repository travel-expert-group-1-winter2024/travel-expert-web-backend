package org.example.travelexpertwebbackend.controller;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.wallet.WalletTopUpRequestDTO;
import org.example.travelexpertwebbackend.dto.wallet.WalletTopUpResponseDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.service.WalletService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @PostMapping("/wallet/topup")
    public ResponseEntity<GenericApiResponse<WalletTopUpResponseDTO>> topUpWallet(Authentication authentication, @Valid @RequestBody WalletTopUpRequestDTO requestDTO) {
        String username = "";
        try {
            username = (String) authentication.getPrincipal();
            Customer customer = userService.getCustomerByUsername(username);
            WalletTopUpResponseDTO result = walletService.topUpWallet(customer, requestDTO);
            return ResponseEntity.ok(new GenericApiResponse<>(result));
        } catch (IllegalArgumentException e) {
            Logger.error("Error topping up wallet for customer with username: " + username, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo(e.getMessage()))));
        } catch (Exception e) {
            Logger.error("Error topping up wallet for customer with username: " + username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }
}
