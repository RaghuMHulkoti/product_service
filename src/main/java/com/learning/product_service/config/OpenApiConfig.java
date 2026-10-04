package com.learning.product_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("Master catalog: products, categories, brands, category attributes and media. "
                                + "Holds no price and no stock: price comes from Supplier Service offers, "
                                + "stock from Inventory Service.")
                        .version("1.0.0"))
                .addServersItem(new Server()
                        .url("http://localhost:8082")
                        .description("Local Development Server"));
    }
}
