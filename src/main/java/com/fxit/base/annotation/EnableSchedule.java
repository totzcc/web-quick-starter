package com.fxit.base.annotation;

import com.fxit.base.annotation.config.EnableScheduleConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({EnableScheduleConfig.class})
@Documented
public @interface EnableSchedule {
}
