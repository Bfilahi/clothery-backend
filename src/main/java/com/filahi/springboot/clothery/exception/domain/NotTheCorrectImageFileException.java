package com.filahi.springboot.clothery.exception.domain;

public class NotTheCorrectImageFileException extends RuntimeException{
    public NotTheCorrectImageFileException(String message){
        super(message);
    }
}
