package vn.vifo.logging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import vn.vifo.logging.entity.SourceRequest;
import vn.vifo.logging.entity.specification.SourceFilteeSpecification;
import vn.vifo.logging.entity.specification.criteria.PaginationCriteria;
import vn.vifo.logging.entity.specification.criteria.SourceCriteria;
import vn.vifo.logging.repository.SourceRequestRepository;
import vn.vifo.logging.util.PageRequestBuilder;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceService {
    @Autowired
    private SourceRequestRepository sourceRequestRepository;

    public ResponseEntity<String> updateSource(@PathVariable UUID id, @RequestBody SourceRequest source) {
        SourceRequest sourceSave = sourceRequestRepository.findById(id).orElse(null);

        if (sourceSave == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant find id");
        } else {
            sourceSave.setSourceName(source.getSourceName());
            sourceRequestRepository.save(sourceSave);

        }
        return ResponseEntity.ok("Source updated successfully");
    }

    public ResponseEntity<String> addSource(@RequestBody SourceRequest sourceRequest) {
        if(sourceRequest.getSourceIp() == null || sourceRequest.getSourceIp().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source ip is required");
        }
        if(sourceRequest.getSourceName() == null || sourceRequest.getSourceName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source name is required");
        }
        Optional<SourceRequest> existingSource = sourceRequestRepository.findBySourceName(sourceRequest.getSourceName());

        if (existingSource.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Source with this name already exists");
        }

        sourceRequestRepository.save(sourceRequest);
        return ResponseEntity.ok("Source added successfully");
    }


    public Page<SourceRequest> findAll(SourceCriteria criteria, PaginationCriteria paginationCriteria) {
        return sourceRequestRepository.findAll(new SourceFilteeSpecification(criteria),
                PageRequestBuilder.build(paginationCriteria));
    }
}
