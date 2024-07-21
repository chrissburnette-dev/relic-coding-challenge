package com.example.reliccodingchallenge.config;

import com.example.reliccodingchallenge.config.utils.WebSocketTestUtils;
import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.ErrorResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebSocketSessionManager sessionManager;

    private WebSocketStompClient stompClient;

    private StompSession session;
    private BlockingQueue<ConfirmationResponse> confirmationQueue;
    private BlockingQueue<ErrorResponse> errorQueue;

    @BeforeEach
    public void setup() throws ExecutionException, InterruptedException, TimeoutException {

        stompClient = WebSocketTestUtils.createWebSocketClient();
        session = WebSocketTestUtils.connectWebSocketClient(stompClient, port);

        confirmationQueue = WebSocketTestUtils.createBlockingQueue();
        errorQueue = WebSocketTestUtils.createBlockingQueue();

        WebSocketTestUtils.subscribeToTopic(session, "/topic/confirmation",
                (BlockingQueue<Object>) (Object) confirmationQueue, ConfirmationResponse.class);

        WebSocketTestUtils.subscribeToTopic(session, "/queue/errors",
                (BlockingQueue<Object>) (Object) confirmationQueue, ErrorResponse.class);
    }

    @Test
    public void testConnection() {
        assertNotNull(session);
    }

    @Test
    public void testSubmitNumber() throws InterruptedException {
        WebSocketTestUtils.sendNumberRequest(session, new NumberRequest("123456789"));
        ConfirmationResponse response = confirmationQueue.poll(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals("Number accepted.", response.message());
    }

    @Test
    public void testSubmitInvalidNumber() throws InterruptedException {
        String sessionId = session.getSessionId();
        WebSocketTestUtils.sendNumberRequest(session, new NumberRequest("Invalid"));
        Thread.sleep(1000);
        assertFalse(sessionManager.isSessionActive(sessionId));
    }

    @Test
    public void testTerminateCommand() throws InterruptedException {
        WebSocketTestUtils.sendTerminateCommand(session);
        ConfirmationResponse response = confirmationQueue.poll(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals("Application terminating..", response.message());
    }

}
