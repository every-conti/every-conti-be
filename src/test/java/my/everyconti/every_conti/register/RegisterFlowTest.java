//package my.everyconti.every_conti.register;
//
//import my.everyconti.every_conti.modules.redis.RedisService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@WithMockUser
//public class RegisterFlowTest {
//
//    @Autowired
//    RedisService redisService;
//    @Autowired
//    MockMvc mockMvc;
//
//    private String nickname = "LYC";
//    private String email = "dhapdhap123@naver.com";
//    private String password = "11111";
//    private String church = "아바교회";
//
//    @Test
//    public void verifyEmail() throws Exception {
//        // 이메일 인증 코드 받기
//        Map<String, String> body = new HashMap<>();
//        body.put("email", email);
//
//        mockMvc.perform(post("/mail/code", body)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"dhapdhap123@naver.com\"}")
//                )
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("true")); // 응답 내용
//    }
//
//    @Test
//    public void register() throws Exception {
//        Map<String, String> body = new HashMap<>();
//        body.put("email", email);
//        // 이메일 코드 직접 입력
//        String userCode = "112869";
//
//        // 이메일 인증 코드 확인
//        mockMvc.perform(get(String.format("/mail/code/verify?email=%s&userCode=%s", email, userCode)))
//                .andExpect(status().isOk());
//
//
//        // 회원가입
//        body.put("password", password);
//        body.put("nickname", nickname);
//        body.put("church", church);
//        mockMvc.perform(post("/auth/member", body).with(csrf()))
//                .andExpect(status().is2xxSuccessful());
//    }
//}
