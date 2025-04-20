package com.example.eventsAmoBE.user;

import com.example.eventsAmoBE.event.model.Event;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventNotificationService {

    private final JavaMailSender emailSender;

    @Value("${admin.email}")
    private String adminEmail;

    public void sendEventProposalNotification(User user, Event proposedEvent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(adminEmail);
        helper.setSubject("New Event Proposal: " + proposedEvent.getName());

        String emailContent = buildEventProposalEmailContent(user, proposedEvent);
        helper.setText(emailContent, true); // true indicates HTML content

        emailSender.send(message);
    }

    private String buildEventProposalEmailContent(User user, Event event) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append("<h2>New Event Proposal</h2>");
        builder.append("<p><strong>Submitted by:</strong> ")
                .append(user.getName()).append(" ").append(user.getLastName())
                .append(" (").append(user.getEmail()).append(")</p>");

        builder.append("<h3>Event Details:</h3>");
        builder.append("<p><strong>Name:</strong> ").append(event.getName()).append("</p>");
        builder.append("<p><strong>Description:</strong> ").append(event.getDescription()).append("</p>");
        builder.append("<p><strong>City:</strong> ").append(event.getCity()).append("</p>");
        builder.append("<p><strong>Date & Time:</strong> ").append(event.getStartDateTime()).append("</p>");
        builder.append("<p><strong>Price:</strong> ").append(event.getPrice()).append("</p>");
        builder.append("<p><strong>Categories:</strong> ").append(event.getCategories()).append("</p>");

        builder.append("<p>Please review this event proposal in the admin panel.</p>");
        builder.append("</body></html>");

        return builder.toString();
    }

    public void sendEventCreationConfirmation(User admin, Event createdEvent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(admin.getEmail());
        helper.setSubject("Event Created: " + createdEvent.getName());

        String emailContent = "<html><body>" +
                "<h2>Event Successfully Created</h2>" +
                "<p>The following event has been added to the system:</p>" +
                "<p><strong>Name:</strong> " + createdEvent.getName() + "</p>" +
                "<p><strong>Date & Time:</strong> " + createdEvent.getStartDateTime() + "</p>" +
                "<p><strong>City:</strong> " + createdEvent.getCity() + "</p>" +
                "</body></html>";

        helper.setText(emailContent, true);
        emailSender.send(message);
    }
}