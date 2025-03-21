package vn.vifo.logging.entity.specification;

import vn.vifo.logging.entity.Log;
import vn.vifo.logging.entity.specification.criteria.LogCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class LogFilterSpecification implements Specification<Log> {
    private final LogCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull final Root<Log> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder builder) {
        if (criteria == null) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getTypeLog() != null && !criteria.getTypeLog().isEmpty()) {
            predicates.add(
                    builder.like(builder.lower(root.get("typeLog")), "%" + criteria.getTypeLog().toLowerCase() + "%"));
        }

        if (criteria.getLogCategoryId() != null) {
            predicates.add(builder.equal(root.get("logCategoryId"), criteria.getLogCategoryId()));
        }

        if (criteria.getEnvironment() != null && !criteria.getEnvironment().isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("environment")), "%" + criteria.getEnvironment().toLowerCase() + "%"));
        }

        if (criteria.getMethod() != null){
            predicates.add(builder.equal(root.get("method"), criteria.getMethod()));
        }

        if (criteria.getEndpoint() != null && !criteria.getEndpoint().isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("endpoint")), "%" + criteria.getEndpoint().toLowerCase() + "%"));
        }

        if (criteria.getSourceId() != null) {
            predicates.add(builder.equal(root.get("sourceId"), criteria.getSourceId()));
        }

        if (criteria.getLogTimestampStart() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("logTimestamp"), criteria.getLogTimestampStart()));
        }

        if (criteria.getLogTimestampEnd() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("logTimestamp"), criteria.getLogTimestampEnd()));
        }

        if (criteria.getIsDeleted() != null) {
            predicates.add(builder.equal(root.get("isDeleted"), criteria.getIsDeleted()));
        }
        else {
            predicates.add(builder.equal(root.get("isDeleted"), false));
        }

        // General search (q parameter)
        if (criteria.getQ() != null && !criteria.getQ().isEmpty()) {
            String q = "%" + criteria.getQ().toLowerCase() + "%";
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get("typeLog")), q),
                    builder.like(builder.lower(root.get("environment")), q),
                    builder.like(builder.lower(root.get("method")), q),
                    builder.like(builder.lower(root.get("endpoint")), q),
                    builder.like(builder.lower(root.get("header")), q),
                    builder.like(builder.lower(root.get("body")), q),
                    builder.like(builder.lower(root.get("responseLog")), q)
            ));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.distinct(true).getRestriction();
    }
}