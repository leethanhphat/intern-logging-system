package vn.vifo.logging.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import vn.vifo.logging.dto.request.log.CreateLogRequest;
import vn.vifo.logging.dto.request.log.UpdateLogRequest;
import vn.vifo.logging.entity.Log;
import vn.vifo.logging.entity.specification.LogFilterSpecification;
import vn.vifo.logging.entity.specification.criteria.LogCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.exception.NotFoundException;
import vn.vifo.logging.repository.LogRepository;
import vn.vifo.logging.util.PageRequestBuilder;
import jakarta.transaction.Transactional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LogService implements ILogService {

    private final LogRepository logRepository;
    private final MessageSourceService messageSourceService;
    private final LogCategoryService logCategoryService;

    @Override
    public Page<Log> findAll(LogCriteria criteria, PaginationCriteria paginationCriteria) {
            return logRepository.findAll(new LogFilterSpecification(criteria),
                    PageRequestBuilder.build(paginationCriteria));
    }

    @Override
    public Log findById(UUID id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Log not found with id: " + id));
    }

    @Override
    public Log findById(String id){
        return findById(UUID.fromString(id));
    }

    private Log createLog(CreateLogRequest request) {

        return Log.builder()
                .typeLog(request.getTypeLog())
                .logCategoryId(request.getLogCategoryId())
                .environment(request.getEnvironment())
                .method(request.getMethod())
                .endpoint(request.getEndpoint())
                .header(request.getHeader())
                .body(request.getBody())
                .responseTime(request.getResponseTime())
                .responseLog(request.getResponseLog())
                .sourceId(request.getSourceId())
                .build();
    }
    @Override
    public Log create(CreateLogRequest request) throws BindException {
        log.info("Create log with log category: {}", request.getTypeLog());
        Log log = createLog(request);
        logRepository.save(log);
        return log;
    }

    @Override
    public Log update(UUID id, UpdateLogRequest request){
        Log log = findById(id);

        if (log.getIsDeleted()) {
            throw new IllegalStateException("Cannot update a deleted log with id: " + id);
        }

        log.setTypeLog(request.getTypeLog());
        log.setLogCategoryId(request.getLogCategoryId());
        log.setEnvironment(request.getEnvironment());
        log.setMethod(request.getMethod());
        log.setEndpoint(request.getEndpoint());
        log.setHeader(request.getHeader());
        log.setBody(request.getBody());
        log.setResponseTime(request.getResponseTime());
        log.setResponseLog(request.getResponseLog());
        log.setSourceId(request.getSourceId());
        log.setLogTimestamp(request.getLogTimestamp());

        logRepository.save(log);
        return log;
    }

    @Override
    public Log update(String id, UpdateLogRequest request){
        return update(UUID.fromString(id), request);
    }

    @Override
    public void delete(UUID id) {
        Log log = findById(id);
        log.setIsDeleted(true);
        logRepository.save(log);
    }

    @Override
    public void delete(String id){
        delete(UUID.fromString(id));
    }
}