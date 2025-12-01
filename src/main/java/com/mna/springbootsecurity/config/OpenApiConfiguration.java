package com.mna.springbootsecurity.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    private static final String KEY = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info())
                .addSecurityItem(new SecurityRequirement().addList(KEY))
                .components(components());
    }

    private Info info() {
        return new Info()
                .title("Applications APIs")
                .version("1.0")
                .description("Description of my API")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("API Support")
                        .email("support@example.com")
                        .url("http://www.example.com/support"))
                .license(new io.swagger.v3.oas.models.info.License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0"));

    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(KEY, new SecurityScheme()
                        .name("Bearer Authentication")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );
    }
}
