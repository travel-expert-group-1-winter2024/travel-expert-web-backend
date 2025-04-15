package org.example.travelexpertwebbackend.dto.agent;

public class AgentCreationResponseDTO {
    // agent table
    private Integer agentId;
    private String agtFirstName;
    private String agtMiddleInitial;
    private String agtLastName;
    private String agtBusPhone;
    private String agtEmail;
    private String agtPosition;
    // agency table
    private String agencyName;
    // user table
    private String userId;
    private String username;
    private String role;
    // customer table
    private Integer customerId;
    private String custFirstName;
    private String custLastName;
    private String custBusPhone;
    private String custEmail;

    public AgentCreationResponseDTO() {
    }

    public AgentCreationResponseDTO(Integer agentId, String agtFirstName, String agtMiddleInitial, String agtLastName, String agtBusPhone, String agtEmail, String agtPosition, String agencyName, String userId, String username, String role, Integer customerId, String custFirstName, String custLastName, String custBusPhone, String custEmail) {
        this.agentId = agentId;
        this.agtFirstName = agtFirstName;
        this.agtMiddleInitial = agtMiddleInitial;
        this.agtLastName = agtLastName;
        this.agtBusPhone = agtBusPhone;
        this.agtEmail = agtEmail;
        this.agtPosition = agtPosition;
        this.agencyName = agencyName;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.customerId = customerId;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.custBusPhone = custBusPhone;
        this.custEmail = custEmail;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public String getAgtFirstName() {
        return agtFirstName;
    }

    public String getAgtMiddleInitial() {
        return agtMiddleInitial;
    }

    public String getAgtLastName() {
        return agtLastName;
    }

    public String getAgtBusPhone() {
        return agtBusPhone;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public String getAgtPosition() {
        return agtPosition;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public String getCustBusPhone() {
        return custBusPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }
}
