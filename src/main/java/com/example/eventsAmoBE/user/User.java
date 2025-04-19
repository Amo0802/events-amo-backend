package com.example.eventsAmoBE.user;

import jakarta.persistence.*;
import lombok.Data;
import com.example.eventsAmoBE.event.model.Event;

import java.util.Set;

@Data
@Entity
@Table(name = "com/example/eventsAmoBE/user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;

    @ManyToMany
    @JoinTable(
            name = "user_saved_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> savedEvents;

    @ManyToMany
    @JoinTable(
            name = "user_attending_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> attendingEvents;
}
