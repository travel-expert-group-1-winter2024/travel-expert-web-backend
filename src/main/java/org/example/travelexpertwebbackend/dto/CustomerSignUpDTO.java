package org.example.travelexpertwebbackend.dto;

import jakarta.validation.constraints.*;


public class CustomerSignUpDTO {

    @NotNull
    @Size(min = 1, max = 25)
    @NotBlank
    private String firstName;

    @NotNull
    @Size(min = 1, max = 25)
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @Pattern(regexp = "/^[A-Za-z]+$/")
    @NotBlank
    private String city;

    @NotNull
    private String province;

    @NotNull
    @Pattern(regexp = "/^[A-Za-z]\\d[A-Za-z]\\s?\\d[A-Za-z]\\d$/")
    @NotBlank
    private String postalCode;

    @NotNull
    @Pattern(regexp = "/^[A-Za-z]+$/")
    @NotBlank
    private String country;

    @NotNull
    @NotBlank
    @Pattern(regexp = "/^\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}$/")
    @Size(min = 10)
    private String homePhone;

    @NotNull
    @NotBlank
    @Pattern(regexp = "/^\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}$/")
    @Size(min = 10)
    private String busPhone;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,}$" , message = "Password must contain at least one upper case letter, one number and one special character.")
    private String password;

    /**
     * ! Args & No Args Constructors
     */

    public CustomerSignUpDTO() {}

    public CustomerSignUpDTO(String firstName, String lastName, String address, String city, String province, String postalCode, String country, String homePhone, String busPhone, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.homePhone = homePhone;
        this.busPhone = busPhone;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * ! Getters and Setters
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getBusPhone() {
        return busPhone;
    }

    public void setBusPhone(String busPhone) {
        this.busPhone = busPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}//class
