package vn.vifo.logging.entity.specification.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.vifo.logging.util.HttpMethod;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class LogCriteria {
    private String typeLog;

    private UUID logCategoryId;

    private String environment;

    private HttpMethod method;

    private String endpoint;

    private UUID sourceId;

    private LocalDateTime logTimestampStart;

    private LocalDateTime logTimestampEnd;

    private Boolean isDeleted;

    private String q;
}