package com.example.reliccodingchallenge.service;

import com.example.reliccodingchallenge.config.WebSocketSessionManager;
import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.impl.DuplicatedListNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DuplicatedListNumberServiceTest {

    private NumberService numberService;

    private StatisticsService statisticsService;

    private WebSocketSessionManager sessionManager;

    private ExecutorService executorService;


    @BeforeEach
    public void setUp() {
        statisticsService = mock(StatisticsService.class);
        sessionManager = mock(WebSocketSessionManager.class);
        executorService = mock(ExecutorService.class);
        numberService = new DuplicatedListNumberService(statisticsService, executorService, sessionManager);
    }

    @Test
    public void testHandleNumberRequest_ValidNumber() {
        NumberRequest request = new NumberRequest("123456789");
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);

        ConfirmationResponse response = numberService.handleNumberRequest(request, headerAccessor);

        assertEquals("Number accepted.", response.message());

        verify(statisticsService).incrementUnique();

        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(executorService).submit(captor.capture());

        //captor.getValue().run();

    }
    @Test
    public void testHandleNumberRequest_InvalidNumber() {
        NumberRequest request = new NumberRequest("invalid");
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);

        ConfirmationResponse response = numberService.handleNumberRequest(request, headerAccessor);

        assertEquals("Invalid number.", response.message());
        verify(statisticsService, never()).incrementUnique();
        verify(executorService, never()).submit(any(Runnable.class));
    }

    @Test
    public void testHandleNumberRequest_Terminate() {
        NumberRequest request = new NumberRequest("Terminate");
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);

        numberService.handleNumberRequest(request, headerAccessor);

        verify(statisticsService, never()).incrementUnique();
        verify(executorService, never()).submit(any(Runnable.class));
    }

}
