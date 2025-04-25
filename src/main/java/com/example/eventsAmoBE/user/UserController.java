package com.example.eventsAmoBE.user;

import com.example.eventsAmoBE.event.model.Category;
import com.example.eventsAmoBE.event.model.City;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserDto(userService.getUserById(id)));
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(new UserDto(userService.getCurrentUser()));
    }

    @DeleteMapping("/current")
    @Transactional
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(new UserDto(userService.updateUser(id, user)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Event saving functionality
    @PostMapping("/save-event/{eventId}")
    public ResponseEntity<Void> saveEvent(@PathVariable Long eventId) {
        userService.saveEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unsave-event/{eventId}")
    public ResponseEntity<Void> unsaveEvent(@PathVariable Long eventId) {
        userService.unsaveEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saved-events")
    public ResponseEntity<Set<EventDto>> getSavedEvents() {
        return ResponseEntity.ok(userService.getSavedEvents());
    }

    // Event attending functionalities
    @PostMapping("/attend-event/{eventId}")
    public ResponseEntity<Void> attendEvent(@PathVariable Long eventId) {
        userService.attendEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unattend-event/{eventId}")
    public ResponseEntity<Void> unattendEvent(@PathVariable Long eventId) {
        userService.unattendEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attending-events")
    public ResponseEntity<Set<EventDto>> getAttendingEvents() {
        return ResponseEntity.ok(userService.getAttendingEvents());
    }

    // For admin use only - to make another user an admin
    @PutMapping("/make-admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new UserDto(userService.makeUserAdmin(id)));
    }

    @PostMapping("/submit-event")
    public ResponseEntity<Void> submitEvent(
            @RequestPart("name") String name,
            @RequestPart("description") String description,
            @RequestPart("address") String address,
            @RequestPart("startDateTime") String startDateTime,
            @RequestPart("price") String price,
            @RequestPart("categories") List<String> categories,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // Convert string parameters to Event object
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setAddress(address);
        event.setStartDateTime(LocalDateTime.parse(startDateTime));
        event.setPrice(Double.parseDouble(price));

        // Convert string categories to enum Category
        Set<Category> categorySet = new HashSet<>();
        for (String category : categories) {
            categorySet.add(Category.valueOf(category));
        }
        event.setCategories(categorySet);

        userService.submitEventProposal(event, images);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/city")
//    public ResponseEntity<City> getCity(){
//        return ResponseEntity.ok(userService.getCity());
//    }
}