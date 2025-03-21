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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import vn.vifo.logging.controller.AbstractBaseController;
import vn.vifo.logging.dto.request.log.CreateLogRequest;
import vn.vifo.logging.dto.request.log.UpdateLogRequest;
import vn.vifo.logging.dto.response.ErrorResponse;
import vn.vifo.logging.dto.response.log.LogPaginationResponse;
import vn.vifo.logging.dto.response.log.LogResponse;
import vn.vifo.logging.entity.Log;
import vn.vifo.logging.entity.specification.criteria.LogCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.service.LogCategoryService;
import vn.vifo.logging.service.LogService;
import vn.vifo.logging.service.MessageSourceService;
import vn.vifo.logging.util.HttpMethod;

import java.time.LocalDateTime;
import java.util.UUID;

import static vn.vifo.logging.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Tag(name = "Log", description = "Log API")
public class LogController extends AbstractBaseController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "logTimestamp", "createdAt", "updatedAt", "responseTime", "typeLog", "environment"};
    private final LogService logService;
    private final MessageSourceService messageSourceService;

    @GetMapping
    @Operation(
            summary = "Logs list endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LogPaginationResponse.class)
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
    public ResponseEntity<LogPaginationResponse> list(
            @Parameter(name = "typeLog", description = "Type of the log", example = "INFO")
            @RequestParam(required = false) final String typeLog,
            @Parameter(name = "environment", description = "Environment of the log", example = "PRODUCTION")
            @RequestParam(required = false) final String environment,
            @Parameter(name = "method", description = "Method of the log", example = "GET")
            @RequestParam(required = false) final HttpMethod method,
            @Parameter(name = "endpoint", description = "Endpoint of the log", example = "/users")
            @RequestParam(required = false) final String endpoint,
            @Parameter(name = "logTimestampStart", description = "Log timestamp start", example = "2022-10-25T22:54:58")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime logTimestampStart,
            @Parameter(name = "logTimestampEnd", description = "Log timestamp end", example = "2022-10-25T22:54:58")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime logTimestampEnd,
            @Parameter(name = "page", description = "Page number", example = "1")
            @RequestParam(defaultValue = "1", required = false) final Integer page,
            @Parameter(name = "size", description = "Page size", example = "20")
            @RequestParam(defaultValue = "20", required = false) final Integer size,
            @Parameter(name = "sortBy", description = "Sort by column", example = "logTimestamp", schema =
            @Schema(type = "String", allowableValues = {"id", "typeLog", "logTimestamp", "createdAt", "updatedAt"}))
            @RequestParam(defaultValue = "logTimestamp", required = false) final String sortBy,
            @Parameter(name = "sort", description = "Sort direction", schema =
            @Schema(type = "string", allowableValues = {"asc", "desc"}, defaultValue = "desc"))
            @RequestParam(defaultValue = "desc", required = false) final String sort
    ) {
        sortColumnCheck(messageSourceService, SORT_COLUMNS, sortBy);
        Page<Log> logs = logService.findAll(
                LogCriteria.builder()
                        .typeLog(typeLog)
                        .environment(environment)
                        .method(method)
                        .endpoint(endpoint)
                        .logTimestampStart(logTimestampStart)
                        .logTimestampEnd(logTimestampEnd)
                        .build(),
                PaginationCriteria.builder()
                        .page(page)
                        .size(size)
                        .sortBy(sortBy)
                        .sort(sort)
                        .columns(SORT_COLUMNS)
                        .build());
        return ResponseEntity.ok(new LogPaginationResponse(logs, logs.stream()
                .map(LogResponse::convert)
                .toList()));
    }

    @PostMapping
    @Operation(summary = "Create log endpoint", security = @SecurityRequirement(name = SECURITY_SCHEME_NAME), responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Success operation",
                    content = @Content(schema = @Schema(hidden = true)),
                    headers = @Header(
                            name = "Location",
                            description = "Location of created log",
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
    })
    public ResponseEntity<LogResponse> create(
            @Parameter(description = "Request body to log create", required = true)
            @RequestBody @Valid  CreateLogRequest request
    ) throws BindException {
        Log log = logService.create(request);
        return ResponseEntity.ok(LogResponse.convert(log));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Show log endpoint", security = @SecurityRequirement(name = SECURITY_SCHEME_NAME), responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success operation",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LogResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Full authentication is required to access this resource",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<LogResponse> show
            (@Parameter(name = "id", description = "Log ID", required = true)
             @PathVariable("id") final UUID id)
    {
        return ResponseEntity.ok(LogResponse.convert(logService.findById(id)));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update log endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LogResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<LogResponse> update(
            @Parameter(name = "id", description = "Log ID", required = true)
            @PathVariable("id") final UUID id,
            @Parameter(description = "Request body to update log", required = true)
            @RequestBody @Valid UpdateLogRequest request
    ) {
        return ResponseEntity.ok(LogResponse.convert(logService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete log endpoint",
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
            @Parameter(name = "id", description = "Log ID", required = true)
            @PathVariable("id") final UUID id
    ) {
            logService.delete(id);
            return ResponseEntity.notFound().build();
    }
}