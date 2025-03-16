package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "triptypes")
public class TripType {
    @Id
    @Size(max = 1)
    @SequenceGenerator(name = "triptypes_id_gen", sequenceName = "suppliersid_seq", allocationSize = 1)
    @Column(name = "triptypeid", nullable = false, length = 1)
    private String triptypeid;

    @Size(max = 25)
    @ColumnDefault("NULL")
    @Column(name = "ttname", length = 25)
    private String ttname;

    @OneToMany(mappedBy = "triptypeid")
    private Set<Booking> bookings = new LinkedHashSet<>();

    public String getTriptypeid() {
        return triptypeid;
    }

    public void setTriptypeid(String triptypeid) {
        this.triptypeid = triptypeid;
    }

    public String getTtname() {
        return ttname;
    }

    public void setTtname(String ttname) {
        this.ttname = ttname;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

}