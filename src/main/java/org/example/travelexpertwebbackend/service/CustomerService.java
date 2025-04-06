package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.CustomerDetailResponseDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
    public Optional<CustomerDTO> updateCustomer(Integer id, CustomerDTO customerDTO) {
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

    public CustomerDetailResponseDTO getCurrentCustomer(String username) {

        // find customer by email
        Optional<Customer> customer = customerRepository.findByCustemail(username);
        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer foundCustomer = customer.get();
        return new CustomerDetailResponseDTO(
                foundCustomer.getId(),
                foundCustomer.getCustfirstname(),
                foundCustomer.getCustlastname(),
                foundCustomer.getCustaddress(),
                foundCustomer.getCustcity(),
                foundCustomer.getCustprov(),
                foundCustomer.getCustpostal(),
                foundCustomer.getCustcountry(),
                foundCustomer.getCusthomephone(),
                foundCustomer.getCustbusphone(),
                foundCustomer.getCustemail(),
                foundCustomer.getAgent().getId()
        );
    }
}

