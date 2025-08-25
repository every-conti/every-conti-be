package com.everyconti.every_conti.modules.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/log")
@Slf4j
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