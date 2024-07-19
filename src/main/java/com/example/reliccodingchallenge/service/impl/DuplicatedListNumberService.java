package com.example.reliccodingchallenge.service.impl;

import com.example.reliccodingchallenge.service.NumberService;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DuplicatedListNumberService implements NumberService {

    private final Set<String> uniqueNumbers = new HashSet<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    @Async
    public void processNumber(String number) {
        synchronized (uniqueNumbers) {
            uniqueNumbers.add(number);
        }
        executorService.submit(this::writeToFile);
    }

    @Override
    public synchronized void writeToFile() {
        synchronized (uniqueNumbers) {
            try(FileWriter writer = new FileWriter("numbers.log")) {
                for(String number : uniqueNumbers) {
                    writer.write(number + System.lineSeparator());
                }
                uniqueNumbers.clear();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }
}
