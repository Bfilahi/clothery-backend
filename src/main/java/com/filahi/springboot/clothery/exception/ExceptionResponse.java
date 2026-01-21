package com.filahi.springboot.clothery.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private int status;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private long timestamp;
}
