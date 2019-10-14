package com.trilogyed.admin.controller;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.trilogyed.admin.exception.NotFoundException;
import feign.FeignException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> validationError(MethodArgumentNotValidException e, WebRequest request) {
        // BindingResult holds the validation errors
        BindingResult result = e.getBindingResult();
        // Validation errors are stored in FieldError objects
        List<FieldError> fieldErrors = result.getFieldErrors();

        // Translate the FieldErrors to VndErrors
        List<VndErrors.VndError> vndErrorList = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            VndErrors.VndError vndError = new VndErrors.VndError(request.toString(), fieldError.getDefaultMessage());
            vndErrorList.add(vndError);
        }

        // Wrap all of the VndError objects in a VndErrors object
        VndErrors vndErrors = new VndErrors(vndErrorList);

        // Create and return the ResponseEntity
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(vndErrors, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    // Handles invalid arguments from user such as invalid id
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> outOfRangeException(IllegalArgumentException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    // Handles not found cases in get APIs
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> notFoundException(NotFoundException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "Not found : " + e.getMessage());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    // Handles null pointer exceptions
    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> nullPointerException(NullPointerException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "Null Pointer: " + e.getMessage().toLowerCase());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    // Handles null pointer exceptions
    @ExceptionHandler(value = {JsonMappingException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<VndErrors> JsonMappingException(JsonMappingException e, WebRequest request) {
        VndErrors error = new VndErrors(request.toString(), "Null Pointer: " + e.getMessage().toLowerCase());
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
        return responseEntity;
    }

    // Handles exceptions returned from Feign that are nested in other exceptions
    // Ex. validations for objects in a list
    @ExceptionHandler(value = {feign.codec.EncodeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> encodeException(feign.codec.EncodeException e, WebRequest request) {

        int begIdx = e.toString().indexOf("Could not write JSON:") + 22;
        int endIdx = e.toString().indexOf("; ");
        String msg = e.toString().substring(begIdx, endIdx);

        // displays exception message from remote service
        VndErrors error = new VndErrors(request.toString(), msg);
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    // Handles exceptions returned from Feign
    @ExceptionHandler(value = {FeignException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<VndErrors> feignNotFoundException(FeignException e, WebRequest request) {

        // extracts exception message from remote service
        int begIdx = e.contentUTF8().indexOf("message")+10;
        int endIdx;
        String msg;
        try {
            endIdx = e.contentUTF8().indexOf("links") - 3;
            msg = e.contentUTF8().substring(begIdx, endIdx);
        }
        catch (Exception s) {
            endIdx = e.contentUTF8().indexOf("path") - 3;
            msg = e.contentUTF8().substring(begIdx, endIdx);
        }

        // displays Not Found Status and exception message from remote service
        VndErrors error = new VndErrors(request.toString(), msg);
        ResponseEntity<VndErrors> responseEntity = new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

}
