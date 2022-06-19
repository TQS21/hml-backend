package com.service.hml.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserDTO {
    private String name;

    private String email;

    private String password;

    private int phone;

    public UserDTO(){}

    public UserDTO(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String name, String email, String password, int phone){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public static UserDTO fromUserEntity(User user){
        return new UserDTO(user.getName(), user.getEmail(), user.getPassword());
    }

    public User toUserEntity(UserDTO userDTO){
        return new User(userDTO.name, userDTO.email, userDTO.password);
    }

    public String getName() {return name;}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                '}';
    }
}
