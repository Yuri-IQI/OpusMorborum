package org.aegrotatio.zookeeper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.UUID;

@ApplicationScoped
public class ZookeeperRegistry {

    private final ServiceDiscovery<Void> serviceDiscovery;

    public ZookeeperRegistry(CuratorFramework client) throws Exception {
        JsonInstanceSerializer<Void> serializer = new JsonInstanceSerializer<>(Void.class);

        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .client(client)
                .basePath("/services")
                .serializer(serializer)
                .build();

        this.serviceDiscovery.start();
    }

    public void registerService(String serviceName, String address, int port) {
        try {
            ServiceInstance<Void> instance = ServiceInstance.<Void>builder()
                    .name(serviceName)
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(address)
                    .port(port)
                    .id(UUID.randomUUID().toString())
                    .build();

            serviceDiscovery.registerService(instance);
            System.out.println("Registered service: " + serviceName + " at " + address + ":" + port);
        } catch (Exception e) {
            Log.errorf("Failed to register service: %s", serviceName);
        }
    }
}