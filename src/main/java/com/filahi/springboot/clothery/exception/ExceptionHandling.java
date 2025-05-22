package com.filahi.springboot.clothery.exception;

import com.filahi.springboot.clothery.domain.HttpResponse;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import jakarta.persistence.NoResultException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(NotTheCorrectImageFileException.class)
    public ResponseEntity<HttpResponse> notTheCorrectImageFileException(NotTheCorrectImageFileException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<HttpResponse> handleMissingParameterException(MissingServletRequestParameterException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}
