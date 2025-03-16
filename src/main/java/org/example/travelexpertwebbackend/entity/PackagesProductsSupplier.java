package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "packages_products_suppliers")
public class PackagesProductsSupplier {
    @SequenceGenerator(name = "packages_products_suppliers_id_gen", sequenceName = "packages_packageid_seq", initialValue = 5, allocationSize = 1)
    @EmbeddedId
    private PackagesProductsSupplierId id;

    @MapsId("packageid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "packageid", nullable = false)
    private Package packageid;

    @MapsId("productsupplierid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productsupplierid", nullable = false)
    private ProductsSupplier productsupplierid;

    public PackagesProductsSupplierId getId() {
        return id;
    }

    public void setId(PackagesProductsSupplierId id) {
        this.id = id;
    }

    public Package getPackageid() {
        return packageid;
    }

    public void setPackageid(Package packageid) {
        this.packageid = packageid;
    }

    public ProductsSupplier getProductsupplierid() {
        return productsupplierid;
    }

    public void setProductsupplierid(ProductsSupplier productsupplierid) {
        this.productsupplierid = productsupplierid;
    }

}