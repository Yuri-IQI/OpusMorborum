package org.aegrotatio.sole.kafka;

import org.aegrotatio.sole.exceptions.RegistrationNotFoundException;
import org.aegrotatio.sole.services.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private final RegistrationService registrationService;
    private final Producer producer;
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public Consumer(RegistrationService registrationService, Producer producer) {
        this.registrationService = registrationService;
        this.producer = producer;
    }

    @KafkaListener(topics = "registration-request", groupId = "registration-group")
    public void listenForRegistrationRequests(String registrationSignature) {
        try {
            Long registrationId = registrationService.verifyRegistration(registrationSignature);
            producer.forwardRegistrationId(registrationSignature, registrationId);
        } catch (RegistrationNotFoundException e) {
            logger.warn("Registration not found: {}", registrationSignature);
            producer.forwardRegistrationNotFound(registrationSignature);
        } catch (Exception e) {
            logger.error("Unexpected error processing registration: {}", registrationSignature, e);
            producer.forwardProcessingError(registrationSignature, e.getMessage());
        }
    }
}