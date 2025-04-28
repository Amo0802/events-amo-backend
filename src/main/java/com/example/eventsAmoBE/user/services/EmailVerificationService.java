package com.example.eventsAmoBE.user.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class EmailVerificationService {

    // Store verification codes with expiration (userId -> [code, email, expiration])
    private final Map<Long, VerificationData> verificationCodes = new ConcurrentHashMap<>();

    // Code expiration time in minutes
    private static final long EXPIRATION_MINUTES = 15;

    public String generateVerificationCode(Long userId, String newEmail) {
        // Generate a random 6-digit code
        String code = String.format("%06d", (int)(Math.random() * 1000000));

        // Store code with expiration time
        LocalDateTime expiration = LocalDateTime.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        verificationCodes.put(userId, new VerificationData(code, newEmail, expiration));

        return code;
    }

    public boolean verifyCode(Long userId, String code) {
        VerificationData data = verificationCodes.get(userId);

        if (data == null) {
            return false; // No code found for this user
        }

        if (LocalDateTime.now().isAfter(data.getExpiration())) {
            verificationCodes.remove(userId); // Code expired
            return false;
        }

        if (!data.getCode().equals(code)) {
            return false; // Incorrect code
        }

        // Code is valid
        String newEmail = data.getNewEmail();
        verificationCodes.remove(userId); // Remove used code

        return true;
    }

    public String getPendingEmail(Long userId) {
        VerificationData data = verificationCodes.get(userId);
        return data != null ? data.getNewEmail() : null;
    }

    // Inner class to store verification data
    private static class VerificationData {
        private final String code;
        private final String newEmail;
        private final LocalDateTime expiration;

        public VerificationData(String code, String newEmail, LocalDateTime expiration) {
            this.code = code;
            this.newEmail = newEmail;
            this.expiration = expiration;
        }

        public String getCode() {
            return code;
        }

        public String getNewEmail() {
            return newEmail;
        }

        public LocalDateTime getExpiration() {
            return expiration;
        }
    }
}