package com.test.rmmfl.service.impl;

import com.test.gdo.helper.ReportPeriod;
import com.test.gdo.service.NotifyService;
import com.test.gdo.service.SubscriptionsService;
import com.test.gdo.web.dto.TickDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.math.BigInteger.valueOf;

@Slf4j
@Component
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    SubscriptionsService subscriptionsService;
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<String> validPeriods = Arrays.stream(ReportPeriod.values()).map(Enum::toString).collect(Collectors.toSet());

    @Override
    @Scheduled(fixedRateString = "${gdo.websocket.push.interval:3000}")
    public void notifySubscribers() {
        log.trace("Pushing to topics random data");

        for (String topic : subscriptionsService.getActiveTopics()) {
            final String period = getPeriod(topic);
            if (!validPeriods.contains(period)) {
                log.debug("Invalid topic period '{}'", period);
                return;
            }
            final TickDto row = TickDto.builder()
                    .actions(valueOf(random.nextLong(Long.MAX_VALUE)))
                    .impressions(valueOf(random.nextLong(Long.MAX_VALUE)))
                    .clicks(valueOf(random.nextLong(Long.MAX_VALUE)))
                    .market("us")
                    .period(ReportPeriod.valueOf(period))
                    .priorPeriodData(TickDto.PriorPeriodData.builder()
                            .actions(valueOf(random.nextLong(Long.MAX_VALUE)))
                            .impressions(valueOf(random.nextLong(Long.MAX_VALUE)))
                            .clicks(valueOf(random.nextLong(Long.MAX_VALUE))).build())
                    .build();
            log.trace("Sending random data to topic '{}'", topic);
            messagingTemplate.convertAndSend(topic, row);
        }
    }

    private String getPeriod(final String topic) {
        return topic.substring(7, 10);
    }
}
