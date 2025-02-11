package com.sistemaligafutbol.sistemaligafutbol.exceptions;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.dto.ErrorMessage;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //PARA 404 "RECURSO NO ENCONTRADO"
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> localNotFoundException(NotFoundException exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    //PARA 400 VALIDACIONES CON ANOTACIONES
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {


        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    //409 - CONFLICT  ERRORES BD
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Error de integridad en la base de datos.";

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            errorMessage = "Violación de restricción en la base de datos: " + ex.getCause().getMessage();
        }

        ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT, errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }


    //401 Unauthorized → Falta autenticación
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED, "No estás autenticado. Por favor, inicia sesión.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    //403 403 Forbidden → Autenticado, pero sin permisos
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este recurso.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    //500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {
        ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    //NULL POINTER
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, "Se encontró un valor nulo inesperado.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    //PARA EXCEPTIONS DE IMAGEN
    @ExceptionHandler(ImageValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleImageValidationException(ImageValidationException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    //PARA CREDENCIALES INCORRECTAS DEL LOGIN DE CONTRA Y CORREO
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleBadCredentials(BadCredentialsException ex) {
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }


}