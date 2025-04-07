package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.auth.Role;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.example.travelexpertwebbackend.repository.*;
import org.example.travelexpertwebbackend.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        //* The default state for each Customer being registered
        boolean isAgent = false;

        //* Will check if email exists in Users table, and change accordingly.
        boolean userAlreadyExists = false;

       //* Check email coming for a match in Users table
        Optional<User> existingUser = userRepository.findByUsername(customerDTO.getCustemail());

        if (existingUser.isPresent()) {
            if (customerDTO.getCustemail().toLowerCase().endsWith("@travelexperts.com")){
                isAgent = true;
            } else {
                //* Assuming the email exists already, and is not an Agent email.
                userAlreadyExists = true;
            }
        }

        //* Grabbing all Agents from Agents db.
        List<Agent> existingAgents = agentRepository.findAll();
        //* Handling potential errors, such as error grabbing all Agents
        if(existingAgents.isEmpty()){
            throw new IllegalStateException("No available agents to assign");
        }
        //* Assigning a random numbered agent based on the length of the list
        //* This will handle new agents being added without having to adjust the logic
        Random random = new Random();
        Agent assignedAgent = existingAgents.get(random.nextInt(existingAgents.size()));

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

        //* Create user credentials
        User user = new User();
        if (!userAlreadyExists) {
            user.setUsername(customerDTO.getCustemail());
            user.setPasswordHash(customerDTO.getPassword());
            if (isAgent){
                user.setRole(Role.AGENT.name());
                Agent registeringAgent = agentRepository.findByAgtEmail(customerDTO.getCustemail()).orElseThrow(() -> new IllegalStateException("Agent not found"));
                user.setAgent(registeringAgent);
            } else {
                user.setRole(Role.CUSTOMER.name());
                Customer registeringCustomer = customerRepository.findByCustemail(customerDTO.getCustemail()).orElseThrow(() -> new IllegalStateException("Customer not found"));
                user.setCustomer(registeringCustomer);
            }
        }


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