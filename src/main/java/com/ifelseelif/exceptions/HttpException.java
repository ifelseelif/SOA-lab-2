package com.ifelseelif.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpException extends Exception {
    private String message;
    private int statusCode;
}