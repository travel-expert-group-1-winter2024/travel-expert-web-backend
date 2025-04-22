package org.example.travelexpertwebbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.auth.Role;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.*;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.travelexpertwebbackend.utils.RestUtil.buildPhotoUrl;

@Service
public class CustomerService {
    private static final String UPLOAD_DIR = "uploads/";
    @Autowired
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
    @Autowired
    private BlobStorageService blobStorageService;

    public CustomerService(CustomerRepository customerRepository, AgentRepository agentRepository) {
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
    }

    public String uploadCustomerPhoto(int customerId, MultipartFile image, HttpServletRequest request) throws IOException {
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

        return blobStorageService.uploadFile(image, filename);
    }

    private String uploadLocal(MultipartFile image, HttpServletRequest request, String filename, Customer customer) throws IOException {
        Path dirPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(filename);
        Files.write(filePath, image.getBytes());

        customer.setPhotoPath(filename);
        customerRepository.save(customer);

        return buildPhotoUrl(filename, request);
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

    public String getCustomerPhoto(int id, HttpServletRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        String photoPath = customer.getPhotoPath();
        if (photoPath == null || photoPath.isEmpty()) {
            throw new IllegalArgumentException("Customer photo not found");
        }

        return buildPhotoUrl(photoPath, request);
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

        Optional<CustomerDTO> optionalCustomerDTO = customerRepository.findById(id).map(customer -> {
            customer.setCustfirstname(customerDTO.getCustfirstname());
            customer.setCustlastname(customerDTO.getCustlastname());
            customer.setCustaddress(customerDTO.getCustaddress());
            customer.setCustcity(customerDTO.getCustcity());
            customer.setCustprov(customerDTO.getCustprov());
            customer.setCustpostal(customerDTO.getCustpostal());
            customer.setCustcountry(customerDTO.getCustcountry());
            customer.setCusthomephone(customerDTO.getCusthomephone());
            customer.setCustbusphone(customerDTO.getCustbusphone());
            customer.setCustemail(customerDTO.getCustemail()); // James: should we allow they change email?
            customerRepository.save(customer);
            return new CustomerDTO(customer);
        });

        // find agent record
        Optional<Agent> agent = agentRepository.findByAgtEmail(customerDTO.getCustemail());
        if (agent.isPresent()) {
            // update agent detail as well
            Agent agentRecord = agent.get();
            agentRecord.setAgtFirstName(customerDTO.getCustfirstname());
            agentRecord.setAgtLastName(customerDTO.getCustlastname());
            agentRecord.setAgtBusPhone(customerDTO.getCustbusphone());
            agentRecord.setAgtEmail(customerDTO.getCustemail()); // James: should we allow they change email?
            agentRepository.save(agentRecord);
        }

        return optionalCustomerDTO;
    }

    public CustomerDTO registerCustomer(CustomerDTO customerDTO, boolean isSkipRandomAgent) {
        //* Grabbing the email from the request
        String email = customerDTO.getCustemail();
        //* Checking if a user with this email already exists in our records
        Optional<User> existingUser = userRepository.findByUsername(email);

        //* If AGENT or MANAGER, return TRUE, otherwise return FALSE
        boolean isAgent = existingUser
                .map(user -> {
                    List<String> roles = Arrays.asList(user.getRoles());
                    return roles.contains(Role.AGENT.name()) || roles.contains(Role.MANAGER.name());
                })
                .orElse(false);
        if (isAgent) {
            Logger.info("Agent registering as a new Customer: " + email + ", " + customerDTO.getCustfirstname() + " " + customerDTO.getCustlastname());
        }

        //* If the email exists, and DOES NOT belong to an AGENT/MANAGER, throw an exception.
        if (existingUser.isPresent() && !isAgent) {
            Logger.info("Already registered Customer, attempting to register again" + email + ", " + customerDTO.getCustfirstname() + " " + customerDTO.getCustlastname());
            throw new IllegalStateException("User already exists in our records: " + email);
        }

        //* Assigns a random agent to each new customer
        Agent assignedAgent = null;
        if (!isSkipRandomAgent) {
            assignedAgent = assignRandomAgent();
        }
        //* Creates and save the new Customer Object
        Customer customer = createAndSaveCustomer(customerDTO, assignedAgent, isAgent);
        // create wallet for customer
        Wallet wallet = createAndAssignWallet(customer);

        return new CustomerDTO(customer);
    }

    /**
     * This method is responsible for creating a wallet and assigning it to the customer
     *
     * @param customer The customer object, of which will be associated with the wallet being created
     * @return The newly created wallet object assigned to the customer.
     */
    private Wallet createAndAssignWallet(Customer customer) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCustomer(customer);
        wallet.setLastUpdated(Instant.now());
        Wallet savedWallet = walletRepository.save(wallet);

        // update wallet
        customer.setWallet(savedWallet);
        customerRepository.save(customer);

        return savedWallet;
    }

    /**
     * The method responsible for first creating, and then saving the new Customer Object
     *
     * @param customerDTO   The data transfer object containing all the data in regards to our user, the customer.
     * @param assignedAgent A randomly selected agent, from the roster of agents is assigned to this customer as their liaison.
     * @param isAgent       A boolean value assigned at creation to determine if the new customer record, belongs to an already existing agent
     * @return The newly created Customer object, saved to the repository.
     */
    private Customer createAndSaveCustomer(CustomerDTO customerDTO, Agent assignedAgent, boolean isAgent) {
        Customer customer = new Customer();
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
        //* Assigning a randomly selected agent
        customer.setAgent(assignedAgent);
        //* Setting true/false based on username/email check.
        customer.setAgent(isAgent);
        // link with start tier
        CustomerTier starterTier = customerTierService.getStarterTier();
        customer.setCustomerTier(starterTier);
        return customerRepository.save(customer);
    }

    /**
     * This method picks a random agent from the full roster of agents, to be assigned to the created Customer Object.
     *
     * @return The selected agent, to be assigned.
     */
    private Agent assignRandomAgent() {
        //* Grabbing all Agents from Agents db.
        List<Agent> existingAgents = agentRepository.findAll();
        //* Handling potential errors, such as error grabbing all Agents
        if (existingAgents.isEmpty()) {
            throw new IllegalStateException("No available agents to assign");
        }
        //* Assigning a random numbered agent based on the length of the list
        //* This will handle new agents being added without having to adjust the logic
        Random random = new Random();
        return existingAgents.get(random.nextInt(existingAgents.size()));
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
    @Transactional
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Unlink wallet if needed
        Wallet wallet = customer.getWallet();
        if (wallet != null) {
            customer.setWallet(null);
            wallet.setCustomer(null);
            walletRepository.delete(wallet);
        }

        // Unlink agent if needed
        Agent agent = customer.getAgent();
        if (agent != null) {
            customer.setAgent(null);
            agent.getCustomers().remove(customer);
            agentRepository.save(agent);
        }

        // Unlink customer tier if needed
        CustomerTier customerTier = customer.getCustomerTier();
        if (customerTier != null) {
            customerTier.getCustomers().remove(customer);
            customerTierRepository.save(customerTier);
        }

        // Unlink user if needed
        User user = customer.getUser();
        if (user != null) {
            user.setCustomer(null);
            userRepository.save(user);
        }

        // Unlink bookings if needed
        Set<Booking> bookings = customer.getBookings();
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                booking.setCustomer(null);
                bookingRepository.save(booking);
            }
        }

        customerRepository.delete(customer);
    }
}