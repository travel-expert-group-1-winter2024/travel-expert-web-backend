package org.example.travelexpertwebbackend.entity;

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
    private String agtfirstname;

    @Size(max = 5)
    @ColumnDefault("NULL")
    @Column(name = "agtmiddleinitial", length = 5)
    private String agtmiddleinitial;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtlastname", length = 20)
    private String agtlastname;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtbusphone", length = 20)
    private String agtbusphone;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agtemail", length = 50)
    private String agtemail;

    @Size(max = 20)
    @ColumnDefault("NULL")
    @Column(name = "agtposition", length = 20)
    private String agtposition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencyid")
    private Agency agencyid;

    @OneToMany(mappedBy = "agentid")
    private Set<org.example.travelexpertwebbackend.entity.Customer> customers = new LinkedHashSet<>();

    @OneToOne(mappedBy = "agentid")
    private org.example.travelexpertwebbackend.entity.User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgtfirstname() {
        return agtfirstname;
    }

    public void setAgtfirstname(String agtfirstname) {
        this.agtfirstname = agtfirstname;
    }

    public String getAgtmiddleinitial() {
        return agtmiddleinitial;
    }

    public void setAgtmiddleinitial(String agtmiddleinitial) {
        this.agtmiddleinitial = agtmiddleinitial;
    }

    public String getAgtlastname() {
        return agtlastname;
    }

    public void setAgtlastname(String agtlastname) {
        this.agtlastname = agtlastname;
    }

    public String getAgtbusphone() {
        return agtbusphone;
    }

    public void setAgtbusphone(String agtbusphone) {
        this.agtbusphone = agtbusphone;
    }

    public String getAgtemail() {
        return agtemail;
    }

    public void setAgtemail(String agtemail) {
        this.agtemail = agtemail;
    }

    public String getAgtposition() {
        return agtposition;
    }

    public void setAgtposition(String agtposition) {
        this.agtposition = agtposition;
    }

    public Agency getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(Agency agencyid) {
        this.agencyid = agencyid;
    }

    public Set<org.example.travelexpertwebbackend.entity.Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<org.example.travelexpertwebbackend.entity.Customer> customers) {
        this.customers = customers;
    }

    public org.example.travelexpertwebbackend.entity.User getUser() {
        return user;
    }

    public void setUser(org.example.travelexpertwebbackend.entity.User user) {
        this.user = user;
    }

}