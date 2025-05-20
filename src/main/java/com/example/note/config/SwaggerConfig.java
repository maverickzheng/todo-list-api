package com.example.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI noteAPI() {
        Info info = new Info()
                .title("代辦事項API") // API 標題
                .version("V 0.0.0") // API 版本
                .description("Note API") // API 描述
                .contact(new Contact() // 聯絡人資訊
                        .name("開發團隊") // 聯絡人姓名
                        .email("team@example.com") // 聯絡人 Email
                        .url("https://www.example.com/contact")) // 聯絡人 URL
                .license(new License() // 授權資訊
                        .name("Your License") // 授權名稱
                        .url("http://springdoc.org")) // 授權 URL
                .termsOfService("https://www.example.com/terms"); // 服務條款
        // .servers();

        return new OpenAPI().info(info);
    }

}
