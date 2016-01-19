package com.test.rmmfl.service.impl;

import com.test.gdo.helper.TopicsSubscriptionRegistry;
import com.test.gdo.service.SubscriptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final Map<String, AtomicInteger> destinationCountMap = new ConcurrentHashMap<>();

    @Autowired
    TopicsSubscriptionRegistry registry;

    @Override
    public void subscribe(final String sessionId, final String subscriptionId, final String destination) {
        registry.addSubscription(sessionId, subscriptionId, destination);
        final AtomicInteger count = destinationCountMap.putIfAbsent(destination, new AtomicInteger(1));
        int current = 1;
        if (count != null) {
            current = count.incrementAndGet();
        }
        log.info("Topic '{}' has '{}' subscriptions", destination, current);
    }

    @Override
    public void unsubscribe(final String sessionId, final String subscriptionId) {
        log.info("Removing subscription '{}' for session '{}'", subscriptionId, sessionId);
        registry.removeSubscription(sessionId, subscriptionId).ifPresent(this::checkDestinationCount);
    }

    @Override
    public void destroySession(final String sessionId) {
        log.info("Destroying session '{}'", sessionId);
        registry.removeSession(sessionId).forEach(this::checkDestinationCount);
    }

    @Override
    public Set<String> getActiveTopics() {
        return destinationCountMap.keySet();
    }

    private void checkDestinationCount(final String destination) {
        if (destinationCountMap.containsKey(destination)) {
            synchronized (destinationCountMap) {
                if (destinationCountMap.containsKey(destination)) {
                    if (destinationCountMap.get(destination).decrementAndGet() < 1) {
                        log.info("Topic '{}' hasn't any subscriptions, removing", destination);
                        destinationCountMap.remove(destination);
                    }
                }
            }
        }
    }
}
