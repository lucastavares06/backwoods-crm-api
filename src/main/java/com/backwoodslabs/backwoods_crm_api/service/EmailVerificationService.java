package com.backwoodslabs.backwoods_crm_api.service;

import com.backwoodslabs.backwoods_crm_api.model.EmailVerificationToken;
import com.backwoodslabs.backwoods_crm_api.model.User;
import com.backwoodslabs.backwoods_crm_api.repository.EmailVerificationTokenRepository;
import com.backwoodslabs.backwoods_crm_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public boolean verifyUser(String token) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent() && optionalToken.get().getExpiryDate().after(new Date())) {
            User user = optionalToken.get().getUser();
            user.setEmailVerified(true);
            userRepository.save(user);
            tokenRepository.delete(optionalToken.get());
            return true;
        }
        return false;
    }

    public void handleUserEmailVerificationToken(User user, String siteURL) {
        try {
            EmailVerificationToken verificationToken = new EmailVerificationToken(user);
            tokenRepository.save(verificationToken);

            String verificationLink = siteURL + "/api/verify-email?token=" + verificationToken.getToken();
            String subject = "Confirm your email address";
            String content = "<div style=\"font-family: Arial, sans-serif; color: #333; line-height: 1.6;\">"
                    + "<h2 style=\"color: #4CAF50;\">Welcome to Backwoods CRM!</h2>"
                    + "<p>Hello " + user.getUsername() + ",</p>"
                    + "<p>Thank you for registering with us. To complete your registration, please confirm your email address by clicking the button below:</p>"
                    + "<p style=\"text-align: center;\">"
                    + "<a href=\"" + verificationLink + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 5px;\">Verify Email</a>"
                    + "</p>"
                    + "<p>If the button above does not work, copy and paste the following link into your browser:</p>"
                    + "<p style=\"word-break: break-all;\"><a href=\"" + verificationLink + "\" style=\"color: #4CAF50;\">" + verificationLink + "</a></p>"
                    + "<p><strong>Note:</strong> This verification link will expire in 24 hours.</p>"
                    + "<p>If you did not register with us, you can safely ignore this email.</p>"
                    + "<p>Best regards,<br>Backwoods CRM Team</p>"
                    + "</div>";

            emailService.sendVerificationEmail(user.getEmail(), subject, content);
        } catch (Exception e) {
            throw new RuntimeException("Error generating or sending the verification email", e);
        }
    }
}
