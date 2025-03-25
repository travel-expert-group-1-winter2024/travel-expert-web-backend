package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "agencies")
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agencies_id_gen")
    @SequenceGenerator(name = "agencies_id_gen", sequenceName = "agencies_agencyid_seq", initialValue = 3, allocationSize = 1)
    @Column(name = "agencyid", nullable = false)
    private Integer id;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyaddress", length = 50)
    private String agencyAddress;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncycity", length = 50)
    private String agencyCity;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyprov", length = 50)
    private String agencyProvince;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncypostal", length = 50)
    private String agencyPostal;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncycountry", length = 50)
    private String agencyCountry;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyphone", length = 50)
    private String agencyPhone;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyfax", length = 50)
    private String agencyFax;

    @OneToMany(mappedBy = "agency")
    private Set<Agent> agents = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgencyAddress() {
        return agencyAddress;
    }

    public void setAgencyAddress(String agencyAddress) {
        this.agencyAddress = agencyAddress;
    }

    public String getAgencyCity() {
        return agencyCity;
    }

    public void setAgencyCity(String agencyCity) {
        this.agencyCity = agencyCity;
    }

    public String getAgencyProvince() {
        return agencyProvince;
    }

    public void setAgencyProvince(String agencyProvince) {
        this.agencyProvince = agencyProvince;
    }

    public String getAgencyPostal() {
        return agencyPostal;
    }

    public void setAgencyPostal(String agencyPostal) {
        this.agencyPostal = agencyPostal;
    }

    public String getAgencyCountry() {
        return agencyCountry;
    }

    public void setAgencyCountry(String agencyCountry) {
        this.agencyCountry = agencyCountry;
    }

    public String getAgencyPhone() {
        return agencyPhone;
    }

    public void setAgencyPhone(String agencyPhone) {
        this.agencyPhone = agencyPhone;
    }

    public String getAgencyFax() {
        return agencyFax;
    }

    public void setAgencyFax(String agencyFax) {
        this.agencyFax = agencyFax;
    }

    public Set<org.example.travelexpertwebbackend.entity.Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<org.example.travelexpertwebbackend.entity.Agent> agents) {
        this.agents = agents;
    }

}