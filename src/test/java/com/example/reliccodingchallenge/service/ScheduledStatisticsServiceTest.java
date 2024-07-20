package com.example.reliccodingchallenge.service;

import com.example.reliccodingchallenge.service.impl.ScheduledStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
        assertEquals(1, (int) ReflectionTestUtils.getField(statisticsService, "totalUniqueNumbers"));
        assertEquals(1, (int) ReflectionTestUtils.getField(statisticsService, "newUniqueNumbers"));

    }

    @Test
    public void testIncrementDuplicate() {

        statisticsService.incrementDuplicate();
        assertEquals(1, (int) ReflectionTestUtils.getField(statisticsService, "newDuplicateNumbers"));

    }

    @Test
    public void testReportStatistics() {

        statisticsService.incrementUnique();
        statisticsService.incrementDuplicate();
        statisticsService.reportStatistics();

        assertEquals(0, (int) ReflectionTestUtils.getField(statisticsService, "newUniqueNumbers"));
        assertEquals(0, (int) ReflectionTestUtils.getField(statisticsService, "newDuplicateNumbers"));

    }
}
