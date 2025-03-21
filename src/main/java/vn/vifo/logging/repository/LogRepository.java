package vn.vifo.logging.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.vifo.logging.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vifo.logging.entity.specification.LogFilterSpecification;

import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID>, JpaSpecificationExecutor<Log> {
    boolean existsByLogCategoryId(UUID logCategoryId);
}
