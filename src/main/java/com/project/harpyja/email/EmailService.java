package com.project.harpyja.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.api-key}")
    private String resendApiKey;

    @Value("${app.host-domain}")
    private String hostDomain;

    @Value("${email.from}")
    private String emailFrom;

    public void sendVerificationEmail(String to, String token) throws ResendException {
        String verificationLink = String.format("%s/verify?token=%s", hostDomain, token);

        String htmlContent = String.format("""
                <div style="font-family: Arial, sans-serif; line-height: 1.5;">
                    <h2>Please verify your email address</h2>
                    <p>Hello,</p>
                    <p>Click the button below to verify your email address:</p>
                    <a href="%s" style="display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #007BFF; border-radius: 5px; text-decoration: none;">Verify email address</a>
                    <p>Thank you!</p>
                    <p>Your Harpyja team</p>
                </div>""", verificationLink);

        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(emailFrom)
                .to(to)
                .subject("Please verify your email address")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email sent successfully with ID: " + data.getId());
        } catch (ResendException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e; // Re-throw para ser tratado no controller
        }
    }
}