package vn.vifo.logging.service;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import vn.vifo.logging.dto.request.logcategory.CreateLogCategoryRequest;
import vn.vifo.logging.dto.request.logcategory.UpdateLogCategoryRequest;
import vn.vifo.logging.entity.LogCategory;
import vn.vifo.logging.entity.specification.LogCategoryFilterSpecification;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.entity.specification.criteria.LogCategoryCriteria;
import vn.vifo.logging.exception.NotFoundException;
import vn.vifo.logging.repository.LogCategoryRepository;
import vn.vifo.logging.util.PageRequestBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LogCategoryService implements ILogCategoryService {
    private final LogCategoryRepository logCategoryRepository;
    private final MessageSourceService messageSourceService;

    @Override
    public Page<LogCategory> findAll(LogCategoryCriteria criteria, PaginationCriteria paginationCriteria) {
        return logCategoryRepository.findAll(new LogCategoryFilterSpecification(criteria),
                PageRequestBuilder.build(paginationCriteria));
    }

    @Override
    public LogCategory findById(UUID id) {
            return logCategoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Log category not found with id: " + id));
    }

    @Override
    public LogCategory findById(String id) {
            return findById(UUID.fromString(id));
    }

    private LogCategory createLogCategory(CreateLogCategoryRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (logCategoryRepository.existsByCode(request.getCode())) {
            LogCategoryService.log.error("Code already exists with code: {}", request.getCode());
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "code",
                    messageSourceService.get("Code already exists")));
        }
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return LogCategory.builder()
                .categoryName(request.getCategoryName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
    }

    @Override
    public LogCategory create(CreateLogCategoryRequest request) throws BindException {
        log.info("Creating log category: {}", request.getCategoryName());
        LogCategory logCategory = createLogCategory(request);
        logCategoryRepository.save(logCategory);
        log.info("Log category created with name: {}, {}", logCategory.getCategoryName(), logCategory.getId());
        return logCategory;
    }

    @Override
    public LogCategory update(UUID id, UpdateLogCategoryRequest request) {
        LogCategory logCategory = findById(id);

        if (logCategoryRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new IllegalArgumentException("Code already exists with code: " + request.getCode());
        }
        if (logCategory.getIsDeleted()) {
            throw new IllegalStateException("Cannot update a deleted log with id: " + id);
        }

        logCategory.setCategoryName(request.getCategoryName());
        logCategory.setCode(request.getCode());
        logCategory.setDescription(request.getDescription());

        logCategoryRepository.save(logCategory);
        log.info("Log category updated with id: {}", logCategory.getId());
        return logCategory;
    }

    @Override
    public LogCategory update(String id, UpdateLogCategoryRequest request) {
        return update(UUID.fromString(id), request);
    }

    public void delete(UUID id) {
        LogCategory logCategory = findById(id);
        logCategory.setIsDeleted(true);
        logCategoryRepository.save(logCategory);
    }

    @Override
    public void delete(String id) {
        delete(UUID.fromString(id));
    }
}