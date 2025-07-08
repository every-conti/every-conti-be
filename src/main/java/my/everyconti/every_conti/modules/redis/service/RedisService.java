package my.everyconti.every_conti.modules.redis.service;

import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.common.exception.UnAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisKeyValue(String key,String value, Integer timeout){
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        valOperations.set(key, value,timeout, TimeUnit.SECONDS);
    }

    public String getRedisValueByKey(String key){
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        Object value = valOperations.get(key);
        if(value == null){
            throw new UnAuthenticationException(ResponseMessage.UN_AUTHORIZED);
        }
        return value.toString();
    }
}
