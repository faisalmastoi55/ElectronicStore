package com.electronic.store.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
        title = "Electronics Store Backend : APIS",
        description = "This is backend project created by Faisal",
        summary = "ElectronicStore is commerce based project. In this project we will create complete REST APIS of Electronic store. We will cover different modules of electronic store like User Module, Category Module, Product Module, Cart Module, Order Module, Authentication Module, Api Documentation Module and Deployment using docker on cloud module etc.",
        termsOfService = "Terms of service",
        contact = @Contact(
                name = "Faisal Ali",
                email = "faisalmastoii55@gmail.com",
                url = "www.linkedin.com/in/faisalmastoi"
        ),

        license = @License(name = "Faisal License"),
        version = "Api/V1"
    ),
        servers = {
                @Server(description = "testEnv", url = "http://localhost:9090"),
                @Server(description = "devEnv", url = "http://localhost:9090")
        },

        security = @SecurityRequirement(name = "jwtAuthorization")
    )

@SecurityScheme(
        name = "jwtAuthorization",
        description = "This is my jwt security for Electronic Store Project",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
