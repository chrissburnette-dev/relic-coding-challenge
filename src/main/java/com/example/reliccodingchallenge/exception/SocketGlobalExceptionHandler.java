package com.example.reliccodingchallenge.exception;


import com.example.reliccodingchallenge.config.WebSocketSessionManager;
import com.example.reliccodingchallenge.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler class for global exception handling and customization .
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */
@RestControllerAdvice
public class SocketGlobalExceptionHandler {

    private WebSocketSessionManager sessionManager;

    public SocketGlobalExceptionHandler(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ExceptionHandler(InvalidNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNumberException(InvalidNumberException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler(CustomServletException.class)
    @SendToUser("/queue/errors")
    public ResponseEntity<ErrorResponse> handleCustomException(CustomServletException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException() {
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser("/queue/errors")
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                   SimpMessageHeaderAccessor headerAccessor) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error: ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse("Invalid number.", errors);
        headerAccessor.getSessionAttributes().put("close", true);
        closeWebSocketSession(headerAccessor.getSessionId());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler(ConstraintViolationException.class)
    @SendToUser("/queue/errors")
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
                                                                            SimpMessageHeaderAccessor headerAccessor) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        ErrorResponse errorResponse = new ErrorResponse("Invalid number.", errors);
        headerAccessor.getSessionAttributes().put("close", true);
        closeWebSocketSession(headerAccessor.getSessionId());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private void closeWebSocketSession(String sessionId) {
        sessionManager.closeSession(sessionId);
    }

}
