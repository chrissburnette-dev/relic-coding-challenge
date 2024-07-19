package com.example.reliccodingchallenge.controller;

import com.example.reliccodingchallenge.dto.NumberRequest;
import com.example.reliccodingchallenge.service.NumberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NumberController {

    private NumberService numberService;
    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @MessageMapping("/submitNumber")
    @SendTo("/topic/confirmation")
    public ResponseEntity<?> copyToList(@Valid NumberRequest numberInput) throws InterruptedException {
        Thread.sleep(1000);
        return new ResponseEntity<>(null , HttpStatus.OK );
    }

}

