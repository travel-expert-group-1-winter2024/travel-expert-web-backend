package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookingdetails")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingdetails_id_gen")
    @SequenceGenerator(name = "bookingdetails_id_gen", sequenceName = "bookingdetails_bookingdetailid_seq", initialValue = 1304, allocationSize = 1)
    @Column(name = "bookingdetailid", nullable = false)
    private Integer id;

    @Column(name = "itineraryno")
    private Double itineraryno;

    @Column(name = "tripstart")
    private Instant tripstart;

    @Column(name = "tripend")
    private Instant tripend;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "destination", length = Integer.MAX_VALUE)
    private String destination;

    @ColumnDefault("NULL")
    @Column(name = "baseprice", precision = 19, scale = 4)
    private BigDecimal baseprice;

    @ColumnDefault("NULL")
    @Column(name = "agencycommission", precision = 19, scale = 4)
    private BigDecimal agencycommission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingid")
    private org.example.travelexpertwebbackend.entity.Booking bookingid;

    @Column(name = "productsupplierid")
    private Integer productsupplierid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getItineraryno() {
        return itineraryno;
    }

    public void setItineraryno(Double itineraryno) {
        this.itineraryno = itineraryno;
    }

    public Instant getTripstart() {
        return tripstart;
    }

    public void setTripstart(Instant tripstart) {
        this.tripstart = tripstart;
    }

    public Instant getTripend() {
        return tripend;
    }

    public void setTripend(Instant tripend) {
        this.tripend = tripend;
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

    public BigDecimal getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(BigDecimal baseprice) {
        this.baseprice = baseprice;
    }

    public BigDecimal getAgencycommission() {
        return agencycommission;
    }

    public void setAgencycommission(BigDecimal agencycommission) {
        this.agencycommission = agencycommission;
    }

    public org.example.travelexpertwebbackend.entity.Booking getBookingid() {
        return bookingid;
    }

    public void setBookingid(org.example.travelexpertwebbackend.entity.Booking bookingid) {
        this.bookingid = bookingid;
    }

    public Integer getProductsupplierid() {
        return productsupplierid;
    }

    public void setProductsupplierid(Integer productsupplierid) {
        this.productsupplierid = productsupplierid;
    }

}