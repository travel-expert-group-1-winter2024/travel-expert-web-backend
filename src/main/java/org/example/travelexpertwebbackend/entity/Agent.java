package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "agents")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agents_id_gen")
    @SequenceGenerator(name = "agents_id_gen", sequenceName = "agents_agentid_seq", initialValue = 10, allocationSize = 1)
    @Column(name = "agentid", nullable = false)
    private Integer id;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtfirstname", length = 20)
    private String agtFirstName;

    @Size(max = 5)
    @ColumnDefault("NULL")
    @Column(name = "agtmiddleinitial", length = 5)
    private String agtMiddleInitial;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtlastname", length = 20)
    private String agtLastName;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtbusphone", length = 20)
    private String agtBusPhone;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agtemail", length = 50)
    private String agtEmail;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtposition", length = 20)
    private String agtPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencyid")
    private Agency agency;

    @OneToMany(mappedBy = "agent")
    private Set<Customer> customers = new LinkedHashSet<>();

    @OneToOne(mappedBy = "agentid")
    private User user;

    private String photoPath;

    public Agent() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getAgtPosition() {
        return agtPosition;
    }

    public void setAgtPosition(String agtPosition) {
        this.agtPosition = agtPosition;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}