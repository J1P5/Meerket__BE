package org.j1p5.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server prodServer = new Server();
        Server devServer = new Server();
        Server localServer = new Server();

        prodServer.setUrl("https://meerket-prod.store");
        devServer.setUrl("https://meerket.store");
        localServer.setUrl("http://localhost:8080");
        return new OpenAPI().components(new Components()).info(apiInfo()).servers(List.of(prodServer, devServer, localServer));
    }

    private Info apiInfo() {
        return new Info()
                .title("Meerket Swagger")
                .description("Meerket 서비스에 관한 REST API")
                .version("1.0.0");
    }
}
