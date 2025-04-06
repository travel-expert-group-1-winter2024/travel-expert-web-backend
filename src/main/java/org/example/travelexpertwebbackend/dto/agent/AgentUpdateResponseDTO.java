package org.example.travelexpertwebbackend.dto.agent;

public class AgentUpdateResponseDTO {
    private int id;
    private String agtFirstName;
    private String agtMiddleInitial;
    private String agtLastName;
    private String agtBusPhone;
    private String agtEmail;
    private String agtPosition;
    private int agencyId;

    public AgentUpdateResponseDTO() {
    }

    public AgentUpdateResponseDTO(int id, String agtFirstName, String agtMiddleInitial, String agtLastName, String agtBusPhone, String agtEmail, String agtPosition, int agencyId) {
        this.id = id;
        this.agtFirstName = agtFirstName;
        this.agtMiddleInitial = agtMiddleInitial;
        this.agtLastName = agtLastName;
        this.agtBusPhone = agtBusPhone;
        this.agtEmail = agtEmail;
        this.agtPosition = agtPosition;
        this.agencyId = agencyId;
    }

    public int getId() {
        return id;
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

    public int getAgencyId() {
        return agencyId;
    }
}
