package com.test.rmmfl.helper;


import com.test.rmmfl.service.SubscriptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@Slf4j
@Component
public class TopicEventListener implements ApplicationListener<AbstractSubProtocolEvent> {


    @Autowired
    SubscriptionsService subscriptionsService;

    @Override
    public void onApplicationEvent(final AbstractSubProtocolEvent event) {

        log.debug("Received event {}", event);
        final MessageHeaders headers = event.getMessage().getHeaders();
        final StompCommand stompCommand = headers.get("stompCommand", StompCommand.class);
        if (stompCommand == null) {
            return;
        }
        switch (stompCommand) {
            case DISCONNECT:
                handleDisconnectEvent(headers);
                break;
            case SUBSCRIBE:
                handleSubscribeEvent(headers);
                break;
            case UNSUBSCRIBE:
                handleUnsubscribeEvent(headers);
                break;
            default:
                log.trace("Ignoring event");
        }
    }

    private void handleUnsubscribeEvent(final MessageHeaders headers) {
        final String sessionId = getSessionId(headers);
        final String subscriptionId = getSubscriptionId(headers);
        subscriptionsService.unsubscribe(sessionId, subscriptionId);
    }

    private void handleSubscribeEvent(final MessageHeaders headers) {
        final String destination = getDestination(headers);
        final String sessionId = getSessionId(headers);
        final String subscriptionId = getSubscriptionId(headers);
        subscriptionsService.subscribe(sessionId, subscriptionId, destination);
    }

    private void handleDisconnectEvent(final MessageHeaders headers) {
        subscriptionsService.destroySession(getSessionId(headers));
    }

    private String getSessionId(final MessageHeaders headers) {
        return headers.get("simpSessionId").toString();
    }

    private String getDestination(final MessageHeaders headers) {
        return headers.get("simpDestination").toString();
    }

    private String getSubscriptionId(final MessageHeaders headers) {
        return headers.get("simpSubscriptionId").toString();
    }
}
