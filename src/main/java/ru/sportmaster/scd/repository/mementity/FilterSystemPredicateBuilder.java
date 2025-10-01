package ru.sportmaster.scd.repository.mementity;

import static java.util.Objects.isNull;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.Builder;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.mementity.FilterSystemLogKey;
import ru.sportmaster.scd.entity.mementity.FilterSystemSelectedNumber;
import ru.sportmaster.scd.entity.mementity.FilterSystemSelectedNumberKey_;
import ru.sportmaster.scd.entity.mementity.FilterSystemSelectedNumber_;

@Builder
public class FilterSystemPredicateBuilder {
    private String formUUID;
    private String tableName;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<?> query;
    private Expression<?> filteringIdExpression;

    public Predicate getFilterPredicate(@NonNull Predicate predicate,
                                        @NonNull Byte typeSelection) {
        return
            criteriaBuilder.and(
                predicate,
                getNumFilterPredicate(typeSelection)
            );
    }

    private Predicate getNumFilterPredicate(@NonNull Byte typeSelection) {
        Subquery<FilterSystemSelectedNumber> queryFsNum = query.subquery(FilterSystemSelectedNumber.class);
        Root<FilterSystemSelectedNumber> rootFsNum = queryFsNum.from(FilterSystemSelectedNumber.class);
        var restrictions = criteriaBuilder.conjunction();
        restrictions =
            criteriaBuilder.and(
                restrictions,
                criteriaBuilder.equal(
                    rootFsNum.get(FilterSystemSelectedNumber_.ID).get(FilterSystemSelectedNumberKey_.FORM_UU_ID),
                    formUUID
                )
            );
        restrictions =
            criteriaBuilder.and(
                restrictions,
                criteriaBuilder.equal(
                    rootFsNum.get(FilterSystemSelectedNumber_.ID).get(FilterSystemSelectedNumberKey_.TABLE_NAME),
                    tableName
                )
            );
        restrictions =
            criteriaBuilder.and(
                restrictions,
                criteriaBuilder.equal(
                    rootFsNum.get(FilterSystemSelectedNumber_.ID).get(FilterSystemSelectedNumberKey_.SEL_VALUE),
                    filteringIdExpression
                )
            );
        queryFsNum
            .select(rootFsNum.get(FilterSystemSelectedNumber_.ID).get(FilterSystemSelectedNumberKey_.SEL_VALUE))
            .where(restrictions);
        if (typeSelection == 0) {
            return criteriaBuilder.exists(queryFsNum).not();
        }
        return criteriaBuilder.exists(queryFsNum);
    }

    public boolean nonValid() {
        return isNull(formUUID) || formUUID.isEmpty() || isNull(tableName) || tableName.isEmpty();
    }

    public FilterSystemLogKey getFilterSystemLogKey() {
        return
            FilterSystemLogKey.builder()
                .formUUID(formUUID)
                .tableName(tableName)
                .build();
    }
}
