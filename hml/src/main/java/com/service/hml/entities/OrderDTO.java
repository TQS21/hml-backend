package com.service.hml.entities;

import java.util.List;

public class OrderDTO {
    private UserDTO userDTO;
    private Address address;

    private List<OrderStatsDTO> orderedBooks;

    public OrderDTO() {}

    public OrderDTO(UserDTO cpd, Address apd, List<OrderStatsDTO> orderedBooks) {
        this.userDTO = cpd;
        this.address = apd;
        this.orderedBooks = orderedBooks;
    }

    public Order createOrder(){
        Client client = new Client(this.userDTO.getName(),this.userDTO.getPhone());
        return new Order(1,1,client,this.address);
    }

    public UserDTO getUserDTO() {
        return this.userDTO;
    }

    public void setUserDTO(UserDTO cpd) {
        this.userDTO = cpd;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address apd) {
        this.address = apd;
    }

    public List<OrderStatsDTO> getOrderedBooks() {
        return orderedBooks;
    }

    public void setOrderedBooks(List<OrderStatsDTO> orderedBooks) {
        this.orderedBooks = orderedBooks;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", client=" + userDTO +
                ", address=" + address +
                '}';
    }
}