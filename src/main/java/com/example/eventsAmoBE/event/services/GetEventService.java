package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetEventService {

    private final EventRepository eventRepository;

    public GetEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event execute(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()){
            return eventOptional.get();
        }
        throw new RuntimeException();
    }
}
