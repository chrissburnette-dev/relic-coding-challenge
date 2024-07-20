package com.example.reliccodingchallenge.config.utils;

import com.example.reliccodingchallenge.dto.NumberRequest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebSocketTestUtils {

    public static WebSocketStompClient createWebSocketClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    public static StompSession connectWebSocketClient(WebSocketStompClient stompClient, int port) throws ExecutionException, InterruptedException, TimeoutException {
        String url = new StringBuilder("ws://localhost:").append(port).append("/ws").toString();
        CompletableFuture<StompSession> future = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {});
        return future.get(5, TimeUnit.SECONDS);
    }

    public static <T> BlockingQueue<T> createBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }
    public static void subscribeToTopic(StompSession session, String topic, StompSessionHandlerAdapter handler) {
        session.subscribe(topic, handler);
    }
    public static void sendNumberRequest(StompSession session, NumberRequest request) {
        session.send("/app/submitNumber", request);
    }
}
