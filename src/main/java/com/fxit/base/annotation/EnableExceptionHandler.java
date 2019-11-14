package com.fxit.base.annotation;

import com.fxit.base.exception.ControllerExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ControllerExceptionHandler.class})
@Documented
public @interface EnableExceptionHandler {
}
