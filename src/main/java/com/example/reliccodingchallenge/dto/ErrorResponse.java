package com.example.reliccodingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

/**
 * Record DTO to map Error Response.
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */

@JsonSerialize
public record ErrorResponse(
        @JsonProperty String  errorMessage,
     @JsonProperty Map<String, String> errors
) {

    public ErrorResponse(String errorMessage) {
        this(errorMessage, new HashMap<>());
    }

}
