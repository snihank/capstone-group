package com.trilogyed.retail.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Customer {

    private Integer customerId;
    @Size(max = 50, message = "Your first name is too long")
    private String firstName;
    @Size(max = 50, message = "Your last name is too long")
    private String lastName;
    @Size(max = 50, message = "Your street name is too long")
    private String street;
    @Size(max = 50, message = "Your city name is too long")
    private String city;
    @Size(max = 6, message = "Your zip is too long")
    private String zip;
    @Email
    private String email;
    private String phone;

    public Customer() {
    }

    public Customer(Integer customerId, @Size(max = 50, message = "Your first name is too long") String firstName, @Size(max = 50, message = "Your last name is too long") String lastName, @Size(max = 50, message = "Your street name is too long") String street, @Size(max = 50, message = "Your city name is too long") String city, @Size(max = 6, message = "Your zip is too long") String zip, String email, String phone) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getCustomerId().equals(customer.getCustomerId()) &&
                getFirstName().equals(customer.getFirstName()) &&
                getLastName().equals(customer.getLastName()) &&
                getStreet().equals(customer.getStreet()) &&
                getCity().equals(customer.getCity()) &&
                getZip().equals(customer.getZip()) &&
                getEmail().equals(customer.getEmail()) &&
                getPhone().equals(customer.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getFirstName(), getLastName(), getStreet(), getCity(), getZip(), getEmail(), getPhone());
    }
}
