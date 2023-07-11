package com.guoshunfa.springboot2swaggerdemo.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// @Configuration 标识当前类是配置类，与@Bean搭配使用会直接注册为bean。
@Configuration
// @EnableOpenApi 标识开启生成接口文档功能（只有开启了OpenApi,才可以实现生成接口文档的功能）
@EnableOpenApi
@EnableSwagger2
public class SwaggerConfig {
    @Bean("swagger-1")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("swagger-1")
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean("swagger-2")
    public Docket createRest2Api() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("swagger-2")
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 添加标题
                .title("郭顺发 SpringBoot+Swagger接口文档平台" + System.currentTimeMillis())
                // 添加描述
                .description("https://www.guoshunfa.com")
                // 添加版本
                .version("1.0")
                .build();
    }
}