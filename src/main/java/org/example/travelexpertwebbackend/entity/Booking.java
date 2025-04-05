package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_id_gen")
    @SequenceGenerator(name = "bookings_id_gen", sequenceName = "bookings_bookingid_seq", initialValue = 1304, allocationSize = 1)
    @Column(name = "bookingid", nullable = false)
    private Integer id;

    @Column(name = "bookingdate")
    private Instant bookingdate;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "bookingno", length = 50)
    private String bookingno;

    @Column(name = "travelercount")
    private Double travelercount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private org.example.travelexpertwebbackend.entity.Customer customerid;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("NULL")
    @JoinColumn(name = "triptypeid")
    private TripType triptypeid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageid")
    private org.example.travelexpertwebbackend.entity.Package packageid;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "points_earned")
    private Integer pointsEarned;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @OneToMany(mappedBy = "bookingid")
    private Set<BookingDetail> bookingdetails = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(Instant bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getBookingno() {
        return bookingno;
    }

    public void setBookingno(String bookingno) {
        this.bookingno = bookingno;
    }

    public Double getTravelercount() {
        return travelercount;
    }

    public void setTravelercount(Double travelercount) {
        this.travelercount = travelercount;
    }

    public org.example.travelexpertwebbackend.entity.Customer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(org.example.travelexpertwebbackend.entity.Customer customerid) {
        this.customerid = customerid;
    }

    public TripType getTriptypeid() {
        return triptypeid;
    }

    public void setTriptypeid(TripType triptypeid) {
        this.triptypeid = triptypeid;
    }

    public org.example.travelexpertwebbackend.entity.Package getPackageid() {
        return packageid;
    }

    public void setPackageid(org.example.travelexpertwebbackend.entity.Package packageid) {
        this.packageid = packageid;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Set<BookingDetail> getBookingdetails() {
        return bookingdetails;
    }

    public void setBookingdetails(Set<BookingDetail> bookingdetails) {
        this.bookingdetails = bookingdetails;
    }

}