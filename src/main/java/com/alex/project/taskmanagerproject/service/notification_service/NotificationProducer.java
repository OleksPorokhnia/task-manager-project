package com.alex.project.taskmanagerproject.service.notification_service;

import com.alex.project.taskmanagerproject.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final String topic;

    public NotificationProducer(KafkaTemplate<String, Message> kafkaTemplate, @Value("${topic.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendNotification(Message message) {
        kafkaTemplate.send(topic, message);
    }
}
