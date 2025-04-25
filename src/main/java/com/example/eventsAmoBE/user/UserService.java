package com.example.eventsAmoBE.user;

import com.example.eventsAmoBE.event.EventRepository;
//import com.example.eventsAmoBE.event.model.City;
import com.example.eventsAmoBE.event.model.Event;
import com.example.eventsAmoBE.event.model.EventDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JavaMailSender mailSender;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public void deleteCurrentUser() {
        userRepository.delete(getCurrentUser());
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Only update fields that can be modified
        user.setName(userDetails.getName());
        user.setLastName(userDetails.getLastName());
        // Password is updated separately through a specific endpoint

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Transactional
    public void saveEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getSavedEvents().add(event);
        userRepository.save(user);
    }

    @Transactional
    public void unsaveEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getSavedEvents().remove(event);
        userRepository.save(user);
    }

    public Set<EventDto> getSavedEvents() {
        User user = getCurrentUser();

        return user.getSavedEvents().stream()
                .map(event -> new EventDto(event, user))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void attendEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getAttendingEvents().add(event);
        event.getAttendees().add(user);

        userRepository.save(user);
        eventRepository.save(event);
    }

    @Transactional
    public void unattendEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        user.getAttendingEvents().remove(event);
        event.getAttendees().remove(user);

        userRepository.save(user);
        eventRepository.save(event);
    }

    public Set<EventDto> getAttendingEvents() {
        User user = getCurrentUser();
        return user.getAttendingEvents().stream()
                .map(event -> new EventDto(event, user))
                .collect(Collectors.toSet());
    }

    @Transactional
    public User makeUserAdmin(Long id) {
        User user = getUserById(id);
        user.setAdmin(true);
        return userRepository.save(user);
    }

    public void submitEventProposal(Event event, List<MultipartFile> images)  {
        User user = getCurrentUser();

        // Send email to admin with event proposal
        try {
            sendEventProposalEmail(user, event, images);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send event proposal email", e);
        }
    }

    private void sendEventProposalEmail(User user, Event event, List<MultipartFile> images) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("arminramusovic11@gmail.com"); // Replace with your admin email
        helper.setSubject("New Event Proposal");

        String content = String.format(
                "Event Proposal from: %s %s (%s)\n\n" +
                        "Event Details:\n" +
                        "Name: %s\n" +
                        "Description: %s\n" +
                        "City: %s\n" +
                        "Date & Time: %s\n" +
                        "Price: %s\n" +
                        "Categories: %s\n",
                user.getName(), user.getLastName(), user.getEmail(),
                event.getName(), event.getDescription(), event.getCity(),
                event.getStartDateTime(), event.getPrice(), event.getCategories()
        );

        helper.setText(content);

        // Add image attachments
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                try {
                    MultipartFile image = images.get(i);
                    String originalFilename = image.getOriginalFilename();
                    String extension = originalFilename != null ?
                            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                    String filename = "event-image-" + (i+1) + "-" + System.currentTimeMillis() + extension;
                    helper.addAttachment(filename, new ByteArrayResource(image.getBytes()));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process image attachment", e);
                }
            }
        }

        mailSender.send(message);
    }

//    public City getCity(){
//        User user = getCurrentUser();
//        return user.getCity();
//    }
}