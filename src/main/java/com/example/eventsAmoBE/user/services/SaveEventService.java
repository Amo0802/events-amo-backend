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
public class SaveEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;

    public SaveEventService(UserRepository userRepository, EventRepository eventRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void saveEvent(Long eventId) {
        User user = currentUserService.getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getSavedEvents().add(event);
        userRepository.save(user);
    }

    @Transactional
    public void unsaveEvent(Long eventId) {
        User user = currentUserService.getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getSavedEvents().remove(event);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Set<EventDto> getSavedEvents() {
        User user = currentUserService.getCurrentUserWithSavedEvents();

        return user.getSavedEvents().stream()
                .map(event -> new EventDto(event, user))
                .collect(Collectors.toSet());
    }
}
