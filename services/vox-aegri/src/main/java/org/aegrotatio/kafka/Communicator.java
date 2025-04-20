package org.aegrotatio.kafka;


import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class Communicator {

    @Inject
    @Channel("registration-request-out")
    Emitter<String> emitter;

    private final Map<String, CompletableFuture<Long>> pendingRequests = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Communicator.class);

    public Uni<Long> askForRegistrationId(String registration) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        pendingRequests.put(registration, future);
        emitter.send(registration);

        return Uni.createFrom().completionStage(future)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @Incoming("registration-response-in")
    public void receiveRegistrationId(String registrationResponse) {
        String[] parts = registrationResponse.split(":");
        if (parts.length == 2) {
            String registration = parts[0];
            Long registrationId = Long.valueOf(parts[1]);

            CompletableFuture<Long> future = pendingRequests.remove(registration);

            if (future != null) {
                future.complete(registrationId);
            }
        }
    }

    @Incoming("registration-error-in")
    public void receiveRegistrationError(String registrationError) {
        logger.error("Received registration error: {}", registrationError);
    }
}