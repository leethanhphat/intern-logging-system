package vn.vifo.logging.service;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindException;
import vn.vifo.logging.dto.request.logcategory.CreateLogCategoryRequest;
import vn.vifo.logging.dto.request.logcategory.UpdateLogCategoryRequest;
import vn.vifo.logging.entity.LogCategory;
import vn.vifo.logging.entity.specification.criteria.LogCategoryCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;

import java.util.UUID;

public interface ILogCategoryService {
    Page<LogCategory> findAll(LogCategoryCriteria criteria, PaginationCriteria paginationCriteria);

    LogCategory findById(UUID id);

    LogCategory findById(String id);

    LogCategory create(CreateLogCategoryRequest request) throws BindException;

    LogCategory update(UUID id, UpdateLogCategoryRequest request);

    LogCategory update(String id, UpdateLogCategoryRequest request);

    void delete(UUID id);

    void delete(String id);
}
