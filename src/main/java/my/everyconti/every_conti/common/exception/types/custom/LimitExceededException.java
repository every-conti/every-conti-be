package my.everyconti.every_conti.common.exception.types.custom;

public class LimitExceededException extends RuntimeException {
    public LimitExceededException(String message) {
        super(message);
    }
}