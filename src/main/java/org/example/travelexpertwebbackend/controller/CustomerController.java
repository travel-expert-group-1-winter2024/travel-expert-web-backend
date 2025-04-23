package org.example.travelexpertwebbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.example.travelexpertwebbackend.service.CustomerService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private UserRepository userRepository;


    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, UserRepository userRepository) {
        this.customerService = customerService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<GenericApiResponse<Map<String, String>>> uploadCustomerPhoto(
            @PathVariable int id,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {
        try {
            String photoUrl = customerService.uploadCustomerPhoto(id, image, request);
            return ResponseEntity.ok()
                    .body(new GenericApiResponse<>(Map.of("imageURL", photoUrl)));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error uploading customer photo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericApiResponse<>(List.of(new ErrorInfo("Customer not found"))));
        } catch (Exception e) {
            Logger.error(e, "Error uploading customer photo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<GenericApiResponse<Map<String, String>>> downloadCustomerPhoto(@PathVariable int id, HttpServletRequest request) {
        try {
            String imageURL = customerService.getCustomerPhoto(id, request);
            if (imageURL == null) {
                return ResponseEntity.ok().body(new GenericApiResponse<>(null));
            }
            return ResponseEntity.ok()
                    .body(new GenericApiResponse<>(Map.of("imageURL", imageURL)));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Error retrieving customer photo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            Logger.error(e, "Error retrieving customer photo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable Integer id) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerDetails(id);
        return customerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomermobile(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO) {
        Optional<CustomerDTO> updatedCustomer = customerService.updateCustomerMobile(id, customerDTO);
        return updatedCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/register")
    public ResponseEntity<GenericApiResponse<CustomerDTO>> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            Optional<User> userExists = userRepository.findByUsername(customerDTO.getCustemail());

            if (userExists.isPresent()) {
                User existingUser = userExists.get();

                if ("CUSTOMER".equals(existingUser.getRole())) {
                    Logger.info("User Already Exists: " + customerDTO.getCustemail());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new GenericApiResponse<>(List.of(new ErrorInfo("User Already Exists"))));
                } else if ("AGENT".equals(existingUser.getRole())) {
                    Logger.info("User Already Exists as Agent: " + customerDTO.getCustemail());
                    CustomerDTO registeredAgentCustomer = customerService.registerCustomer(customerDTO, false);
                    customerDTO.setCustomerid(registeredAgentCustomer.getCustomerid());
                    Logger.info("Successfully registered agent - customer: " + customerDTO.getCustemail());
                    return ResponseEntity.ok(new GenericApiResponse<>(registeredAgentCustomer));
                }
            }

            Logger.debug("Registering customer: " + customerDTO.getCustemail());
            CustomerDTO registeredCustomer = customerService.registerCustomer(customerDTO, false);
            customerDTO.setCustomerid(registeredCustomer.getCustomerid());
            Logger.info("Successfully registered customer: " + customerDTO.getCustemail());

            if (registeredCustomer.getCustomerid() != null) {
                userService.saveUser(customerDTO);
            }
            return ResponseEntity.ok(new GenericApiResponse<>(registeredCustomer));

        } catch (Exception ex) {
            Logger.error(ex, "Error registering customer: " + customerDTO.getCustemail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }
//@PostMapping("/register")
//public ResponseEntity<GenericApiResponse<CustomerDTO>> registerCustomer(@RequestBody CustomerDTO customerDTO) {
//    try {
//        Logger.debug("Registering customer" + customerDTO.getCustemail());
//        CustomerDTO registeredCustomer = customerService.registerCustomer(customerDTO);
//        Logger.info("Successfully registered customer" + registeredCustomer.getCustemail());
//        return ResponseEntity.ok(new GenericApiResponse<>(registeredCustomer));
//    } catch (Exception e) {
//        Logger.error(e, "Error registering customer" + customerDTO.getCustemail());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred."))));
//    }
//}


    @PostMapping("/updatecustomer/{id}")
    public ResponseEntity<GenericApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Integer id,
            @RequestBody CustomerDTO customerDTO) {
        try {
            Optional<CustomerDTO> updatedCustomer = customerService.updateCustomer(id, customerDTO);

            if (updatedCustomer.isPresent()) {
                Logger.info("Successfully updated customer: " + customerDTO.getCustemail());
                userService.updateUser(customerDTO);
                return ResponseEntity.ok(new GenericApiResponse<>(updatedCustomer.get()));
            } else {
                Logger.error("Error Updating customer: " + customerDTO.getCustemail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GenericApiResponse<>(List.of(new ErrorInfo("Customer not found"))));
            }
        } catch (Exception e) {
            Logger.error("Unexpected Error " + customerDTO.getCustemail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericApiResponse<String>> deleteCustomer(@PathVariable Integer id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(new GenericApiResponse<>("Customer deleted successfully"));
        } catch (IllegalArgumentException e) {
            Logger.error(e, "Customer not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("Customer not found"))));
        } catch (Exception e) {
            Logger.error(e, "Unexpected Error while deleting customer with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }
}
