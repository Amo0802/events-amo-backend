package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SearchEventService {

    private final EventRepository eventRepository;

    public SearchEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public PageResponse<Event> execute(String search, Pageable pageable){

        Page<Event> page = eventRepository.searchUpcomingByNameOrDescription(
                        search, LocalDateTime.now(), pageable);

        return new PageResponse<>(page);
    }
}
