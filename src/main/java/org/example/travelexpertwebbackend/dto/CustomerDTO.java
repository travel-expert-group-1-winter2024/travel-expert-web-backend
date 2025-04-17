package org.example.travelexpertwebbackend.dto;

import jakarta.validation.constraints.NotNull;
import org.example.travelexpertwebbackend.entity.Customer;

public class CustomerDTO {
    private Integer customerid;
    private String custfirstname;
    private String custlastname;
    private String custaddress;
    private String custcity;
    private String custprov;
    private String custpostal;
    private String custcountry;
    private String custhomephone;
    private String custbusphone;
    private String custemail;
    private Integer agentId;
    private boolean isAgent;

    @NotNull(message = "Password is required")
    private String password;
    //    @NotNull(message = "Confirm Password is required")
    private Integer points;

    private String tier;

    public CustomerDTO() {
    }

    public CustomerDTO(Integer id, String custfirstname, String custlastname, String custaddress,
                       String custcity, String custprov, String custpostal, String custcountry,
                       String custhomephone, String custbusphone, String custemail, Integer agentId,
                       String password, boolean isAgent) {
        this.customerid = id;
        this.custfirstname = custfirstname;
        this.custlastname = custlastname;
        this.custaddress = custaddress;
        this.custcity = custcity;
        this.custprov = custprov;
        this.custpostal = custpostal;
        this.custcountry = custcountry;
        this.custhomephone = custhomephone;
        this.custbusphone = custbusphone;
        this.custemail = custemail;
        this.agentId = agentId;
        this.password = password;
        this.isAgent = isAgent;
    }

    public CustomerDTO(Customer customer) {
        this.customerid = customer.getId();
        this.custfirstname = customer.getCustfirstname();
        this.custlastname = customer.getCustlastname();
        this.custaddress = customer.getCustaddress();
        this.custcity = customer.getCustcity();
        this.custprov = customer.getCustprov();
        this.custpostal = customer.getCustpostal();
        this.custcountry = customer.getCustcountry();
        this.custhomephone = customer.getCusthomephone();
        this.custbusphone = customer.getCustbusphone();
        this.custemail = customer.getCustemail();
        if (customer.getAgent() != null) {
            this.agentId = customer.getAgent().getId();
        }
        this.isAgent = customer.isAgent();
        this.points = customer.getPoints();
        if (customer.getCustomerTier() != null) {
            this.tier = customer.getCustomerTier().getName();
        } else {
            this.tier = null;
        }
    }

    // Getters and Setters
    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getCustfirstname() {
        return custfirstname;
    }

    public void setCustfirstname(String custfirstname) {
        this.custfirstname = custfirstname;
    }

    public String getCustlastname() {
        return custlastname;
    }

    public void setCustlastname(String custlastname) {
        this.custlastname = custlastname;
    }

    public String getCustaddress() {
        return custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public String getCustcity() {
        return custcity;
    }

    public void setCustcity(String custcity) {
        this.custcity = custcity;
    }

    public String getCustprov() {
        return custprov;
    }

    public void setCustprov(String custprov) {
        this.custprov = custprov;
    }

    public String getCustpostal() {
        return custpostal;
    }

    public void setCustpostal(String custpostal) {
        this.custpostal = custpostal;
    }

    public String getCustcountry() {
        return custcountry;
    }

    public void setCustcountry(String custcountry) {
        this.custcountry = custcountry;
    }

    public String getCusthomephone() {
        return custhomephone;
    }

    public void setCusthomephone(String custhomephone) {
        this.custhomephone = custhomephone;
    }

    public String getCustbusphone() {
        return custbusphone;
    }

    public void setCustbusphone(String custbusphone) {
        this.custbusphone = custbusphone;
    }

    public String getCustemail() {
        return custemail;
    }

    public void setCustemail(String custemail) {
        this.custemail = custemail;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
