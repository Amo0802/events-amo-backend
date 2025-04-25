package com.example.eventsAmoBE.user;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private boolean isAdmin;

    UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();;
         this.lastName = user.getLastName();
         this.email = user.getEmail();
         this.isAdmin = user.isAdmin();
    }
}
