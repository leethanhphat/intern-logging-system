package vn.vifo.logging.dto.request.log;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.vifo.logging.util.HttpMethod;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractBaseUpdateLogRequest {

    @NotBlank(message = "{not_blank}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{invalid_format}")
    @Size(max = 50, message = "{max_length}")
    @Schema(
            name = "typeLog",
            description = "Type of the log",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "INFO"
    )
    private String typeLog;

    @NotNull(message = "{not_null}")
    @Schema(
            name = "logCategoryId",
            description = "ID of the log category",
            type = "UUID",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "a07f4a3f-b732-4624-be16-ea5652fbe8c7"
    )
    private UUID logCategoryId;

    @NotBlank(message = "{not_blank}")
    @Size(max = 50, message = "{max_length}")
    @Schema(
            name = "environment",
            description = "Environment where the log occurred",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "production"
    )
    private String environment;

    @NotNull
    @Schema(
            name = "method",
            description = "HTTP method of the request",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "GET"
    )
    private HttpMethod method;

    @NotBlank(message = "{not_blank}")
    @Size(max = 255, message = "{max_length}")
    @Schema(
            name = "endpoint",
            description = "Endpoint of the request",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "/api/users"
    )
    private String endpoint;

    @Size(max = 1000, message = "{max_length}")
    @Schema(
            name = "header",
            description = "HTTP headers of the request",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "{'Content-Type': 'application/json'}"
    )
    private String header;

    @Size(max = 65535, message = "{max_length}")
    @Schema(
            name = "body",
            description = "Body of the request",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "{\"username\":\"johndoe\",\"password\":\"secret\"}"
    )
    private String body;

    @Schema(
            name = "responseTime",
            description = "Response time of the request in milliseconds",
            type = "Double",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "150.5"
    )
    private Double responseTime;

    @Size(max = 65535, message = "{max_length}")
    @Schema(
            name = "responseLog",
            description = "Response log of the request",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "{\"status\":200,\"message\":\"OK\"}"
    )
    private String responseLog;

    @Schema(
            name = "sourceId",
            description = "Source ID of the log",
            type = "UUID",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID sourceId;
}