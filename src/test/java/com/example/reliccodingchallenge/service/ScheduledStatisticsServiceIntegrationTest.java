package com.example.reliccodingchallenge.service;


import com.example.reliccodingchallenge.service.impl.ScheduledStatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableScheduling
public class ScheduledStatisticsServiceIntegrationTest {

    @SpyBean
    private ScheduledStatisticsService scheduledStatisticsService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Test
    public void testScheduledReportStatistics() {

        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(scheduledStatisticsService, atLeast(1)).reportStatistics();
        });

    }


}
