package com.test.rmmfl.helper;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TopicsSubscriptionRegistry {

    private final Map<String, SubscriptionInfo> sessions = new ConcurrentHashMap<>();

    public void addSubscription(final String sessionId, final String subscriptionId, final String destination) {
        SubscriptionInfo info = this.sessions.get(sessionId);
        if (info == null) {
            info = new SubscriptionInfo(sessionId);
            final SubscriptionInfo val = this.sessions.putIfAbsent(sessionId, info);
            if (val != null) {
                info = val;
            }
        }
        info.addSubscription(subscriptionId, destination);
    }

    public Optional<String> removeSubscription(final String sessionId, final String subscriptionId) {
        final SubscriptionInfo info = sessions.get(sessionId);
        if (info != null) {
            return Optional.ofNullable(info.removeSubscription(subscriptionId));
        }
        return Optional.empty();
    }

    public Set<String> removeSession(final String sessionId) {
        final SubscriptionInfo info = sessions.get(sessionId);
        if (info == null) {
            return Collections.emptySet();
        }
        final HashSet<String> destinations = new HashSet<>(info.subscriptionIdDestinationMap.values());
        sessions.remove(sessionId);
        return Collections.unmodifiableSet(destinations);
    }

    private static class SubscriptionInfo {

        private final String sessionId;
        private final Map<String, String> subscriptionIdDestinationMap = new ConcurrentHashMap<>();

        private SubscriptionInfo(final String sessionId) {
            this.sessionId = sessionId;
        }

        void addSubscription(final String subId, final String destination) {
            subscriptionIdDestinationMap.put(subId, destination);
        }

        String removeSubscription(final String subId) {
            return subscriptionIdDestinationMap.remove(subId);
        }
    }
}
