package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookingdetailsview")
public class BookingDetailsView {
    @Id
    @Column(name = "bookingdetailid")
    private Integer bookingDetailId;

    @Column(name = "itineraryno")
    private Float itineraryNo;

    @Column(name = "tripstart")
    private LocalDateTime tripStart;

    @Column(name = "tripend")
    private LocalDateTime tripEnd;

    @Column(name = "description")
    private String description;

    @Column(name = "destination")
    private String destination;

    @Column(name = "baseprice")
    private BigDecimal basePrice;

    @Column(name = "agencycommission")
    private BigDecimal agencyCommission;

    @Column(name = "agentid")
    private Integer agentid;

    @Column(name = "customerid")
    private Integer customerid;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @ManyToOne
    @JoinColumn(name = "bookingid", referencedColumnName = "bookingid")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "region", referencedColumnName = "regionname")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "class", referencedColumnName = "classname")
    private Classes className;

    @ManyToOne
    @JoinColumn(name = "fee", referencedColumnName = "feeName")
    private Fee fee;

//    @ManyToOne
//    @JoinColumn(name = "product", referencedColumnName = "productsupplierid")
//    private ProductsSupplier productSupplier;

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "prodname")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier", referencedColumnName = "supname")
    private Supplier supplier;

    @Size(max = 50)
    @Column(name = "bookingno", length = 50)
    private String bookingNo;

    @Size(max = 20)
    @Column(name = "booking_status", length = 20)
    private String bookingStatus;

    @Column(name = "packageid")
    private Integer packageId;

    @Column(name = "travelercount")
    private Double travelerCount;

    @Size(max = 1)
    @Column(name = "triptypeid", length = 1)
    private String tripTypeId;

    @Column(name = "travellername")
    private String travellername;

    public String getTripTypeId() {
        return tripTypeId;
    }

    public Double getTravelerCount() {
        return travelerCount;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public Integer getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Integer bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public Float getItineraryNo() {
        return itineraryNo;
    }

    public void setItineraryNo(Float itineraryNo) {
        this.itineraryNo = itineraryNo;
    }

    public LocalDateTime getTripStart() {
        return tripStart;
    }

    public void setTripStart(LocalDateTime tripStart) {
        this.tripStart = tripStart;
    }

    public LocalDateTime getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(LocalDateTime tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getAgencyCommission() {
        return agencyCommission;
    }

    public void setAgencyCommission(BigDecimal agencyCommission) {
        this.agencyCommission = agencyCommission;
    }

    public Integer getBooking() {
        return booking.getId();
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getRegion() {
        return region.getRegionName();
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getClassName() {
        return className.getClassname();
    }

    public void setClassName(Classes className) {
        this.className = className;
    }

    public String getFee() {
        return fee.getFeeName();
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public String getProduct() {
        return product.getProdname();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSupplier() {
        return supplier.getSupname();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTravellername() {
        return travellername;
    }

    public void setTravellername(String travellername) {
        this.travellername = travellername;
    }
}
