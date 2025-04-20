package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.utils.PageResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetEventsService {

    private final EventRepository eventRepository;

    public GetEventsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Cacheable(value = "events", key = "'allEvents_' + #pageable.pageNumber + '_' + #pageable.pageSize", cacheManager = "eventCacheManager")
    public PageResponse<Event> execute(Pageable pageable) {
        Page<Event> page = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.isLast()
        );
    }
}