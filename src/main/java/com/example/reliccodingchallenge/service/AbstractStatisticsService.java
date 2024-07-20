package com.example.reliccodingchallenge.service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */
public abstract class AbstractStatisticsService implements StatisticsService {

    protected final AtomicInteger totalUniqueNumbers = new AtomicInteger(0);
    protected final AtomicInteger newUniqueNumbers = new AtomicInteger(0);
    protected final AtomicInteger newDuplicateNumbers = new AtomicInteger(0);

    @Override
    public void incrementUnique() {
        newUniqueNumbers.incrementAndGet();
        totalUniqueNumbers.incrementAndGet();
    }

    @Override
    public void incrementDuplicate() {
        newDuplicateNumbers.incrementAndGet();
    }

    @Override
    public abstract void reportStatistics();

}
