package org.aegrotatio.sole.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public Producer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void forwardRegistrationId(String registrationSignature, Long registrationId) {
        String message = registrationSignature + ":" + registrationId;
        kafkaTemplate.send("registration-response", message);
    }

    public void forwardRegistrationNotFound(String registrationSignature) {
        kafkaTemplate.send("registration-error", "registration-not-found:" + registrationSignature);
    }

    public void forwardProcessingError(String registrationSignature, String errorMessage) {
        kafkaTemplate.send("registration-error", "processing-error:" + registrationSignature);
    }
}