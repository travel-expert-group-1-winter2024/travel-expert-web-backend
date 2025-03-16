package org.example.travelexpertwebbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_gen")
    @SequenceGenerator(name = "products_id_gen", sequenceName = "products_productid_seq", initialValue = 17, allocationSize = 1)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "prodname", nullable = false, length = 50)
    private String prodname;

    @OneToMany(mappedBy = "productid")
    @JsonManagedReference
    private Set<ProductsSupplier> productsSuppliers = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public Set<ProductsSupplier> getProductsSuppliers() {
        return productsSuppliers;
    }

    public void setProductsSuppliers(Set<ProductsSupplier> productsSuppliers) {
        this.productsSuppliers = productsSuppliers;
    }

}