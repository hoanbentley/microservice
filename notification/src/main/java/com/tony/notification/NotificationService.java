package com.tony.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send() {
        notificationRepository.save(
                Notification.builder()
                        .toCustomerId(0)
                        .toCustomerEmail("")
                        .sender("tonycode")
                        .message("")
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}
