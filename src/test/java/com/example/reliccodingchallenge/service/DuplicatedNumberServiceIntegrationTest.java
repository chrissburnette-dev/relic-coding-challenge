package com.example.reliccodingchallenge.service;


import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DuplicatedNumberServiceIntegrationTest {

    @Autowired
    private NumberService numberService;

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testHandleNumberRequest() {

        NumberRequest request = new NumberRequest("123456789");
        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.create();

        ConfirmationResponse response = numberService.handleNumberRequest(request, messageHeaderAccessor);

        assertEquals("Number accepted.", response.message());
    }

    @Test
    public void testReportStatistics() {
        statisticsService.incrementUnique();
        statisticsService.incrementDuplicate();
        statisticsService.reportStatistics();
    }

}
