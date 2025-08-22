package my.everyconti.every_conti.common.exception;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import my.everyconti.every_conti.common.dto.error.ErrorResponseDto;
import my.everyconti.every_conti.common.exception.types.*;
import my.everyconti.every_conti.common.exception.types.custom.CustomAuthException;
import my.everyconti.every_conti.common.exception.types.custom.LimitExceededException;
import my.everyconti.every_conti.common.utils.ExceptionUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.env}")
    private String applicationEnv;
    private boolean isTraceOn;

    @PostConstruct
    public void init() {
        // dev 환경에서만 trace 켜기
        isTraceOn = applicationEnv.equals("dev");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.valueOf(statusCode.value()), request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      String message,
                                                      HttpStatus httpStatus,
                                                      WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(httpStatus.value(), message, LocalDateTime.now());
        if (isTraceOn) {
            errorResponseDto.setStackTrace(ExceptionUtil.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponseDto);
    }

    // 400 Invalid Request Exception
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exception, WebRequest request) {
        log.error(ResponseMessage.BAD_REQUEST, exception);
        return buildErrorResponse(exception, ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, request);
    }

    // 401 Invalid Request Exception
    @ExceptionHandler(UnAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorizedException(UnAuthorizationException exception, WebRequest request) {
        log.error(ResponseMessage.UN_AUTHORIZED, exception);
        return buildErrorResponse(exception, ResponseMessage.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(CustomAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthException(CustomAuthException exception, WebRequest request) {
        log.error(ResponseMessage.UN_AUTHORIZED, exception);
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    // 403 Access Denied Exception
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        log.error(ResponseMessage.FORBIDDEN, exception);
        return buildErrorResponse(exception, ResponseMessage.FORBIDDEN, HttpStatus.FORBIDDEN, request);
    }
    // 403 Access Denied Exception
    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden
    public ResponseEntity<Object> handleAuthorizationDeniedException(org.springframework.security.authorization.AuthorizationDeniedException exception, WebRequest request) {
        log.error(ResponseMessage.FORBIDDEN, exception);
        return buildErrorResponse(exception, ResponseMessage.FORBIDDEN, HttpStatus.FORBIDDEN, request);
    }

    // 404 Not Found Exception
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 NotFound
    public ResponseEntity<Object> handleNotfoundException(NotFoundException exception, WebRequest request) {
        log.error(ResponseMessage.NOT_FOUND, exception);
        return buildErrorResponse(exception, ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND, request);
    }

    // 409 AlreadyExistElementException
    @ExceptionHandler(AlreadyExistElementException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleAlreadyExistElementException(AlreadyExistElementException exception, WebRequest request){
        log.error(ResponseMessage.CONFLICT, exception);
        return buildErrorResponse(exception, ResponseMessage.CONFLICT, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<Object> handleLimitExceeded(LimitExceededException ex, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    // 412 Validate Exception
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details.", LocalDateTime.now());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponseDto.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponseDto);
    }

    // 500 Uncaught Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error(ResponseMessage.INTERNAL_SERVER_ERROR, exception);
        return buildErrorResponse(exception, ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
