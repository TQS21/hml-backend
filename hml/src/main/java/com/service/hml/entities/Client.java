package com.service.hml.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {
    private String name;
    private int phoneNumber;

    public Client() {}

    public Client(String name, int phone) {
        this.name = name;
        this.phoneNumber = phone;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("phone_number")
    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(int phone) {
        this.phoneNumber = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
