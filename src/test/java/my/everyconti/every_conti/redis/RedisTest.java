package my.everyconti.every_conti.redis;

import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.service.MemberService;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
//@Rollback(false)
public class RedisTest {

    @Autowired
    RedisService redisService;

    @Test
    public void userSave(){
        String number = "123456";
        String email = "dhapdhap123@naver.com";
        redisService.setCode(email, number);
        System.out.println("email: " + redisService.getCode(email));
    }
}
