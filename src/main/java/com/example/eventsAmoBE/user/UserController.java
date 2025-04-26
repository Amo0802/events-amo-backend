package com.example.eventsAmoBE.user;

//import com.example.eventsAmoBE.event.model.City;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import com.example.eventsAmoBE.event.model.EventRequestDTO;
import com.example.eventsAmoBE.user.model.User;
import com.example.eventsAmoBE.user.model.UserDto;
import com.example.eventsAmoBE.user.services.*;
import com.example.eventsAmoBE.utils.EventMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserByIdService userByIdService;
    private final SaveEventService saveEventService;
    private final AttendEventService attendEventService;
    private final SubmitEventService submitEventService;
    private final EventMapper eventMapper;

    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserDto(userByIdService.getUserById(id)));
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(new UserDto(currentUserService.getCurrentUser()));
    }

    @DeleteMapping("/current")
    @Transactional
    public ResponseEntity<Void> deleteCurrentUser() {
        currentUserService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(new UserDto(userByIdService.updateUserById(id, user)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userByIdService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Event saving functionality
    @PostMapping("/save-event/{eventId}")
    public ResponseEntity<Void> saveEvent(@PathVariable Long eventId) {
        saveEventService.saveEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unsave-event/{eventId}")
    public ResponseEntity<Void> unsaveEvent(@PathVariable Long eventId) {
        saveEventService.unsaveEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saved-events")
    public ResponseEntity<Set<EventDto>> getSavedEvents() {
        return ResponseEntity.ok(saveEventService.getSavedEvents());
    }

    // Event attending functionalities
    @PostMapping("/attend-event/{eventId}")
    public ResponseEntity<Void> attendEvent(@PathVariable Long eventId) {
        attendEventService.attendEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unattend-event/{eventId}")
    public ResponseEntity<Void> unattendEvent(@PathVariable Long eventId) {
        attendEventService.unattendEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attending-events")
    public ResponseEntity<Set<EventDto>> getAttendingEvents() {
        return ResponseEntity.ok(attendEventService.getAttendingEvents());
    }

    // For admin use only - to make another user an admin
    @PutMapping("/make-admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new UserDto(userByIdService.makeUserAdmin(id)));
    }

    @PostMapping(value = "/submit-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Void> submitEvent(
            @RequestPart("event") EventRequestDTO eventDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        User user = currentUserService.getCurrentUser();

        Event event = eventMapper.fromDto(eventDto);

        submitEventService.submitEventProposal(user, event, images);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/city")
//    public ResponseEntity<City> getCity(){
//        return ResponseEntity.ok(userService.getCity());
//    }
}