//package my.everyconti.every_conti.redis;
//
//import my.everyconti.every_conti.constant.redis.RedisTimeout;
//import my.everyconti.every_conti.modules.redis.RedisService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
////@Transactional
////@Rollback(false)
//public class RedisTest {
//
//    @Autowired
//    RedisService redisService;
//
//    @Test
//    public void userSave(){
//        String number = "123456";
//        String email = "dhapdhap123@naver.com";
//        redisService.setRedisKeyValue(email, number, RedisTimeout.EMAIL_VERIFICATION_TIMEOUT);
//        System.out.println("email: " + redisService.getRedisValueByKey(email));
//    }
//}
