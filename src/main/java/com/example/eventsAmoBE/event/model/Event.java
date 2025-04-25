package com.example.eventsAmoBE.event.model;

import com.example.eventsAmoBE.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private String address;
    private LocalDateTime startDateTime;
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private City city;

    @ElementCollection
    @CollectionTable(name = "event_categories", joinColumns = @JoinColumn(name = "event_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<Category> categories = new HashSet<>();

    private int priority;
    private boolean mainEvent;
    private boolean promoted;

    @ManyToMany(mappedBy = "attendingEvents")
    private Set<User> attendees = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
