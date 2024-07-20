package com.example.reliccodingchallenge.controller;

import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.NumberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for Number operations.
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */

@RestController
public class NumberController {

    private NumberService numberService;

    @Autowired
    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @MessageMapping("/submitNumber")
    @SendTo("/topic/confirmation")
    public ConfirmationResponse copyToList(@Valid NumberRequest numberInput,
                                           SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
        Thread.sleep(1000);
        return numberService.handleNumberRequest(numberInput, headerAccessor);
    }

}

