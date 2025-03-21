package vn.vifo.logging.service;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindException;
import vn.vifo.logging.dto.request.log.CreateLogRequest;
import vn.vifo.logging.dto.request.log.UpdateLogRequest;
import vn.vifo.logging.entity.Log;
import vn.vifo.logging.entity.specification.criteria.LogCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;

import java.util.UUID;

public interface ILogService {
    Page<Log> findAll(LogCriteria criteria, PaginationCriteria paginationCriteria);

    Log findById(UUID id);

    Log findById(String id);

    Log create(CreateLogRequest request) throws BindException;

    Log update(UUID id, UpdateLogRequest request);

    Log update(String id, UpdateLogRequest request);

    void delete(UUID id);

    void delete(String id);
}
