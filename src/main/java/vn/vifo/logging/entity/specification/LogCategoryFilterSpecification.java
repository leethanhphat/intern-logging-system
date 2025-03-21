package vn.vifo.logging.entity.specification;

import vn.vifo.logging.entity.LogCategory;
import vn.vifo.logging.entity.specification.criteria.LogCategoryCriteria;
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
public final class LogCategoryFilterSpecification implements Specification<LogCategory> {
    private final LogCategoryCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull final Root<LogCategory> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder builder) {
        if (criteria == null) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getCategoryName() != null && !criteria.getCategoryName().isEmpty()) {
            predicates.add(
                    builder.like(builder.lower(root.get("categoryName")),
                            String.format("%%%s%%", criteria.getCategoryName().toLowerCase())
                    )
            );
        }

        if (criteria.getCode() != null && !criteria.getCode().isEmpty()) {
            predicates.add(
                    builder.like(builder.lower(root.get("code")),
                            String.format("%%%s%%", criteria.getCode().toLowerCase())
                    )
            );
        }

        if (criteria.getCreatedAtStart() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get("createdAt"), criteria.getCreatedAtStart())
            );
        }

        if (criteria.getCreatedAtEnd() != null) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get("createdAt"), criteria.getCreatedAtEnd())
            );
        }

        // Luôn lọc theo soft delete: nếu criteria không cung cấp isDeleted thì mặc định chỉ lấy bản ghi chưa bị xóa
        if (criteria.getIsDeleted() != null) {
            predicates.add(builder.equal(root.get("isDeleted"), criteria.getIsDeleted()));
        } else {
            predicates.add(builder.equal(root.get("isDeleted"), false));
        }

        if (criteria.getQ() != null && !criteria.getQ().isEmpty()) {
            String q = String.format("%%%s%%", criteria.getQ().toLowerCase());
            predicates.add(
                    builder.or(
                            builder.like(builder.lower(root.get("categoryName")), q),
                            builder.like(builder.lower(root.get("code")), q),
                            builder.like(builder.lower(root.get("description")), q)
                    )
            );
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.distinct(true).getRestriction();
    }
}