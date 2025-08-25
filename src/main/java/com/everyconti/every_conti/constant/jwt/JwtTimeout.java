package com.everyconti.every_conti.constant.jwt;

public class JwtTimeout {
    public static final long ACCESS_TOKEN_TIMEOUT = 1000 * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_TIMEOUT = 720 * 1000 * 60 * 60L; // 30일
}
