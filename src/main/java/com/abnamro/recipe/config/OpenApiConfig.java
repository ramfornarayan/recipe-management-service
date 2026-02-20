package com.abnamro.recipe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", bearerSecurityScheme()));
    }

    private Info apiInfo() {
        return new Info()
            .title("Recipe Management Service API")
            .description("""
                Production-grade REST API for managing recipes, ingredients,
                and associations. Designed for scalability, security,
                and maintainability.
                """)
            .version("v1.0.0")
            .contact(new Contact()
                .name("Ram")
                .email("ramfornarayan@gmail.com")
                .url("https://github.com/ramfornarayan/recipe-management-service"))
            .license(new License()
                .name("Proprietary")
                .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    private SecurityScheme bearerSecurityScheme() {
        return new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }
}
