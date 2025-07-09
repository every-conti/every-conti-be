package my.everyconti.every_conti.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsInDBException extends RuntimeException {
    public AlreadyExistsInDBException(String message) {
        super(message);
    }
}
