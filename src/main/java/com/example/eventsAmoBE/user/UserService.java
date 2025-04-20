package com.example.eventsAmoBE.user;

import com.example.eventsAmoBE.event.EventRepository;
import com.example.eventsAmoBE.event.model.Event;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JavaMailSender mailSender;

    @Cacheable(value = "users", key = "#id")
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

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Only update fields that can be modified
        user.setName(userDetails.getName());
        user.setLastName(userDetails.getLastName());
        // Password is updated separately through a specific endpoint

        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
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

    public Set<Event> getSavedEvents() {
        User user = getCurrentUser();
        return user.getSavedEvents();
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

    public Set<Event> getAttendingEvents() {
        User user = getCurrentUser();
        return user.getAttendingEvents();
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public User makeUserAdmin(Long id) {
        User user = getUserById(id);
        user.setAdmin(true);
        return userRepository.save(user);
    }

    @Transactional
    public void submitEventProposal(Event event) {
        User user = getCurrentUser();

        // Send email to admin with event proposal
        try {
            sendEventProposalEmail(user, event);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send event proposal email", e);
        }
    }

    private void sendEventProposalEmail(User user, Event event) throws MessagingException {
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
        mailSender.send(message);
    }
}