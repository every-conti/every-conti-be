package my.everyconti.every_conti.constant;

public class ResponseMessage {
    // 200 ~
    public static final String SUCCESS = "요청이 성공적으로 처리되었습니다.";

    // 400
    public static final String BAD_REQUEST = "잘못된 요청입니다.";
    // 401
    public static final String UN_AUTHORIZED = "인증이 필요합니다.";
    // 403
    public static final String FORBIDDEN = "접근 권한이 없습니다.";
    // 404
    public static final String NOT_FOUND = "요청한 리소스를 찾을 수 없습니다.";
    // 409
    public static final String CONFLICT = "이미 존재하는 리소스입니다.";
    // 500
    public static final String INTERNAL_SERVER_ERROR = "알 수 없는 오류가 발생했습니다.";


    public static final String PASSWORD_INCONSISTENCY = "비밀번호가 일치하지 않습니다.";
    public static final String USER_NOT_EXIST = "일치하는 유저정보가 없습니다.";
    public static final String DELETED = "삭제 완료 되었습니다.";
}
