package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import com.example.eventsAmoBE.user.model.User;
import com.example.eventsAmoBE.user.services.CurrentUserService;
import org.springframework.stereotype.Service;

@Service
public class GetEventService {

    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;

    public GetEventService(EventRepository eventRepository, CurrentUserService currentUserService) {
        this.eventRepository = eventRepository;
        this.currentUserService = currentUserService;
    }

    public EventDto execute(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return currentUserService.getOptionalCurrentUser()
                .map(user -> new EventDto(event, user))
                .orElseGet(() -> new EventDto(event, false, false));
    }
}
