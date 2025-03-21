package vn.vifo.logging.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import vn.vifo.logging.util.HttpMethod;

import java.util.UUID;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Log extends BaseEntityWithSoftDelete {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Pattern(regexp = "^[a-zA-Z0-9_\\s]+$")
    @Column(name = "typelog", nullable = false, length = 50)
    private String typeLog;

    @Column(name = "log_category_id", nullable = false)
    private UUID logCategoryId;

    @Column(name = "environment", nullable = false)
    private String environment;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private HttpMethod method;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "header")
    private String header;

    @Column(name = "body")
    private String body;

    @Column(name = "response_time", nullable = false)
    private Double responseTime;

    @Column(name = "response_log")
    private String responseLog;

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "log_timestamp", nullable = false)

    private LocalDateTime logTimestamp;
}