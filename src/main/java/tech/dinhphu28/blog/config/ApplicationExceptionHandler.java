package tech.dinhphu28.blog.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.dinhphu28.blog.exception.OTPNotVerifiedException;
import tech.dinhphu28.blog.model.ExceptionHandlerResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    // 2021-03-24T16:44:39.083+08:00
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(OTPNotVerifiedException.class)
    public ExceptionHandlerResponse handleOTPNotVerifiedException(OTPNotVerifiedException exception,
                                                                  HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        return ExceptionHandlerResponse.builder()
                .timestamp(sdf.format(new Date()))
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ExceptionHandlerResponse handleUsernameNotFoundException(UsernameNotFoundException exception,
                                                                    HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        return ExceptionHandlerResponse.builder()
                .timestamp(sdf.format(new Date()))
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionHandlerResponse handleAccessDeniedException(AccessDeniedException exception,
                                                                HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        return ExceptionHandlerResponse.builder()
                .timestamp(sdf.format(new Date()))
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .build();
    }
}
