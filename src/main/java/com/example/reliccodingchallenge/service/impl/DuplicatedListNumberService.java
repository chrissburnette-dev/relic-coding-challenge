package com.example.reliccodingchallenge.service.impl;

import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.NumberService;
import com.example.reliccodingchallenge.service.StatisticsService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DuplicatedListNumberService implements NumberService {

    private final StatisticsService statisticsService;
    private final Set<String> uniqueNumbers = new HashSet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static final String FILE_PATH = "./resources/static/numbers.log";
    private static final Logger logger = LoggerFactory.getLogger(DuplicatedListNumberService.class);

    public DuplicatedListNumberService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostConstruct
    private void init() {
        try {
            final Path numbersFilePath = Paths.get(FILE_PATH);
            Files.deleteIfExists(numbersFilePath);
            Files.createFile(numbersFilePath);
            logger.info("Log info created at {}", FILE_PATH);
        }
        catch(IOException e) {
            logger.error("Error initializing log file", e);
        }
    }

    @Override
    public ConfirmationResponse handleNumberRequest(NumberRequest request, SimpMessageHeaderAccessor messageHeaderAccessor) {
        String number = request.number();

        if(number.equalsIgnoreCase("Terminate")) {
            logger.info("Received termination command.");
            terminateApplication();
            return new ConfirmationResponse("Application terminating..");
        }
        if(!number.matches("\\d{9}")) {
            logger.warn("Invalid number received: {}", number);
            closeConnection(messageHeaderAccessor);
            return new ConfirmationResponse("Invalid number.");
        }

        processNumber(number);
        return new ConfirmationResponse("Number accepted.");
    }

    @Async
    protected void processNumber(String number) {
        synchronized (uniqueNumbers) {
           if(uniqueNumbers.add(number)) {
               executorService.submit(() -> writeToFile(number));
               statisticsService.incrementUnique();
               logger.info("Processed unique number: {}", number);
           }
           else {
               statisticsService.incrementDuplicate();
               logger.info("Duplicate number received: {}", number);
           }
        }
    }

    private void writeToFile(String number) {
        try(FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(number + System.lineSeparator());
            logger.info("Number written to file: {}", number);
        } catch (IOException e) {
            logger.error("Error writing to log file", e);
        }
    }
    @PreDestroy
    private void shutdown() {
        executorService.shutdown();
        logger.info("Executor service shut down");
    }
    private void terminateApplication() {
        shutdown();
        System.exit(0);
    }

    private void closeConnection(SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("close", true);
        logger.info("Connection closed due to invalid input.");
    }

}
