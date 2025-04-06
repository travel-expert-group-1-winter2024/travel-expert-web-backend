package org.example.travelexpertwebbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ratingsview")
public class RatingsView {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "packageid")
    private Integer packageid;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comments")
    private String comments;

    @Column(name = "customerid")
    private Integer customerid;

    @Column(name = "custfirstname")
    private String custfirstname;

    @Column(name = "custlastname")
    private String custlastname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPackageid() {
        return packageid;
    }

    public void setPackageid(Integer packageid) {
        this.packageid = packageid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getCustfirstname() {
        return custfirstname;
    }

    public void setCustfirstname(String custfirstname) {
        this.custfirstname = custfirstname;
    }

    public String getCustlastname() {
        return custlastname;
    }

    public void setCustlastname(String custlastname) {
        this.custlastname = custlastname;
    }
}
