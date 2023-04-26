package com.example.demo;

import com.example.exceptions.DateFormatException;
import com.example.exceptions.LackDataException;
import com.example.exceptions.ParametersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ParametersException.class)
    public ResponseEntity<Object> handleWrongParametersException(ParametersException ex) {
        Map<String, String> errorMap = new HashMap<>();
        String message = "Parameter " + ex.getVariableName() + " should be between " + ex.getMinVal()+ " and " +ex.getMaxVal();
        errorMap.put("error", message);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LackDataException.class)
    public ResponseEntity<Object> handleLackDataException(LackDataException ex) {
        Map<String, String> errorMap = new HashMap<>();
        String message = "No such data in database";
        errorMap.put("error", message);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateFormatException.class)
    public ResponseEntity<Object> handleDateFormatException(DateFormatException ex) {
        Map<String, String> errorMap = new HashMap<>();
        String message = "Date should be "+ex.getDate()+" format";
        errorMap.put("error", message);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}





