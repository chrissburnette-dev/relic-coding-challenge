package com.example.reliccodingchallenge.config.handlers;

import com.example.reliccodingchallenge.dto.ErrorResponse;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class ErrorFrameHandler extends StompSessionHandlerAdapter {

    private final BlockingQueue<ErrorResponse> queue;

    public ErrorFrameHandler(BlockingQueue<ErrorResponse> queue) {
        this.queue = queue;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ErrorResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        queue.offer( (ErrorResponse) payload);
    }
}
