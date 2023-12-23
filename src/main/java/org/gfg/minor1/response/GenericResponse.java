package org.gfg.minor1.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GenericResponse<T> {
    private String error;
    private String code;
    private T data;
    private HttpStatus status;
}
