package tech.dinhphu28.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class OTPNotVerifiedException extends RuntimeException {
    public OTPNotVerifiedException(String msg) {
        super(msg);
    }
}
