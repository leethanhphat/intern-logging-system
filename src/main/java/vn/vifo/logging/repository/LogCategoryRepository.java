package vn.vifo.logging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.vifo.logging.entity.LogCategory;

import java.util.UUID;

public interface LogCategoryRepository extends JpaRepository<LogCategory, UUID>, JpaSpecificationExecutor<LogCategory> {
    boolean existsByCodeAndIdNot(String code, UUID id);
    boolean existsByCode(String code);
}