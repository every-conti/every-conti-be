package my.everyconti.every_conti.common.exception;

public enum ResponseMessage {
    // 200 OK
    SUCCESS("요청이 성공적으로 처리되었습니다."),

    // 400 Bad Request
    BAD_REQUEST("잘못된 요청입니다."),

    // 401 Unauthorized
    UN_AUTHORIZED("인증이 필요합니다."),

    // 403 Forbidden
    FORBIDDEN("접근 권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("서버에서 오류가 발생했습니다.");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
