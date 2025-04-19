package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CreateEventService {

    private final EventRepository eventRepository;

    public CreateEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @CacheEvict(value = "events", allEntries = true)
    public Event execute(Event input) {
        return eventRepository.save(input);
    }
}
