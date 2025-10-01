package ru.sportmaster.scd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.sportmaster.scd.service.docs.OpenApiViewLoader;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {
    public static final String AUTH_NAME = "bearerAuth";

    private final OpenApiViewLoader openApiViewLoader;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SCD SM")
                .description("Проект Supply Chain Demand SM (Система планирования цепочек поставок "
                    + "- загрузка и допланирование)"))
            .schemaRequirement(AUTH_NAME, securityScheme());
    }

    @Bean
    public OpenApiCustomizer globalHandlerOpenApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses responses = operation.getResponses();
                responses.addApiResponse(
                    getStatusStrCode(HttpStatus.BAD_REQUEST),
                    buildApiResponse("Ошибка запроса.", null)
                );
                responses.addApiResponse(
                    getStatusStrCode(HttpStatus.FORBIDDEN),
                    buildApiResponse("Ошибка авторизации.", null)
                );
                responses.addApiResponse(
                    getStatusStrCode(HttpStatus.INTERNAL_SERVER_ERROR),
                    buildApiResponse("Внутренняя ошибка", null)
                );
            }));
            openApiViewLoader.load(openApi);

            var sortedSchemas = new TreeMap<>(openApi.getComponents().getSchemas());
            openApi.getComponents().setSchemas(sortedSchemas);
        };
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }

    private ApiResponse buildApiResponse(String description, Schema<?> schema) {
        Content content = null;
        if (schema != null) {
            content = new Content().addMediaType(
                MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().schema(schema)
            );
        }
        return new ApiResponse().description(description).content(content);
    }

    private String getStatusStrCode(HttpStatus status) {
        return String.valueOf(status.value());
    }
}
