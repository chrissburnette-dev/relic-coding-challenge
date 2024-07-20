package com.example.reliccodingchallenge.config.handlers;

import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class ConfirmationFrameHandler extends StompSessionHandlerAdapter {

    private final BlockingQueue<ConfirmationResponse> queue;

    public ConfirmationFrameHandler(BlockingQueue<ConfirmationResponse> queue) {
        this.queue = queue;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ConfirmationResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        queue.offer( (ConfirmationResponse) payload);
    }
}
