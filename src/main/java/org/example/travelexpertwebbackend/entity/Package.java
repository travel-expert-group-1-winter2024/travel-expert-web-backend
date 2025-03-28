package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packages_id_gen")
    @SequenceGenerator(name = "packages_id_gen", sequenceName = "packages_packageid_seq", initialValue = 5, allocationSize = 1)
    @Column(name = "packageid", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "pkgname", nullable = false, length = 50)
    private String pkgname;

    @Column(name = "pkgstartdate")
    private Instant pkgstartdate;

    @Column(name = "pkgenddate")
    private Instant pkgenddate;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "pkgdesc", length = 50)
    private String pkgdesc;

    @NotNull
    @Column(name = "pkgbaseprice", nullable = false, precision = 19, scale = 4)
    private BigDecimal pkgbaseprice;

    @ColumnDefault("NULL")
    @Column(name = "pkgagencycommission", precision = 19, scale = 4)
    private BigDecimal pkgagencycommission;

    @OneToMany(mappedBy = "packageid")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "packages_products_suppliers",
            joinColumns = @JoinColumn(name = "packageid"),
            inverseJoinColumns = @JoinColumn(name = "productsupplierid"))
    @JsonIgnore
    private Set<ProductsSupplier> productsSuppliers = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public Instant getPkgstartdate() {
        return pkgstartdate;
    }

    public void setPkgstartdate(Instant pkgstartdate) {
        this.pkgstartdate = pkgstartdate;
    }

    public Instant getPkgenddate() {
        return pkgenddate;
    }

    public void setPkgenddate(Instant pkgenddate) {
        this.pkgenddate = pkgenddate;
    }

    public String getPkgdesc() {
        return pkgdesc;
    }

    public void setPkgdesc(String pkgdesc) {
        this.pkgdesc = pkgdesc;
    }

    public BigDecimal getPkgbaseprice() {
        return pkgbaseprice;
    }

    public void setPkgbaseprice(BigDecimal pkgbaseprice) {
        this.pkgbaseprice = pkgbaseprice;
    }

    public BigDecimal getPkgagencycommission() {
        return pkgagencycommission;
    }

    public void setPkgagencycommission(BigDecimal pkgagencycommission) {
        this.pkgagencycommission = pkgagencycommission;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<ProductsSupplier> getProductsSuppliers() {
        return productsSuppliers;
    }

    public void setProductsSuppliers(Set<ProductsSupplier> productsSuppliers) {
        this.productsSuppliers = productsSuppliers;
    }

}