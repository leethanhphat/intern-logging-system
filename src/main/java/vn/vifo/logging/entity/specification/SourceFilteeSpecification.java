package vn.vifo.logging.entity.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import vn.vifo.logging.entity.SourceRequest;
import vn.vifo.logging.entity.specification.criteria.SourceCriteria;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SourceFilteeSpecification implements Specification<SourceRequest> {
    private final SourceCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull final Root<SourceRequest> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder builder) {

        if (criteria == null) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSourceName() != null) {
            predicates.add(builder.equal(root.get("sourceName"), criteria.getSourceName()));
        }


        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.distinct(true).getRestriction();
    }
}
