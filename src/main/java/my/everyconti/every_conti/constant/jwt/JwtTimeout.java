package my.everyconti.every_conti.constant.jwt;

public class JwtTimeout {
    public static final int ACCESS_TOKEN_TIMEOUT = 1000 * 60 * 60; // 1시간
    public static final int REFRESH_TOKEN_TIMEOUT = 720 * 1000 * 60 * 60; // 30일
}
