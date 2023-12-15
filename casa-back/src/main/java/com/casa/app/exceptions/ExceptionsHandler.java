package com.casa.app.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(value = {UserServiceException.class})
//    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
//        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
//        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), requestUri);
//        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), ex.getStatus());
//    }
    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<Object> handleInvalidCredentialsException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body("Wrong credentials");
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}