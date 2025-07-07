package my.everyconti.every_conti.modules.redis.service;

import my.everyconti.every_conti.common.exception.ResponseMessage;
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

    //email을 key값 code를 value로 하여 3분동안 저장한다.
    public void setCode(String email,String code){
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        //만료기간 3분
        valOperations.set(email,code,180, TimeUnit.SECONDS);
    }

    //key값인 email에 있는 value를 가져온다.
    public String getCode(String email){
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        Object code = valOperations.get(email);
        if(code == null){
            throw new UnAuthenticationException(ResponseMessage.UN_AUTHORIZED.getMessage());
        }
        return code.toString();
    }
}
