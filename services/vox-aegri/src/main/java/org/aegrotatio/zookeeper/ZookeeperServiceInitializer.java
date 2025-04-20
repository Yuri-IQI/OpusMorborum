package org.aegrotatio.zookeeper;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ZookeeperServiceInitializer {

    @ConfigProperty(name = "quarkus.http.port")
    int appPort;

    @Inject
    ZookeeperRegistry registry;

    public void onStart(@Observes StartupEvent ev) {
        registry.registerService("vox-aegri", "127.0.0.1", appPort);
    }
}