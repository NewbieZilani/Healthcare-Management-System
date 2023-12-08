package com.moin.Notification.service;

import com.moin.Notification.exception.CustomException;

public interface EmailNotificationService {
    void sendEmail(String emailTo, String subject, String messageBody) throws CustomException;
}
