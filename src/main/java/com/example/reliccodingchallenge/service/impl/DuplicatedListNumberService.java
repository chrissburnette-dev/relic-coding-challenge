package com.example.reliccodingchallenge.service.impl;

import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.NumberService;
import com.example.reliccodingchallenge.service.StatisticsService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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

    public DuplicatedListNumberService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }


    @PostConstruct
    private void init() {
        try {
            final Path numbersFilePath = Paths.get(FILE_PATH);
            Files.deleteIfExists(numbersFilePath);
            Files.createFile(numbersFilePath);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConfirmationResponse handleNumberRequest(NumberRequest request, SimpMessageHeaderAccessor messageHeaderAccessor) {
        String number = request.number();

        if("terminate".equals(number)) {
            terminateApplication();
        }
        if(!number.matches("\\d{9}")) {
            closeConnection(messageHeaderAccessor);
            return new ConfirmationResponse("Invalid number");
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
           }
           else {
               statisticsService.incrementDuplicate();
           }
        }
    }

    private void writeToFile(String number) {
        try(FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(number + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PreDestroy
    private void shutdown() {
        executorService.shutdown();
    }
    private void terminateApplication() {
        shutdown();
        System.exit(0);
    }

    private void closeConnection(SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("close", true);
    }

}
