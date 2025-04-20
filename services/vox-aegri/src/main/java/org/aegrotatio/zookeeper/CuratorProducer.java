package org.aegrotatio.zookeeper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CuratorProducer {

    @ConfigProperty(name = "zookeeper.address")
    String zookeeperAddress;

    @Produces
    @ApplicationScoped
    public CuratorFramework createCuratorClient() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zookeeperAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        return client;
    }
}