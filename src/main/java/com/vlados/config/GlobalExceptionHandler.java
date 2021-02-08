package com.vlados.config;

import com.vlados.exception.StoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private final String goBack = "goBack";

    @ExceptionHandler
    public <T extends StoreException> ResponseEntity<Object> orderException(T e, Locale locale) {
        String message = messageSource.getMessage(e.getMessage(), null, locale) +
                System.lineSeparator() + messageSource.getMessage(goBack, null, locale);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
