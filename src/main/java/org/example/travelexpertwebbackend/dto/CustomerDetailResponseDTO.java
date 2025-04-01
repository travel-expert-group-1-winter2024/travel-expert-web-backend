package org.example.travelexpertwebbackend.dto;

public class CustomerDetailResponseDTO {
    private int id;
    private String custFirstName;
    private String custLastName;
    private String custAddress;
    private String custCity;
    private String custProv;
    private String custPostal;
    private String custCountry;
    private String custHomePhone;
    private String custBusPhone;
    private String custEmail;
    private int agentId;

    public CustomerDetailResponseDTO() {
    }

    public CustomerDetailResponseDTO(int id, String custFirstName, String custLastName, String custAddress, String custCity, String custProv, String custPostal, String custCountry, String custHomePhone, String custBusPhone, String custEmail, int agentId) {
        this.id = id;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.custAddress = custAddress;
        this.custCity = custCity;
        this.custProv = custProv;
        this.custPostal = custPostal;
        this.custCountry = custCountry;
        this.custHomePhone = custHomePhone;
        this.custBusPhone = custBusPhone;
        this.custEmail = custEmail;
        this.agentId = agentId;
    }

    public int getId() {
        return id;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustCity() {
        return custCity;
    }

    public String getCustProv() {
        return custProv;
    }

    public String getCustPostal() {
        return custPostal;
    }

    public String getCustCountry() {
        return custCountry;
    }

    public String getCustHomePhone() {
        return custHomePhone;
    }

    public String getCustBusPhone() {
        return custBusPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public int getAgentId() {
        return agentId;
    }
}
