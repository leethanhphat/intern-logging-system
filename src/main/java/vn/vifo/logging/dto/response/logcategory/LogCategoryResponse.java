package vn.vifo.logging.dto.response.logcategory;

import vn.vifo.logging.dto.response.AbstractBaseResponse;
import vn.vifo.logging.entity.LogCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class LogCategoryResponse extends AbstractBaseResponse {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "73d1d6de-44ad-41ac-9ac7-5ef6cda1ff99"
    )
    private String id;

    @Schema(
            name = "categoryName",
            description = "Type log of the log category",
            type = "String",
            example = "Error Logs"
    )
    private String categoryName;

    @Schema(
            name = "code",
            description = "Code of the log category",
            type = "String",
            example = "ERR001"
    )
    private String code;

    @Schema(
            name = "description",
            description = "Description of the log category",
            type = "String",
            example = "Logs for tracking errors in the system"
    )
    private String description;

    @Schema(
            name = "createdAt",
            description = "Date time field of log category creation",
            type = "LocalDateTime",
            example = "2025-02-17T06:07:13.98"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            description = "Date time field of log category update",
            type = "LocalDateTime",
            example = "2025-02-17T06:07:13.98"
    )
    private LocalDateTime updatedAt;

    @Schema(
            name = "isDeleted",
            description = "Is the log category deleted",
            type = "Boolean",
            example = "false"
    )
    private Boolean isDeleted;

    public static LogCategoryResponse convert(LogCategory logCategory) {
        return LogCategoryResponse.builder()
                .id(logCategory.getId().toString())
                .categoryName(logCategory.getCategoryName())
                .code(logCategory.getCode())
                .description(logCategory.getDescription())
                .createdAt(logCategory.getCreatedAt())
                .updatedAt(logCategory.getUpdatedAt())
                .build();
    }
}