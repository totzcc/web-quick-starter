package com.fxit.base.annotation.config;

import com.fxit.base.annotation.EnableSwagger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Component
@Configuration
@EnableSwagger2
public class EnableSwaggerConfig {
    private String active;
    private final Environment environment;

    public EnableSwaggerConfig(Environment environment) {
        this.environment = environment;
        this.active = environment.getProperty("spring.profiles.active");
        log.info("EnableSwaggerConfig created");
    }

    private static String packageName = "";
    private static String[] disabledOnProfiles;
    private static String[] headers;

    @Bean
    public Docket docket() {
        log.info("EnableSwaggerConfig config");
        if (disabledOnProfiles != null && disabledOnProfiles.length > 0) {
            // 环境禁用Swagger
            for (String profile : disabledOnProfiles) {
                if (profile.equalsIgnoreCase(active)) {
                    packageName = "hidden-swagger-docs";
                    break;
                }
            }
        }

        List<ApiKey> apiKeys = new ArrayList<ApiKey>();
        if (headers != null && headers.length > 0) {
            for (String header : headers) {
                apiKeys.add(new ApiKey(header, header, "header"));
            }
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageName))
                .build()
                .securitySchemes(apiKeys);
    }

    public static class Config implements ImportBeanDefinitionRegistrar {

        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableSwagger.class.getName()));
            if (attributes != null) {
                packageName = attributes.getString("value");
                disabledOnProfiles = attributes.getStringArray("disabledOnProfiles");
                headers = attributes.getStringArray("headers");
            }

        }
    }
}
