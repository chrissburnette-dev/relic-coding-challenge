package com.example.reliccodingchallenge.service;

import com.example.reliccodingchallenge.service.impl.ScheduledStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduledStatisticsServiceTest {

    private ScheduledStatisticsService statisticsService;

    @BeforeEach
    public void setUp() {
        statisticsService = new ScheduledStatisticsService();
    }

    @Test
    public void testIncrementUnique() {

        statisticsService.incrementUnique();
        assertEquals(1, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "totalUniqueNumbers")).get());
        assertEquals(1, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newUniqueNumbers")).get());

    }

    @Test
    public void testIncrementDuplicate() {

        statisticsService.incrementDuplicate();
        assertEquals(1, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newDuplicateNumbers")).get());

    }

    @Test
    public void testReportStatistics() {

        statisticsService.incrementUnique();
        statisticsService.incrementDuplicate();

        //Check the values before calling reportStatistics. Due to getAndSet() .
        assertEquals(1, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newUniqueNumbers")).get());
        assertEquals(1, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newDuplicateNumbers")).get());

        statisticsService.reportStatistics();

        assertEquals(0, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newUniqueNumbers")).get());
        assertEquals(0, ((AtomicInteger) ReflectionTestUtils.getField(statisticsService, "newDuplicateNumbers")).get());

    }
}
