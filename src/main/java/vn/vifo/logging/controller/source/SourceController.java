package vn.vifo.logging.controller.source;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.vifo.logging.dto.response.ErrorResponse;
import vn.vifo.logging.dto.response.source.SourceResponse;
import vn.vifo.logging.dto.response.source.SourcesPaginationResponse;
import vn.vifo.logging.dto.response.user.UsersPaginationResponse;
import vn.vifo.logging.entity.SourceRequest;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.entity.specification.criteria.SourceCriteria;
import vn.vifo.logging.service.MessageSourceService;
import vn.vifo.logging.service.SourceService;
import vn.vifo.logging.controller.AbstractBaseController;

import java.util.UUID;

import static vn.vifo.logging.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
public class SourceController extends AbstractBaseController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "sourceName", "sourceIp", "createdAt", "updatedAt"};
    private final MessageSourceService messageSourceService;
    private final SourceService sourceService;


    @Operation(
            summary = "Users list endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UsersPaginationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )

    @GetMapping("search-sources")
    public ResponseEntity<SourcesPaginationResponse> searchSources(
            @Parameter(name = "sourceName", description = "sourceName", example = "VIFO")
            @RequestParam(required = false) final String sourceName,

            @Parameter(name = "sortBy", description = "Sort by column", example = "createdAt",
                    schema = @Schema(type = "String", allowableValues = {"createdAt", "updatedAt"}))
            @RequestParam(defaultValue = "createdAt", required = false) final String sortBy,

            @Parameter(name = "page", description = "Page number", example = "1")
            @RequestParam(defaultValue = "1", required = false) final Integer page,

            @Parameter(name = "size", description = "Page size", example = "20")
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}", required = false) final Integer size,

            @Parameter(name = "sort", description = "Sort direction", schema = @Schema(type = "string",
                    allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc", required = false) @Pattern(regexp = "asc|desc") final String sort
    ) {
        sortColumnCheck(messageSourceService, SORT_COLUMNS, sortBy);
        Page<SourceRequest> sourceRequest = sourceService.findAll(
                SourceCriteria.builder()
                        .sourceName(sourceName)
                        .build(),
                PaginationCriteria.builder()
                        .page(page)
                        .size(size)
                        .sortBy(sortBy)
                        .sort(sort)
                        .columns(SORT_COLUMNS)
                        .build()
        );
        return ResponseEntity.ok(new SourcesPaginationResponse(
                sourceRequest, sourceRequest.stream()
                .map(SourceResponse::fromEntity)
                .toList()
        ));
    }

    @PostMapping("add-sources")
    public ResponseEntity<String> addSources(@RequestBody SourceRequest  body) {
        return sourceService.addSource(body);
    }

    @PostMapping("/update-sources/{id}")
    public ResponseEntity<String> updateLogs(@PathVariable UUID id, @RequestBody SourceRequest source) {
        return sourceService.updateSource(id, source);
    }
}
