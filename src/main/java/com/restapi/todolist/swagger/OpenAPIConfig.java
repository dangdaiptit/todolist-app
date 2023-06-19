package com.restapi.todolist.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8000");
        localServer.setDescription("Server URL in Local environment");
        Server productionServer = new Server();
        productionServer.setUrl("https://mytodobackend.herokuapp.com");
        productionServer.description("Server URL in Production environment");
        Info info = new Info()
                .title("TODO MANAGER API")
                .version("1.0")
                .description("This API exposes endpoints for users to manage their todos");
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer));
    }
}
