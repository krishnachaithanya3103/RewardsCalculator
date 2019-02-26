package com.charter.codeTest.rewardsCalculator.controller;

import com.charter.codeTest.rewardsCalculator.error.ErrorDetails;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Haven't completed the exception handler here. We can have more methods here based on any validation exceptions that we return
     * Since i haven't put in any validations, skipping that part for now
     */

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                ExceptionUtils.getRootCause(ex).toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
