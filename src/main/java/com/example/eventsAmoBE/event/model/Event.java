package com.example.eventsAmoBE.event.model;

import com.example.eventsAmoBE.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "com/example/eventsAmoBE/event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private City city;
    private LocalDateTime startDateTime;
    private Double price;

    @ElementCollection
    @CollectionTable(name = "event_categories", joinColumns = @JoinColumn(name = "event_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<Category> categories = new HashSet<>();

    private int priority;
    private boolean eventSaved;
    private boolean eventAttending;
    private boolean mainEvent;
    private boolean promoted;
    private boolean notification;

    @ManyToMany(mappedBy = "attendingEvents")
    private Set<User> attendees = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
