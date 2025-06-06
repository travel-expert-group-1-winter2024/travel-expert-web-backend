package org.example.travelexpertwebbackend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class PackageDetailsDTO {
    private Integer packageid;
    private String pkgname;
    private Instant pkgstartdate;
    private Instant pkgenddate;
    private String pkgdesc;
    private BigDecimal pkgbaseprice;
    private BigDecimal pkgagencycommission;
    private Set<ProductSupplierDTO> productsSuppliers;
    private String destination;
    private String photoURL;
    private List<String> tags;

    // Getters and Setters
    public Integer getPackageid() {
        return packageid;
    }

    public void setPackageid(Integer packageid) {
        this.packageid = packageid;
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

    public Set<ProductSupplierDTO> getProductsSuppliers() {
        return productsSuppliers;
    }

    public void setProductsSuppliers(Set<ProductSupplierDTO> productsSuppliers) {
        this.productsSuppliers = productsSuppliers;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
