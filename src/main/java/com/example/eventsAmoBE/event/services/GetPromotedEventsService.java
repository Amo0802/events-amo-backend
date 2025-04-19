package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class GetPromotedEventsService {

    private final EventRepository eventRepository;

    public GetPromotedEventsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public PageResponse<Event> execute(Pageable pageable){

        Page<Event> page = eventRepository.findUpcomingPromotedEvents(LocalDateTime.now(), pageable);

        return new PageResponse<>(page);
    }
}
