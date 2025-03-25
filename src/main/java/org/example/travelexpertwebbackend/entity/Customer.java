package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_id_gen")
    @SequenceGenerator(name = "customers_id_gen", sequenceName = "customers_customerid_seq", initialValue = 145, allocationSize = 1)
    @Column(name = "customerid", nullable = false)
    private Integer id;

    @Size(max = 25)
    @NotNull
    @Column(name = "custfirstname", nullable = false, length = 25)
    private String custfirstname;

    @Size(max = 25)
    @NotNull
    @Column(name = "custlastname", nullable = false, length = 25)
    private String custlastname;

    @Size(max = 75)
    @NotNull
    @Column(name = "custaddress", nullable = false, length = 75)
    private String custaddress;

    @Size(max = 50)
    @NotNull
    @Column(name = "custcity", nullable = false, length = 50)
    private String custcity;

    @Size(max = 2)
    @NotNull
    @Column(name = "custprov", nullable = false, length = 2)
    private String custprov;

    @Size(max = 7)
    @NotNull
    @Column(name = "custpostal", nullable = false, length = 7)
    private String custpostal;

    @Size(max = 25)
    @ColumnDefault("NULL")
    @Column(name = "custcountry", length = 25)
    private String custcountry;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "custhomephone", length = 20)
    private String custhomephone;

    @Size(max = 20)
    @NotNull
    @Column(name = "custbusphone", nullable = false, length = 20)
    private String custbusphone;

    @Size(max = 50)
    @NotNull
    @Column(name = "custemail", nullable = false, length = 50)
    private String custemail;

    @ManyToOne()
    @JoinColumn(name = "agentid")
    private Agent agent;

    @OneToMany(mappedBy = "customerid")
    @JsonIgnore
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToOne(mappedBy = "customerid")
    private org.example.travelexpertwebbackend.entity.User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public org.example.travelexpertwebbackend.entity.User getUser() {
        return user;
    }

    public void setUser(org.example.travelexpertwebbackend.entity.User user) {
        this.user = user;
    }

}