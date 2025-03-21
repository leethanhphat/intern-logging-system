package vn.vifo.logging.controller.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.vifo.logging.controller.AbstractBaseController;
import vn.vifo.logging.dto.request.logcategory.CreateLogCategoryRequest;
import vn.vifo.logging.dto.request.logcategory.UpdateLogCategoryRequest;
import vn.vifo.logging.dto.response.ErrorResponse;
import vn.vifo.logging.dto.response.logcategory.LogCategoryPaginationResponse;
import vn.vifo.logging.dto.response.logcategory.LogCategoryResponse;
import vn.vifo.logging.entity.LogCategory;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.entity.specification.criteria.LogCategoryCriteria;
import vn.vifo.logging.service.LogCategoryService;
import vn.vifo.logging.service.MessageSourceService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import static vn.vifo.logging.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log-category")
@Tag(name = "Log Category", description = "Log Category API")
public class LogCategoryController extends AbstractBaseController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "name", "code", "createdAt", "updatedAt"};
    private final LogCategoryService logCategoryService;
    private final MessageSourceService messageSourceService;

    @GetMapping
    @Operation(
            summary = "Log Category list endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LogCategoryPaginationResponse.class)
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
    public ResponseEntity<LogCategoryPaginationResponse> list(
            @Parameter(name = "categoryName", description = "Category name of the log category", example = "Error Logs")
            @RequestParam(required = false) final String categoryName,
            @Parameter(name = "code", description = "Code of the log category", example = "ERR001")
            @RequestParam(required = false) final String code,
            @Parameter(name = "createdAtStart", description = "Created date start", example = "2022-10-25T22:54:58")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime createdAtStart,
            @Parameter(name = "createdAtEnd", description = "Created date end", example = "2022-10-25T22:54:58")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime createdAtEnd,
            @Parameter(name = "page", description = "Page number", example = "1")
            @RequestParam(defaultValue = "1", required = false) final Integer page,
            @Parameter(name = "size", description = "Page size", example = "20")
            @RequestParam(defaultValue = "20", required = false) final Integer size,
            @Parameter(name = "sortBy", description = "Sort by column", example = "createdAt", schema = @Schema(type = "String", allowableValues = {"id", "name", "code", "createdAt", "updatedAt"}))
            @RequestParam(defaultValue = "createdAt", required = false) final String sortBy,
            @Parameter(name = "sort", description = "Sort direction", schema = @Schema(type = "string", allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc", required = false) final String sort
    ) {
        sortColumnCheck(messageSourceService, SORT_COLUMNS, sortBy);
        Page<LogCategory> logCategories = logCategoryService.findAll(
                LogCategoryCriteria.builder()
                        .categoryName(categoryName)
                        .code(code)
                        .createdAtStart(createdAtStart)
                        .createdAtEnd(createdAtEnd)
                        .build(),
                PaginationCriteria.builder()
                        .page(page)
                        .size(size)
                        .sortBy(sortBy)
                        .sort(sort)
                        .columns(SORT_COLUMNS)
                        .build()
        );

        return ResponseEntity.ok(new LogCategoryPaginationResponse(logCategories, logCategories.stream()
                .map(LogCategoryResponse::convert)
                .toList()));
    }

    @PostMapping
    @Operation(
            summary = "Create log category endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Success operation",
                            content = @Content(schema = @Schema(hidden = true)),
                            headers = @Header(
                                    name = "Location",
                                    description = "Location of created log category",
                                    schema = @Schema(type = "string"))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<LogCategoryResponse> create(
            @Parameter(description = "Request body to log category create", required = true)
            @RequestBody @Valid final CreateLogCategoryRequest request
    ) throws BindException {
        LogCategory logCategory = logCategoryService.create(request);
        return ResponseEntity.ok(LogCategoryResponse.convert(logCategory));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Show log category endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LogCategoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<LogCategoryResponse> show(
            @Parameter(name = "id", description = "Log Category ID", required = true)
            @PathVariable("id") final UUID id)
    {
        return ResponseEntity.ok(LogCategoryResponse.convert(logCategoryService.findById(id)));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update log category endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LogCategoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<LogCategoryResponse> update(
            @Parameter(name = "id", description = "Log Category ID", required = true)
            @PathVariable("id") final UUID id,
            @Parameter(description = "Request body to update log category", required = true)
            @RequestBody @Valid final UpdateLogCategoryRequest request
    ) {
        return ResponseEntity.ok(LogCategoryResponse.convert(logCategoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete log category endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Success operation",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "Log Category ID", required = true)
            @PathVariable("id") final UUID id
    ) {
        logCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}