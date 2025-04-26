package com.example.eventsAmoBE.event.model;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestDTO {
    private String name;
    private String description;
    private String address;
    private String startDateTime; // as ISO string
    private String price;
    private List<String> categories; // as list of strings
}
