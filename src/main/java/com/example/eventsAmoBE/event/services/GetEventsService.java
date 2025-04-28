package com.example.eventsAmoBE.event.services;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import com.example.eventsAmoBE.utils.PageResponse;
import com.example.eventsAmoBE.user.model.User;
import com.example.eventsAmoBE.user.services.CurrentUserService;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GetEventsService {

    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;

    public GetEventsService(EventRepository eventRepository, CurrentUserService currentUserService) {
        this.eventRepository = eventRepository;
        this.currentUserService = currentUserService;
    }

    //@Cacheable(value = "events", key = "'allEvents_' + #pageable.pageNumber + '_' + #pageable.pageSize", cacheManager = "eventCacheManager")
    public PageResponse<EventDto> execute(Pageable pageable) {
        Page<Event> page = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable);

        Optional<User> optionalUser = currentUserService.getOptionalCurrentUser();

        List<EventDto> dtos = page.getContent().stream()
                .map(event -> optionalUser
                        .map(user -> new EventDto(event, user))
                        .orElseGet(() -> new EventDto(event, false, false)))
                .toList();

        return new PageResponse<>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.isLast()
        );
    }
}
