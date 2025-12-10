package com.electronic.store.exceptions;

import com.electronic.store.payloads.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handler resource not found exception

    @ExceptionHandler(ResourceNoteFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNoteFoundException ex) {
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        HashMap<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String key = ((FieldError) objectError).getField();

            response.put(key,message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //handle bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handlerBadApiRequest(BadApiRequestException ex) {
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }
}
