package my.everyconti.every_conti.common.exception.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistElementException extends RuntimeException {
    public AlreadyExistElementException(String message) {
        super(message);
    }
}
