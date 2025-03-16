package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suppliers_id_gen")
    @SequenceGenerator(name = "suppliers_id_gen", sequenceName = "suppliersid_seq", allocationSize = 1)
    @Column(name = "supplierid", nullable = false)
    private Integer id;

    @Column(name = "supname", length = Integer.MAX_VALUE)
    private String supname;

    @OneToMany(mappedBy = "supplierid")
    @JsonManagedReference
    private Set<ProductsSupplier> productsSuppliers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "supplierid")
    @JsonManagedReference
    private Set<Suppliercontact> suppliercontacts = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupname() {
        return supname;
    }

    public void setSupname(String supname) {
        this.supname = supname;
    }

    public Set<ProductsSupplier> getProductsSuppliers() {
        return productsSuppliers;
    }

    public void setProductsSuppliers(Set<ProductsSupplier> productsSuppliers) {
        this.productsSuppliers = productsSuppliers;
    }

    public Set<Suppliercontact> getSuppliercontacts() {
        return suppliercontacts;
    }

    public void setSuppliercontacts(Set<Suppliercontact> suppliercontacts) {
        this.suppliercontacts = suppliercontacts;
    }

}