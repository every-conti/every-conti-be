package my.everyconti.every_conti.modules.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j // spring-boot-starter-web 의존성 있다면
//@Slf4j 애노테이션으로 바로 log 사용 가능
public class LogController {

    @GetMapping("/")
    public void log() {
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message"); //(기본 로그 레벨)
        log.warn("warn message");
        log.error("error message");
    }
}