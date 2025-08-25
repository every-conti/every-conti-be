package com.everyconti.every_conti.common.utils;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;

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