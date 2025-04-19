package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Category;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class GetEventsByCategoryService {

    private final EventRepository eventRepository;

    public GetEventsByCategoryService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public PageResponse<Event> execute(Pageable pageable, String category){

        Category category1 = Category.valueOf(category.toUpperCase());
        Page<Event> page = eventRepository.findUpcomingByCategoriesIn(category1, LocalDateTime.now(), pageable);
        return new PageResponse<>(page);
    }
}
