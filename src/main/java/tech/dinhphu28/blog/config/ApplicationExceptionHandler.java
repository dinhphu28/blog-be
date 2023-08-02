package tech.dinhphu28.blog.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
    public ExceptionHandlerResponse handleOTPNotVerifiedException(OTPNotVerifiedException exception, HttpServletRequest request) {

        ExceptionHandlerResponse exceptionHandlerResponse = ExceptionHandlerResponse.builder()
                .timestamp(sdf.format(new Date()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .build();

        return exceptionHandlerResponse;
    }
}
