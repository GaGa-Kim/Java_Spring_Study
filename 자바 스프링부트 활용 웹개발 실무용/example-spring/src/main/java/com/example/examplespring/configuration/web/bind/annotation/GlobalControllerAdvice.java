package com.example.examplespring.configuration.web.bind.annotation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageSource messageSource;

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handlerIllegalArgumentException(IllegalStateException e) {
        String code = e.getMessage();
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        logger.info("code : {}", code);
        logger.info("message : {}", message);
        logger.error("e", e);
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(code, message), HttpStatus.BAD_REQUEST);
    }
}
