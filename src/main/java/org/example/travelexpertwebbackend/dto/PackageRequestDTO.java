package org.example.travelexpertwebbackend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PackageRequestDTO {
    private String pkgname;
    private Instant pkgstartdate;
    private Instant pkgenddate;
    private String pkgdesc;
    private BigDecimal pkgbaseprice;
    private BigDecimal pkgagencycommission;
    private List<Integer> productsupplierids; // List of productsupplierid

    // Getters and Setters
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

    public List<Integer> getProductsupplierids() {
        return productsupplierids;
    }

    public void setProductsupplierids(List<Integer> productsupplierids) {
        this.productsupplierids = productsupplierids;
    }
}