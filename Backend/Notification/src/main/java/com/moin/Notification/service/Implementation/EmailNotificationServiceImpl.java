package com.moin.Notification.service.Implementation;

import com.moin.Notification.exception.CustomException;
import com.moin.Notification.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String emailTo, String subject, String messageBody) throws CustomException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(messageBody);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CustomException("Error sending email");
        }
    }
}
