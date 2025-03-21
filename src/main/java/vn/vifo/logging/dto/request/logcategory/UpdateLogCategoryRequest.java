package vn.vifo.logging.dto.request.logcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateLogCategoryRequest{

    @Size(max = 100, message = "{max_length}")
    @Schema(
            name = "categoryName",
            description = "Type log of the log category",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "Error Logs"
    )
    private String categoryName;

    @Size(max = 50, message = "{max_length}")
    @Schema(
            name = "code",
            description = "Code of the log category",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "ERR001"
    )
    private String code;

    @Size(max = 255, message = "{max_length}")
    @Schema(
            name = "description",
            description = "Description of the log category",
            type = "String",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "Logs for tracking errors in the system"
    )
    private String description;
}