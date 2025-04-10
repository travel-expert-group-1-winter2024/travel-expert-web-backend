package org.example.travelexpertwebbackend.dto.booking;

import java.math.BigDecimal;
import java.util.BitSet;

public class CostSummaryResponseDTO {
    private BigDecimal packagePrice;
    private BigDecimal discount;
    private BigDecimal charges;
    private BigDecimal agencyFees;
    private BigDecimal tax;
    private BigDecimal total;

    public CostSummaryResponseDTO() {
    }

    public CostSummaryResponseDTO(BigDecimal packagePrice, BigDecimal tax, BigDecimal charges,
                                  BigDecimal agencyFees, BigDecimal discount, BigDecimal total) {
        this.agencyFees = agencyFees;
        this.charges =charges;
        this.packagePrice = packagePrice;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getCharges() {
        return charges;
    }

    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }

    public BigDecimal getAgencyFees() {
        return agencyFees;
    }

    public void setAgencyFees(BigDecimal agencyFees) {
        this.agencyFees = agencyFees;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
