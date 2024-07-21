package com.example.reliccodingchallenge.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

    private static final String NUMBER_PATTERN = "\\d{9}";
    private static final Logger logger = LoggerFactory.getLogger(NumberValidator.class);
    @Override
    public void initialize(ValidNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        logger.debug("Validating number: {}", s);
        return (s != null && (s.equalsIgnoreCase("terminate") || s.matches(NUMBER_PATTERN)) );
    }
}
