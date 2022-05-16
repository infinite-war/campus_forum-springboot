package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2   //开启swagger2
public class SwaggerConfig {
//
//    // 配置了Swagger的Docket的bean实例
//    @Bean
//    public Docket docket(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.post.controller"))
//                .paths(PathSelectors.ant("/xxx/**"))
//                .build();
//    }
//    //配置Swagger的信息
//    private ApiInfo apiInfo(){
//        //作者信息
//        Contact contact = new Contact("infinite", "http://xxx.com", "123456@qq.com");
//        return new ApiInfo(
//                "自建SwaggerAPI文档",
//                "This is a description",
//                "va.b",
//                "http://xxx.com",
//                contact,
//                "Apache2.0",
//                "http://www.apache.org/lisence/LISENCE-2.0",
//                new ArrayList<>()
//        );
//    }
}