package com.example.examplespring.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * 스케줄
 * @author gagyeong
 */
@Component
public class ExampleScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 5초마다 작동 (로컬, 개발 서버)
     */
    @Scheduled(cron = "#{@schedulerCronExample1}") // @Bean에 등록한 이름을 그대로 맵핑
    public void schedule1() {
        logger.info("schedule1 동작하고 있음 : {}", Calendar.getInstance().getTime());
    }
}
