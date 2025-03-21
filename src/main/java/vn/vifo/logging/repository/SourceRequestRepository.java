package vn.vifo.logging.repository;

import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.vifo.logging.entity.SourceRequest;

import java.util.Optional;
import java.util.UUID;

public interface SourceRequestRepository extends JpaRepository<SourceRequest, UUID>, JpaSpecificationExecutor<SourceRequest> {
    Optional<SourceRequest> findBySourceName(String sourceName);

    Optional<SourceRequest> findById(UUID id);
}
