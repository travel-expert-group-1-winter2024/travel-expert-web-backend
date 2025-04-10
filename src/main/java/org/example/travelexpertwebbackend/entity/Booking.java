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
    private Instant bookingDate;

    @Size(max = 50)
    @ColumnDefault("NULL")
    @Column(name = "bookingno", length = 50)
    private String bookingNo;

    @Column(name = "travelercount")
    private Double travelerCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("NULL")
    @JoinColumn(name = "triptypeid")
    private TripType tripType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageid")
    private Package packageid;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "points_earned")
    private Integer pointsEarned;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<BookingDetail> bookingDetails = new LinkedHashSet<>();

    @Column(name = "reserved_datetime")
    private Instant reservedDatetime;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PENDING'")
    @Column(name = "booking_status", length = 20)
    private BookingStatus bookingStatus;

    public Booking() {
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Instant getReservedDatetime() {
        return reservedDatetime;
    }

    public void setReservedDatetime(Instant reservedDatetime) {
        this.reservedDatetime = reservedDatetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public Double getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(Double travelerCount) {
        this.travelerCount = travelerCount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public Package getPackageid() {
        return packageid;
    }

    public void setPackageid(Package packageid) {
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

    public Set<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(Set<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public enum BookingStatus {
        PENDING, // for pending payment and old record
        RESERVED, // for reserve mode
        CONFIRMED, // for normal mode
        CANCELLED, // future use
        COMPLETED, // after complete transaction
        EXPIRED // if not paid within 24 hours
    }

}