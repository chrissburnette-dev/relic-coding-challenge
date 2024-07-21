package com.example.reliccodingchallenge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    public void addSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean isSessionActive(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public void closeSession(String sessionId) {
        WebSocketSession session = sessions.remove(sessionId);
        if(session != null && session.isOpen()) {
            try {
                session.close();
            }
            catch (IOException e) {
                logger.error("Session was unable to be closed.");
                throw new RuntimeException(e);
            }
        }
    }
}