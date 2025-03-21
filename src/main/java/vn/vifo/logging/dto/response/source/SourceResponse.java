package vn.vifo.logging.dto.response.source;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.vifo.logging.entity.SourceRequest;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class SourceResponse {
    private UUID id;
    private String sourceName;
    private String sourceIp;

    public static SourceResponse fromEntity(SourceRequest sourceRequest) {
        return SourceResponse.builder()
                .id(sourceRequest.getId())
                .sourceName(sourceRequest.getSourceName())
                .sourceIp(sourceRequest.getSourceIp())
                .build();
    }
}
