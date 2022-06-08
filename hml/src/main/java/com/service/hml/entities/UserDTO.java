package com.service.hml.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserDTO {
    private String name;

    private String email;

    private String password;

    public UserDTO(){}

    public UserDTO(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
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
}
