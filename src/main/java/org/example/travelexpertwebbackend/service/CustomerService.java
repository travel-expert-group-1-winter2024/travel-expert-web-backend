package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.*;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;

    private static final String UPLOAD_DIR = "customer-uploads/";

    @Autowired
    private CustomerTierService customerTierService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CustomerTierRepository customerTierRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public CustomerService(CustomerRepository customerRepository, AgentRepository agentRepository) {
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
    }

    public String uploadCustomerPhoto(int customerId, MultipartFile image) throws IOException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        // Validate customer exists
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        // Validate file
        validateImageFile(image);

        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File is empty");
        }

        Customer customer = optionalCustomer.get();
        String filename = generateUniqueFilename(customerId, image.getOriginalFilename());

        Path dirPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(filename);
        Files.write(filePath, image.getBytes());

        customer.setPhotoPath(filename);
        customerRepository.save(customer);

        return filename;
    }

    private void validateImageFile(MultipartFile file) {
        if (file.getContentType() == null) {
            throw new IllegalArgumentException("File type cannot be determined");
        }

        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") &&
                !contentType.equals("image/png") &&
                !contentType.equals("image/jpg")) {
            throw new IllegalArgumentException("Only JPEG/JPG/PNG images are allowed");
        }
    }

    public byte[] getCustomerPhoto(int id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        String photoPath = customer.getPhotoPath();
        if (photoPath == null || photoPath.isEmpty()) {
            throw new IllegalArgumentException("Customer photo not found");
        }

        Path path = Paths.get(UPLOAD_DIR).resolve(photoPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading photo file", e);
        }
    }

    private String generateUniqueFilename(int customerId, String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "customer-" + customerId + "-" + System.currentTimeMillis() + extension;
    }

    // Get customer details by ID
    public Optional<CustomerDTO> getCustomerDetails(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(CustomerDTO::new);
    }

    // Update an existing customer
    public Optional<CustomerDTO> updateCustomerMobile(Integer id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {
            customer.setCustfirstname(customerDTO.getCustfirstname());
            customer.setCustlastname(customerDTO.getCustlastname());
            customer.setCustaddress(customerDTO.getCustaddress());
            customer.setCustcity(customerDTO.getCustcity());
            customer.setCustprov(customerDTO.getCustprov());
            customer.setCustpostal(customerDTO.getCustpostal());
            customer.setCustcountry(customerDTO.getCustcountry());
            customer.setCusthomephone(customerDTO.getCusthomephone());
            customer.setCustbusphone(customerDTO.getCustbusphone());
            customer.setCustemail(customerDTO.getCustemail());
            customerRepository.save(customer);
            return new CustomerDTO(customer);
        });
    }

    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        Agent agent = agentRepository.findById(customerDTO.getAgentId())
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
        customer.setCustfirstname(customerDTO.getCustfirstname());
        customer.setCustlastname(customerDTO.getCustlastname());
        customer.setCustaddress(customerDTO.getCustaddress());
        customer.setCustcity(customerDTO.getCustcity());
        customer.setCustprov(customerDTO.getCustprov());
        customer.setCustpostal(customerDTO.getCustpostal());
        customer.setCustcountry(customerDTO.getCustcountry());
        customer.setCusthomephone(customerDTO.getCusthomephone());
        customer.setCustbusphone(customerDTO.getCustbusphone());
        customer.setCustemail(customerDTO.getCustemail());
        customer.setPoints(0); // Initialize points to 0
        customer.setAgent(agent);

        // link with start tier
        CustomerTier starterTier = customerTierService.getStarterTier();
        customer.setCustomerTier(starterTier);
        customer = customerRepository.save(customer);

        // create wallet for customer
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCustomer(customer);
        wallet.setLastUpdated(Instant.now());
        Wallet savedWallet = walletRepository.save(wallet);

        // update wallet
        customer.setWallet(savedWallet);
        customer = customerRepository.save(customer);

        return new CustomerDTO(customer);
    }

    // Update an existing customer
    public Optional<CustomerDTO> updateCustomer(Integer id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {
            Agent agent = agentRepository.findById(customerDTO.getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
            customer.setCustfirstname(customerDTO.getCustfirstname());
            customer.setCustlastname(customerDTO.getCustlastname());
            customer.setCustaddress(customerDTO.getCustaddress());
            customer.setCustcity(customerDTO.getCustcity());
            customer.setCustprov(customerDTO.getCustprov());
            customer.setCustpostal(customerDTO.getCustpostal());
            customer.setCustcountry(customerDTO.getCustcountry());
            customer.setCusthomephone(customerDTO.getCusthomephone());
            customer.setCustbusphone(customerDTO.getCustbusphone());
            customer.setCustemail(customerDTO.getCustemail());
            customer.setAgent(agent);
            customerRepository.save(customer);
            return new CustomerDTO(customer);
        });
    }

    //Delete Customer
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Unlink wallet if needed
        Wallet wallet = customer.getWallet();
        if (wallet != null) {
            wallet.setCustomer(null);
        }

        // Unlink agent if needed
        Agent agent = customer.getAgent();
        if (agent != null) {
            agent.getCustomers().remove(customer);
        }

        // Unlink customer tier if needed
        CustomerTier customerTier = customer.getCustomerTier();
        if (customerTier != null) {
            customerTier.getCustomers().remove(customer);
        }

        // Unlink user if needed
        User user = customer.getUser();
        if (user != null) {
            user.setCustomer(null);
        }

        // Unlink bookings if needed
        Set<Booking> bookings = customer.getBookings();
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                booking.setCustomer(null);
            }
        }

        customerRepository.deleteById(id);
    }
}
