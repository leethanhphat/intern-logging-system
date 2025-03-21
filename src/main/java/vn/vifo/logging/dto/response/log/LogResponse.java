package vn.vifo.logging.dto.response.log;

import vn.vifo.logging.dto.response.AbstractBaseResponse;
import vn.vifo.logging.entity.Log;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.vifo.logging.util.HttpMethod;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class LogResponse extends AbstractBaseResponse {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "73d1d6de-44ad-41ac-9ac7-5ef6cda1ff99"
    )
    private String id;

    @Schema(
            name = "typeLog",
            description = "Type of the log",
            type = "String",
            example = "INFO"
    )
    private String typeLog;

    @Schema(
            name = "logCategoryId",
            description = "ID of the log category",
            type = "String",
            example = "e756c92f-43ce-46fe-a02b-17081463dea2"
    )
    private String logCategoryId;

    @Schema(
            name = "environment",
            description = "Environment where the log occurred",
            type = "String",
            example = "production"
    )
    private String environment;

    @Schema(
            name = "method",
            description = "HTTP method of the request",
            type = "String",
            example = "GET"
    )
    private HttpMethod method;

    @Schema(
            name = "endpoint",
            description = "Endpoint of the request",
            type = "String",
            example = "/api/users"
    )
    private String endpoint;

    @Schema(
            name = "header",
            description = "HTTP headers of the request",
            type = "String",
            example = "{'Content-Type': 'application/json'}"
    )
    private String header;

    @Schema(
            name = "body",
            description = "Body of the request",
            type = "String",
            example = "{\"username\":\"johndoe\",\"password\":\"secret\"}"
    )
    private String body;

    @Schema(
            name = "responseTime",
            description = "Response time of the request in milliseconds",
            type = "Double",
            example = "150.5"
    )
    private Double responseTime;

    @Schema(
            name = "responseLog",
            description = "Response log of the request",
            type = "String",
            example = "{\"status\":200,\"message\":\"OK\"}"
    )
    private String responseLog;

    @Schema(
            name = "sourceId",
            description = "Source ID of the log",
            type = "String",
            example = "73d1d6de-44ad-41ac-9ac7-5ef6cda1ff99"
    )
    private String sourceId;

    @Schema(
            name = "logTimestamp",
            description = "Timestamp of the log",
            type = "LocalDateTime",
            example = "2023-10-02T15:01:00"
    )
    private LocalDateTime logTimestamp;

    @Schema(
            name = "createdAt",
            description = "Date time field of log creation",
            type = "LocalDateTime",
            example = "2025-02-17T06:07:13.98"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            description = "Date time field of log update",
            type = "LocalDateTime",
            example = "2025-02-17T06:07:13.98"
    )
    private LocalDateTime updatedAt;

    @Schema(
            name = "isDeleted",
            description = "Define status Log",
            type = "Boolean",
            example = "false"
    )
    private boolean isDeleted;

    public static LogResponse convert(Log log) {
        return LogResponse.builder()
                .id(log.getId().toString())
                .typeLog(log.getTypeLog())
                .logCategoryId(log.getLogCategoryId().toString())
                .method(log.getMethod())
                .endpoint(log.getEndpoint())
                .body(log.getBody())
                .logTimestamp(log.getLogTimestamp())
                .createdAt(log.getCreatedAt())
                .updatedAt(log.getUpdatedAt())
//                .environment(log.getEnvironment())
//                .header(log.getHeader())
//                .responseTime(log.getResponseTime())
//                .responseLog(log.getResponseLog())
//                .sourceId(log.getSourceId() != null ? log.getSourceId().toString() : null)
                .build();
    }
}