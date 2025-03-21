package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vn.vifo.logging.entity.LogCategory;
import vn.vifo.logging.entity.specification.criteria.LogCategoryCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.exception.NotFoundException;
import vn.vifo.logging.repository.LogCategoryRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogCategoryServiceTest {

    @Mock
    private LogCategoryRepository logCategoryRepository;

    @InjectMocks
    private LogCategoryService logCategoryService;

    private UUID logCategoryId;
    private LogCategory logCategory;

    @BeforeEach
    void setUp() {
        logCategoryId = UUID.randomUUID();
        logCategory = new LogCategory();
        logCategory.setId(logCategoryId);
        logCategory.setCategoryName("Test Category");
        logCategory.setCode("TEST123");
        logCategory.setIsDeleted(false);
    }

    @Test
    void findById_ShouldReturnLogCategory_WhenIdExists() {
        when(logCategoryRepository.findById(logCategoryId)).thenReturn(Optional.of(logCategory));

        LogCategory foundCategory = logCategoryService.findById(logCategoryId);

        assertNotNull(foundCategory);
        assertEquals(logCategory.getId(), foundCategory.getId());
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(logCategoryRepository.findById(logCategoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> logCategoryService.findById(logCategoryId));
    }

    @Test
    void findAll_ShouldReturnPaginatedResult() {
        LogCategoryCriteria criteria = new LogCategoryCriteria();
        PaginationCriteria paginationCriteria = new PaginationCriteria(0, 10);
        Page<LogCategory> page = new PageImpl<>(Collections.singletonList(logCategory), PageRequest.of(0, 10), 1);

        when(logCategoryRepository.findAll(any(), any())).thenReturn(page);

        Page<LogCategory> result = logCategoryService.findAll(criteria, paginationCriteria);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }
}
