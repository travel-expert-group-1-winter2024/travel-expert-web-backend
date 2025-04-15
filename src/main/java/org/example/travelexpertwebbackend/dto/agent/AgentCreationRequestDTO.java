package org.example.travelexpertwebbackend.dto.agent;

import jakarta.validation.constraints.NotNull;

public class AgentCreationRequestDTO {
    @NotNull(message = "Agent first name is required")
    private String agtFirstName;
    @NotNull(message = "Agent middle initial is required")
    private String agtMiddleInitial;
    @NotNull(message = "Agent last name is required")
    private String agtLastName;
    @NotNull(message = "Agent business phone is required")
    private String agtBusPhone;
    @NotNull(message = "Agent email is required")
    private String agtEmail;
    @NotNull(message = "Agency ID is required")
    private Integer agencyId;
    @NotNull(message = "Password is required")
    private String password;

    public AgentCreationRequestDTO() {
    }

    public String getAgtFirstName() {
        return agtFirstName;
    }

    public void setAgtFirstName(String agtFirstName) {
        this.agtFirstName = agtFirstName;
    }

    public String getAgtMiddleInitial() {
        return agtMiddleInitial;
    }

    public void setAgtMiddleInitial(String agtMiddleInitial) {
        this.agtMiddleInitial = agtMiddleInitial;
    }

    public String getAgtLastName() {
        return agtLastName;
    }

    public void setAgtLastName(String agtLastName) {
        this.agtLastName = agtLastName;
    }

    public String getAgtBusPhone() {
        return agtBusPhone;
    }

    public void setAgtBusPhone(String agtBusPhone) {
        this.agtBusPhone = agtBusPhone;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public void setAgtEmail(String agtEmail) {
        this.agtEmail = agtEmail;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
