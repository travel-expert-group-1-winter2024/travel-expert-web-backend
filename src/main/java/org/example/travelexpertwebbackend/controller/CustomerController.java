package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.service.CustomerService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable Integer id) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerDetails(id);
        return customerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO) {
        Optional<CustomerDTO> updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return updatedCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<GenericApiResponse<CustomerDTO>> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            Logger.debug("Registering customer: " + customerDTO.getCustemail());
            CustomerDTO registeredCustomer = customerService.registerCustomer(customerDTO);
            Logger.info("Successfully registered customer: " + customerDTO.getCustemail());
            if(registeredCustomer.getCustomerid() != null){
                userService.saveUser(customerDTO);
            }
            return ResponseEntity.ok(new GenericApiResponse<>(registeredCustomer));
        } catch (Exception ex) {
            Logger.error("Error registering customer: " + customerDTO.getCustemail(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }
}
