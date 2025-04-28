package com.example.eventsAmoBE.user.model;

import lombok.Data;

@Data
public class EmailChangeRequest {
    private String currentPassword;
    private String newEmail;
}