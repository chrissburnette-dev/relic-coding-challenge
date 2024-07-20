package com.example.reliccodingchallenge.service;

import com.example.reliccodingchallenge.dto.ConfirmationResponse;
import com.example.reliccodingchallenge.dto.NumberRequest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface NumberService {
    ConfirmationResponse handleNumberRequest(NumberRequest request, SimpMessageHeaderAccessor messageHeaderAccessor);

}
