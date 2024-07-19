package com.example.reliccodingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;


public record NumberRequest(

        @JsonProperty
        @Pattern(regexp = "\\d{9}", message = "Number must be 9 digits.")
        String number

) { }
