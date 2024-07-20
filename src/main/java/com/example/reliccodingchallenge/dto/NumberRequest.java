package com.example.reliccodingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Pattern;

/**
 * Record DTO to map incoming Number Request.
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */

@JsonSerialize
public record NumberRequest(

        @JsonProperty
        @Pattern(regexp = "\\d{9}", message = "Must be a 9 digit number.")
        String number

) { }
