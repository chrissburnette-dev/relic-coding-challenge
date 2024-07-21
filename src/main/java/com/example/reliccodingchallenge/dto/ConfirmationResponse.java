package com.example.reliccodingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Record DTO to map Response.
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */

@JsonSerialize
public record ConfirmationResponse(
    @JsonProperty String message

) { }
