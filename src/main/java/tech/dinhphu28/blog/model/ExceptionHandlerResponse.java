package tech.dinhphu28.blog.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionHandlerResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
