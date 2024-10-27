package de.functionfactory.movie_api.tech.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;

//@RestControllerAdvice
//class GlobalRequestExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler
//    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
//        ProblemDetail problemDetail = ProblemDetail
//                .forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
//        problemDetail.setType(URI.create("http://localhost:8080/book"));
//        problemDetail.setProperty("timestamp", Instant.now());
//        return problemDetail;
//    }
//}


@ControllerAdvice
public class GlobalRequestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handle(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        final ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                ZonedDateTime.now(),
                List.of()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    //    // validate-error
    //    @ExceptionHandler(MethodArgumentTypeMismatchException.class)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handle(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<String> errors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                errors
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
//
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handle(
            ConstraintViolationException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                List.of(e.getMessage())
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(
            Exception e,
            HttpServletRequest request
    ) {
        System.out.println("🙈" + e.getClass().getName());
        final ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ZonedDateTime.now(),
                List.of(e.getMessage())
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


