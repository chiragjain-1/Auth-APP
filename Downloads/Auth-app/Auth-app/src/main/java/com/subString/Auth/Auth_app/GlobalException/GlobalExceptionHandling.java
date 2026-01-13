package com.subString.Auth.Auth_app.GlobalException;
import com.subString.Auth.Auth_app.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleResourceNotFoundException(ResourceNotFoundException ex){
       ErrorResponse internalError = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND, "Resource Not Found");
       return new ResponseEntity<>(internalError, HttpStatus.NOT_FOUND);

    }

}
