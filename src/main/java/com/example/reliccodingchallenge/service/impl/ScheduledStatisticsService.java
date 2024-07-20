package com.example.reliccodingchallenge.service.impl;

import com.example.reliccodingchallenge.service.AbstractStatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledStatisticsService extends AbstractStatisticsService {

    @Override
    @Scheduled(fixedRate = 10000)
    public void reportStatistics() {
        int uniqueCount = newUniqueNumbers.getAndSet(0);
        int duplicateCount = newDuplicateNumbers.getAndSet(0);
        int totalUnique = totalUniqueNumbers.get();

        System.out.printf(String.format("Received %d unique numbers, %d duplicates, Unique total: %d",
                uniqueCount, duplicateCount, totalUnique));
    }
}
