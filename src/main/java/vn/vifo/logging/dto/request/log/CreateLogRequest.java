package vn.vifo.logging.dto.request.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateLogRequest extends AbstractBaseCreateLogRequest {

    @Schema(
            name = "logTimestamp",
            description = "Timestamp of the log",
            type = "LocalDateTime",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "2023-10-02T15:01:00"
    )
    private LocalDateTime logTimestamp;
}