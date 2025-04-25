package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import com.example.eventsAmoBE.user.User;
import org.springframework.stereotype.Service;
import com.example.eventsAmoBE.user.UserService;

@Service
public class GetEventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    public GetEventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public EventDto execute(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userService.getCurrentUser();

        return new EventDto(event, user);
    }
}
