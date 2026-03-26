//package com.example.demo.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ProblemDetail;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ResponseEntity<ProblemDetail> handleEmailExists(
//            EmailAlreadyExistsException ex,
//            HttpServletRequest request) {
//
//        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
//        problem.setTitle("Conflict");
//        problem.setDetail(ex.getMessage());
//        problem.setProperty("path", request.getRequestURI());
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ProblemDetail> accessDenied(
//            AccessDeniedException ex,
//            HttpServletRequest request) {
//
//        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
//        problem.setTitle("Access Denied");
//        problem.setDetail("Você não tem permisssão para acessar esse recurso");
//        problem.setProperty("path", request.getRequestURI());
//
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
//    }
//
//    @ExceptionHandler(CertificateValidationException.class)
//    public ResponseEntity<ProblemDetail> handleCertificateException(
//            CertificateValidationException ex,
//            HttpServletRequest request
//    ) {
//        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//        problem.setTitle("Certificado inválido");
//        problem.setDetail(ex.getMessage());
//        problem.setProperty("path", request.getRequestURI());
//
//        return ResponseEntity.badRequest().body(problem);
//    }
//}
