package my.everyconti.every_conti.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import my.everyconti.every_conti.constant.jwt.JwtMode;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class HashIdUtil {

    @Value("${spring.org.hashids.hash-id-salt}")
    private String hashIdSalt;
    private final Hashids hashids;

    public HashIdUtil() {
        this.hashids = new Hashids(hashIdSalt, 8);
    }

    public String encode(Long id) {
        return hashids.encode(id);
    }

    public Long decode(String hash) {
        long[] result = hashids.decode(hash);
        if (result.length == 0) return null;
        return result[0];
    }
}