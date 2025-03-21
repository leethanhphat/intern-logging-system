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
import vn.vifo.logging.entity.Log;
import vn.vifo.logging.entity.specification.criteria.LogCriteria;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.exception.NotFoundException;
import vn.vifo.logging.repository.LogRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    private UUID logId;
    private Log log;

    @BeforeEach
    void setUp() {
        logId = UUID.randomUUID();
        log = new Log();
        log.setId(logId);
        log.setMessage("Test log message");
        log.setCategory("TEST_CATEGORY");
        log.setIsDeleted(false);
    }

    @Test
    void findById_ShouldReturnLog_WhenIdExists() {
        when(logRepository.findById(logId)).thenReturn(Optional.of(log));

        Log foundLog = logService.findById(logId);

        assertNotNull(foundLog);
        assertEquals(log.getId(), foundLog.getId());
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> logService.findById(logId));
    }

    @Test
    void findAll_ShouldReturnPaginatedResult() {
        LogCriteria criteria = new LogCriteria();
        PaginationCriteria paginationCriteria = new PaginationCriteria(0, 10);
        Page<Log> page = new PageImpl<>(Collections.singletonList(log), PageRequest.of(0, 10), 1);

        when(logRepository.findAll(any(), any())).thenReturn(page);

        Page<Log> result = logService.findAll(criteria, paginationCriteria);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }
}
