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
    private String id;

    @Size(max = 25)
    @ColumnDefault("NULL")
    @Column(name = "ttname", length = 25)
    private String ttName;

    @OneToMany(mappedBy = "tripType")
    private Set<Booking> bookings = new LinkedHashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTtName() {
        return ttName;
    }

    public void setTtName(String ttname) {
        this.ttName = ttname;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

}