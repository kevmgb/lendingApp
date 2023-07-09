package com.example.lendingApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendSMS(String msisdn, String message) {
        // Stub for sending notification
        logger.info("Sending sms to msisdn " + msisdn + " with message: " + message);
    }
}
