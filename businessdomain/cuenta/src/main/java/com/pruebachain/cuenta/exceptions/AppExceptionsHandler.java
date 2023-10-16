/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cuenta.exceptions;

import com.pruebachain.cuenta.entities.response.ErrorMessage;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author cberm3o
 */
@ControllerAdvice
public class AppExceptionsHandler {
    
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handlerOtherExceptions(Exception ex, WebRequest request){
        ErrorMessage errorMEnsaje = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMEnsaje, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
