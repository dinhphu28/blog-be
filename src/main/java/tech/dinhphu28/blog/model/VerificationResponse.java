package tech.dinhphu28.blog.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {
    private Integer status;
    private String message;
}
