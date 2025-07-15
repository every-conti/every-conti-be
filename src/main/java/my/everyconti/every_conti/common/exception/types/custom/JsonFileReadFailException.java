package my.everyconti.every_conti.common.exception.types.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JsonFileReadFailException extends RuntimeException {
    public JsonFileReadFailException(String message) {
        super(message);
    }
}
