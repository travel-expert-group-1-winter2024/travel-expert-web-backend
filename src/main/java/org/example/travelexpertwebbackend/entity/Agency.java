package org.example.travelexpertwebbackend.entity;

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
    private String agncyaddress;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncycity", length = 50)
    private String agncycity;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyprov", length = 50)
    private String agncyprov;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncypostal", length = 50)
    private String agncypostal;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncycountry", length = 50)
    private String agncycountry;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyphone", length = 50)
    private String agncyphone;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "agncyfax", length = 50)
    private String agncyfax;

    @OneToMany(mappedBy = "agencyid")
    private Set<org.example.travelexpertwebbackend.entity.Agent> agents = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgncyaddress() {
        return agncyaddress;
    }

    public void setAgncyaddress(String agncyaddress) {
        this.agncyaddress = agncyaddress;
    }

    public String getAgncycity() {
        return agncycity;
    }

    public void setAgncycity(String agncycity) {
        this.agncycity = agncycity;
    }

    public String getAgncyprov() {
        return agncyprov;
    }

    public void setAgncyprov(String agncyprov) {
        this.agncyprov = agncyprov;
    }

    public String getAgncypostal() {
        return agncypostal;
    }

    public void setAgncypostal(String agncypostal) {
        this.agncypostal = agncypostal;
    }

    public String getAgncycountry() {
        return agncycountry;
    }

    public void setAgncycountry(String agncycountry) {
        this.agncycountry = agncycountry;
    }

    public String getAgncyphone() {
        return agncyphone;
    }

    public void setAgncyphone(String agncyphone) {
        this.agncyphone = agncyphone;
    }

    public String getAgncyfax() {
        return agncyfax;
    }

    public void setAgncyfax(String agncyfax) {
        this.agncyfax = agncyfax;
    }

    public Set<org.example.travelexpertwebbackend.entity.Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<org.example.travelexpertwebbackend.entity.Agent> agents) {
        this.agents = agents;
    }

}