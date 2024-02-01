package com.springmvc.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class AdviceController {
    //..
    @ExceptionHandler(NoHandlerFoundException.class)
    public String dealWithNoHandlerFoundException(Exception e, HttpServletRequest httpServletRequest)   {
                return "login";
    }
}
