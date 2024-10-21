package com.backwoodslabs.backwoods_crm_api.service;

import com.backwoodslabs.backwoods_crm_api.model.EmailVerificationToken;
import com.backwoodslabs.backwoods_crm_api.model.User;
import com.backwoodslabs.backwoods_crm_api.repository.EmailVerificationTokenRepository;
import com.backwoodslabs.backwoods_crm_api.repository.UserRepository;
import jakarta.mail.MessagingException;
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

    public void handleUserEmailVerificationToken(User user, String siteURL) throws MessagingException {
        EmailVerificationToken verificationToken = new EmailVerificationToken(user);
        tokenRepository.save(verificationToken);

        String verificationLink = siteURL + "/api/verify-email?token=" + verificationToken.getToken();
        String subject = "Confirme seu endereço de e-mail";
        String content = "<div style=\"font-family: Arial, sans-serif; color: #333; line-height: 1.6;\">"
                + "<h2 style=\"color: #4CAF50;\">Bem-vindo ao Backwoods CRM!</h2>"
                + "<p>Olá " + user.getUsername() + ",</p>"
                + "<p>Obrigado por se registrar conosco. Para completar seu cadastro, por favor, confirme seu endereço de e-mail clicando no botão abaixo:</p>"
                + "<p style=\"text-align: center;\">"
                + "<a href=\"" + verificationLink + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 5px;\">Verificar E-mail</a>"
                + "</p>"
                + "<p>Se o botão acima não funcionar, copie e cole o seguinte link no seu navegador:</p>"
                + "<p style=\"word-break: break-all;\"><a href=\"" + verificationLink + "\" style=\"color: #4CAF50;\">" + verificationLink + "</a></p>"
                + "<p><strong>Nota:</strong> Este link de verificação expirará em 24 horas.</p>"
                + "<p>Se você não se registrou conosco, pode ignorar este e-mail com segurança.</p>"
                + "<p>Atenciosamente,<br>Equipe Backwoods CRM</p>"
                + "</div>";

        emailService.sendVerificationEmail(user.getEmail(), subject, content);
    }
}
