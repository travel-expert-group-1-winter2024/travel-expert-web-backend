package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Classes {
    @Id
    @Column(name = "classid")
    private String classid;

    @Column(name = "classname")
    private String classname;

    @Column(name = "classdesc")
    private String classdesc;

    @OneToMany(mappedBy = "classid")
    private Set<BookingDetail> bookingdetails = new LinkedHashSet<>();

    public Set<BookingDetail> getBookingdetails() {
        return bookingdetails;
    }

    public void setBookingdetails(Set<BookingDetail> bookingdetails) {
        this.bookingdetails = bookingdetails;
    }

    public String getClassdesc() {
        return classdesc;
    }

    public void setClassdesc(String classdesc) {
        this.classdesc = classdesc;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
