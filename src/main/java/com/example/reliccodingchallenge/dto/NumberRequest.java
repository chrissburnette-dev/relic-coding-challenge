package com.example.reliccodingchallenge.dto;

import com.example.reliccodingchallenge.validations.ValidNumber;
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
//        @ValidNumber
        String number

) { }
