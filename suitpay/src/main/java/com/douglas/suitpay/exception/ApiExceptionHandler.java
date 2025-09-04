package com.douglas.suitpay.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(403).body(Map.of(
                "error","forbidden",
                "message","Você não tem permissão para executar esta ação.",
                "path", req.getRequestURI()
        ));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String,Object>> handleBusiness(BusinessException ex, HttpServletRequest req) {
        return ResponseEntity.unprocessableEntity().body(Map.of(
                "error","business_rule",
                "message", ex.getMessage(),
                "path", req.getRequestURI()
        ));
    }
}
