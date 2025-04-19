package com.example.eventsAmoBE.event;

import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.services.*;
import com.example.eventsAmoBE.event.utils.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class EventController {

    private final CreateEventService createEventService;
    private final DeleteEventService deleteEventService;
    private final GetEventsByCategoryService getEventsByCategoryService;
    private final GetEventService getEventService;
    private final GetEventsService getEventsService;
    private final GetMainEventsService getMainEventsService;
    private final GetPromotedEventsService getPromotedEventsService;
    private final SearchEventService searchEventService;
    private final UpdateEventService updateEventService;

    public EventController(CreateEventService createEventService, DeleteEventService deleteEventService, GetEventsByCategoryService getEventsByCategoryService, GetEventService getEventService, GetEventsService getEventsService, GetMainEventsService getMainEventsService, GetPromotedEventsService getPromotedEventsService, SearchEventService searchEventService, UpdateEventService updateEventService) {
        this.createEventService = createEventService;
        this.deleteEventService = deleteEventService;
        this.getEventsByCategoryService = getEventsByCategoryService;
        this.getEventService = getEventService;
        this.getEventsService = getEventsService;
        this.getMainEventsService = getMainEventsService;
        this.getPromotedEventsService = getPromotedEventsService;
        this.searchEventService = searchEventService;
        this.updateEventService = updateEventService;
    }

    @PostMapping("/event")
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        Event response = createEventService.execute(event);
        URI location = URI.create("/event/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        deleteEventService.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("event/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id){
        Event response = getEventService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event){
        Event response = updateEventService.execute(id, event);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/events")
    public ResponseEntity<PageResponse<Event>> getEvents(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Event> response = getEventsService.execute(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/category")
    public ResponseEntity<PageResponse<Event>> getEventsByCategory(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
            ){
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Event> response = getEventsByCategoryService.execute(pageable, category);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/main")
    public ResponseEntity<PageResponse<Event>> getMainEvents(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Event> response = getMainEventsService.execute(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/promoted")
    public ResponseEntity<PageResponse<Event>> getPromotedEvents(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Event> response = getPromotedEventsService.execute(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/search")
    public ResponseEntity<PageResponse<Event>> searchEvents(
            @RequestParam String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Event> response = searchEventService.execute(search, pageable);

        return ResponseEntity.ok(response);
    }
}
