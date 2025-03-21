package vn.vifo.logging.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Builder
@Entity
@Table(name = "log_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogCategory extends BaseEntityWithSoftDelete {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_\\s]+$")
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

}