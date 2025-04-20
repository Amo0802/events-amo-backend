package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteEventService {

    private final EventRepository eventRepository;

    public DeleteEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @CacheEvict(value = "events", allEntries = true)
    public Void execute(Long id) {
        Optional<Event> productWeWantToDelete = eventRepository.findById(id);
        if(productWeWantToDelete.isPresent()){
            eventRepository.deleteById(id);
            return null;
        }
        throw new RuntimeException("Id doesn't exist!");
    }
}