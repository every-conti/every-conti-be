package my.everyconti.every_conti.common.exception.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException(String message) {
        super(message);
    }
}
