package com.fxit.base.annotation.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;


@Data
@Slf4j
@Component
@Configuration
@ConfigurationProperties(prefix = "schedule", ignoreUnknownFields = false)
public class EnableScheduleConfig {
    private boolean enable = false;

    public EnableScheduleConfig() {
        System.out.println("EnableDdcScheduleConfig created");
        log.info("EnableDdcScheduleConfig created");
    }

    @Bean
    public BeanPostProcessor scheduledAnnotationProcessor() {
        System.out.println(String.format("EnableDdcScheduleConfig config, enable=%s, ", enable));
        log.info(String.format("EnableDdcScheduleConfig config, enable=%s, ", enable));
        if (enable) {
            return new ScheduledAnnotationBeanPostProcessor();
        }
        return new BeanPostProcessor() {
        };
    }
}
