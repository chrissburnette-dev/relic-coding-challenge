package com.example.reliccodingchallenge.config;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testWebSocketCommunication() throws ExecutionException, InterruptedException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = new StringBuilder("ws://localhost:").append(port).append("/ws").toString();
        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter(){}).get();

        NumberRequest request = new NumberRequest("123456789");
        session.send("/app/submitNumber", request);
    }

}
