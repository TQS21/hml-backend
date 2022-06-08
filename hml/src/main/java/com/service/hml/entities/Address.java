package com.service.hml.entities;

public class Address {

    private String country;

    private String zipcode;

    private String address;

    public Address(String country, String zipcode, String address){
        this.country = country;
        this.zipcode = zipcode;
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}