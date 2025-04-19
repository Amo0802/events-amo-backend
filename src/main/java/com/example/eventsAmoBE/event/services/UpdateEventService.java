package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateEventService {

    private final EventRepository eventRepository;

    public UpdateEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @CacheEvict(value = "events", allEntries = true)
    public Event execute(Long id, Event event) {

        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {

            event.setId(id);

            eventRepository.save(event);

            return event;
        }

        throw new RuntimeException();
    }
}
