package com.fxit.base.annotation;

import com.fxit.base.annotation.config.EnableSwaggerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({EnableSwaggerConfig.Config.class, EnableSwaggerConfig.class})
@Documented
public @interface EnableSwagger {
    String value();

    String[] disabledOnProfiles() default {};

    String[] headers() default {};
}
