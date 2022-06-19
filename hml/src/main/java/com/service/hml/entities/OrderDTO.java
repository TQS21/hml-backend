package com.service.hml.entities;

public class OrderDTO {
    private UserDTO userDTO;
    private Address address;

    public OrderDTO() {}

    public OrderDTO(UserDTO cpd, Address apd) {
        this.userDTO = cpd;
        this.address = apd;
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

    @Override
    public String toString() {
        return "Order{" +
                ", client=" + userDTO +
                ", address=" + address +
                '}';
    }
}