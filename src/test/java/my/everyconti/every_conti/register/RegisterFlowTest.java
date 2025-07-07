package my.everyconti.every_conti.register;

import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegisterFlowTest {

    @Autowired
    RedisService redisService;

    @Test
    public void register(){
//
//
//        String number = "123456";
//        String email = "dhapdhap123@naver.com";
//
//        System.out.println("email: " + redisService.getCode(email));
    }
}
