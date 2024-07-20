package com.example.reliccodingchallenge.exception;

import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.NumberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GlobalExceptionHandlerIntegrationTest {

    @LocalServerPort
    private int port;
    @Test
    public void testWebSocketCommunicationInvalidNumber() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = new StringBuilder("ws://localhost:").append(port).append("/ws").toString();

        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);

        assertNotNull(session);

        NumberRequest request = new NumberRequest("invalid");
        session.send("/app/submitNumber", request);

    }

}
