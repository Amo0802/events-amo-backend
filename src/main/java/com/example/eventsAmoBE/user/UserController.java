package com.example.eventsAmoBE.user;

import com.example.eventsAmoBE.event.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
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
    public ResponseEntity<Set<Event>> getSavedEvents() {
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
    public ResponseEntity<Set<Event>> getAttendingEvents() {
        return ResponseEntity.ok(userService.getAttendingEvents());
    }

    // For admin use only - to make another user an admin
    @PutMapping("/make-admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> makeAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(userService.makeUserAdmin(id));
    }

    // For admin use only - user event submission
    @PostMapping("/submit-event")
    public ResponseEntity<Void> submitEvent(@RequestBody Event event) {
        userService.submitEventProposal(event);
        return ResponseEntity.ok().build();
    }
}