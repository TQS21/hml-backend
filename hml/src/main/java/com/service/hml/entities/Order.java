package com.service.hml.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private int shopId;
    private int shopOrderRef;
    private Client client;
    private Address address;

    public Order() {}

    public Order(int id, int shopOrderRef, Client cpd, Address apd) {
        this.shopId = id;
        this.shopOrderRef = shopOrderRef;
        this.client = cpd;
        this.address = apd;
    }

    @JsonProperty("shop_id")
    public int getShopId() {
        return this.shopId;
    }

    public void setShopId(int id) {
        this.shopId = id;
    }

    @JsonProperty("shop_order_ref")
    public int getShopOrderRef() {
        return this.shopOrderRef;
    }

    public void setShopOrderRef(int id) {
        this.shopOrderRef = id;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client cpd) {
        this.client = cpd;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address apd) {
        this.address = apd;
    }

    @Override
    public String toString() {
        return "Order{" +
                "shop_id=" + shopId +
                ", shop_order_ref=" + shopOrderRef +
                ", client=" + client +
                ", address=" + address +
                '}';
    }
}