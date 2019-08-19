package com.fxit.base.annotation.config;

import com.fxit.base.annotation.EnableSchedule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;


@Data
@Slf4j
@Component
@Configuration
@ConfigurationProperties(prefix = "schedule", ignoreUnknownFields = false)
public class EnableScheduleConfig {
    private boolean enable = false;
    private static int poolSize = 1;

    public EnableScheduleConfig() {
        System.out.println("EnableDdcScheduleConfig created");
        log.info("EnableDdcScheduleConfig created");
    }

    @Bean
    public BeanPostProcessor scheduledAnnotationProcessor() {
        System.out.println(String.format("EnableDdcScheduleConfig config, enable=%s, poolSize=", enable));
        log.info(String.format("EnableDdcScheduleConfig config, enable=%s, ", enable));
        if (enable) {
            ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
            registrar.setScheduler(Executors.newScheduledThreadPool(poolSize));
            return new ScheduledAnnotationBeanPostProcessor(registrar);
        }
        return new BeanPostProcessor() {
        };
    }

    public static class Config implements ImportBeanDefinitionRegistrar {

        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableSchedule.class.getName()));
            if (attributes != null) {
                EnableScheduleConfig.poolSize = attributes.getNumber("poolSize").intValue();
            }
        }
    }
}
