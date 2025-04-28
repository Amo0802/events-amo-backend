package com.example.eventsAmoBE.user.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import com.example.eventsAmoBE.user.UserRepository;
import com.example.eventsAmoBE.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttendEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;

    public AttendEventService(UserRepository userRepository, EventRepository eventRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void attendEvent(Long eventId) {
        User user = currentUserService.getCurrentUserWithAttendingEvents();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Explicitly initialize the collection to prevent LazyInitializationException
        if (user.getAttendingEvents() != null) {
            user.getAttendingEvents().size(); // Force initialization
        }

        // Use helper method for proper relationship management
        user.attendEvent(event);

        // Just save the user - Hibernate will handle the bidirectional relationship
        userRepository.save(user);
    }

    @Transactional
    public void unattendEvent(Long eventId) {
        User user = currentUserService.getCurrentUserWithAttendingEvents();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Explicitly initialize the collection
        if (user.getAttendingEvents() != null) {
            user.getAttendingEvents().size(); // Force initialization
        }

        // Use helper method for proper relationship management
        user.unattendEvent(event);

        // Just save the user - Hibernate will handle the bidirectional relationship
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Set<EventDto> getAttendingEvents() {
        User user = currentUserService.getCurrentUserWithAttendingEvents();
        return user.getAttendingEvents().stream()
                .map(event -> new EventDto(event, user))
                .collect(Collectors.toSet());
    }
}