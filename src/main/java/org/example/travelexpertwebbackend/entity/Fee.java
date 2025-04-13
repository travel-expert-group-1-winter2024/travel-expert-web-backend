package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Fees")
public class Fee {
    @Id
    @Column(name = "feeid")
    private String feeId;

    @Column(name = "feename")
    private String feeName;

    @NotNull
    @Column(name = "feeamt", nullable = false, precision = 19, scale = 4)
    private BigDecimal feeamt;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "feedesc", length = 50)
    private String feedesc;

    @OneToMany(mappedBy = "feeid")
    private Set<BookingDetail> bookingdetails = new LinkedHashSet<>();

    public Set<BookingDetail> getBookingdetails() {
        return bookingdetails;
    }

    public void setBookingdetails(Set<BookingDetail> bookingdetails) {
        this.bookingdetails = bookingdetails;
    }

    public String getFeedesc() {
        return feedesc;
    }

    public void setFeedesc(String feedesc) {
        this.feedesc = feedesc;
    }

    public BigDecimal getFeeamt() {
        return feeamt;
    }

    public void setFeeamt(BigDecimal feeamt) {
        this.feeamt = feeamt;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
}
