package vn.vifo.logging.entity.specification.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class LogCategoryCriteria {
    private String categoryName;

    private String code;

    private String description;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private Boolean isDeleted;

    private String q;
}