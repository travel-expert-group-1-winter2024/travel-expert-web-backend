package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

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

    @Size(max = 255)
    @Column(name = "photo_path")
    private String photoPath;

    @ColumnDefault("0")
    @Column(name = "points")
    private Integer points = 0;

    @ManyToOne()
    @JoinColumn(name = "agentid")
    private Agent agent;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_tier_id")
    private CustomerTier customerTier;

    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

    @Column(name = "is_agent")
    private boolean isAgent;

    public Customer() {
    }

    public CustomerTier getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(CustomerTier customerTier) {
        this.customerTier = customerTier;
    }

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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }
}