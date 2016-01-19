package com.test.rmmfl.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface SubscriptionsService {
    void subscribe(final String sessionId, final String subscriptionId, final String destination);
    void unsubscribe(final String sessionId, final String subscriptionId);
    void destroySession(final String sessionId);
    Set<String> getActiveTopics();
}
