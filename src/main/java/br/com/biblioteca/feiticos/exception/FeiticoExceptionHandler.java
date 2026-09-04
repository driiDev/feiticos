package br.com.biblioteca.feiticos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeiticoExceptionHandler {

    @ExceptionHandler(FeiticoNaoEncontradoException.class)
    public ResponseEntity<String> feiticoNaoEncontrado(FeiticoNaoEncontradoException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
