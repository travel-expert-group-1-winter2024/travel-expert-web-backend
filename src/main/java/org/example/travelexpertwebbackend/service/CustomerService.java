package org.example.travelexpertwebbackend.service;

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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
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

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomerService(CustomerRepository customerRepository, AgentRepository agentRepository) {
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
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
        //* Method to check the email is an agent email.
        boolean isAgent = isAgentEmail(customerDTO.getCustemail());
        //* Checking to see if the email exists, if it does, does it belong to an agent?
        validateEmailExistence(customerDTO);
        //* Assigns a random agent to each new customer
        Agent assignedAgent = assignRandomAgent();
        //* Creates and save the new Customer Object
        Customer customer = createAndSaveCustomer(customerDTO, assignedAgent, isAgent);
        // create wallet for customer
        Wallet wallet = createAndAssignWallet(customer);
        //* If the new Customer is not an Agent, creates a new User Account.
        if (!isAgent) {
            createUserAccount(customerDTO, customer);
        }
        return new CustomerDTO(customer);
    }

    /**
     * This method creates a new user account based on the data being recieved in the CustomerDTO
     * @param customerDTO The data transfer object containing the user data, email & password
     * @param customer The customer object, of which the new user will be associated with.
     */
    private void createUserAccount(CustomerDTO customerDTO, Customer customer) {
        User user = new User();
        user.setUsername(customerDTO.getCustemail());
        user.setPasswordHash(passwordEncoder.encode(customerDTO.getPassword()));
        user.setRole(Role.CUSTOMER.name());
        Customer registeringCustomer = customerRepository.findByCustemail(customerDTO.getCustemail()).orElseThrow(() -> new IllegalStateException("Customer not found"));
        user.setCustomer(registeringCustomer);
        userRepository.save(user);
    }

    /**
     * This method is responsible for creating a wallet and assigning it to the customer
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
     * @param customerDTO The data transfer object containing all the data in regards to our user, the customer.
     * @param assignedAgent A randomly selected agent, from the roster of agents is assigned to this customer as their liaison.
     * @param isAgent A boolean value assigned at creation to determine if the new customer record, belongs to an already existing agent
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
     * @return The selected agent, to be assigned.
     */
    private Agent assignRandomAgent() {
        //* Grabbing all Agents from Agents db.
        List<Agent> existingAgents = agentRepository.findAll();
        //* Handling potential errors, such as error grabbing all Agents
        if(existingAgents.isEmpty()){
            throw new IllegalStateException("No available agents to assign");
        }
        //* Assigning a random numbered agent based on the length of the list
        //* This will handle new agents being added without having to adjust the logic
        Random random = new Random();
        return existingAgents.get(random.nextInt(existingAgents.size()));
    }

    /**
     * This method acts as the first check point before proceeding to create the customer and optional user objects
     * @param customerDTO The data being sent from the client, pertaining to the customer, in this case specifically the email address
     */
    private void validateEmailExistence(CustomerDTO customerDTO){
        Optional<User> existingUser = userRepository.findByUsername(customerDTO.getCustemail());
        if (existingUser.isPresent() && !isAgentEmail(customerDTO.getCustemail())) {
            throw new IllegalStateException("Email already exists in our records.");
        }
    }

    /**
     * This method checks to see if the email being entered belongs to an Agent
     * @param email The email being checked
     * @return True if it is a registered Agent email, false if otherwise.
     */
    private boolean isAgentEmail(String email) {
        return userRepository.findByUsername(email)
                .map(user -> Role.AGENT.name().equalsIgnoreCase(user.getRole()))
                .orElse(false);

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