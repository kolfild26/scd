package ru.sportmaster.scd.repository.replenishment;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.Category;
import ru.sportmaster.scd.entity.dictionary.Category_;
import ru.sportmaster.scd.entity.dictionary.SubCategory;
import ru.sportmaster.scd.entity.dictionary.SubCategory_;
import ru.sportmaster.scd.entity.dictionary.WareClass;
import ru.sportmaster.scd.entity.dictionary.WareClass_;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.entity.dictionary.WareGroup_;
import ru.sportmaster.scd.entity.replenishment.KgtSettingsAttrValue;
import ru.sportmaster.scd.entity.replenishment.KgtSettingsAttrValue_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class KgtSettingsAttrValueRepositoryImpl
    extends AbstractRepositoryImpl<KgtSettingsAttrValue, Long>
    implements KgtSettingsAttrValueRepository {
    public KgtSettingsAttrValueRepositoryImpl() {
        super(KgtSettingsAttrValue.class);
    }

    @Override
    public List<WareGroup> findAllWareGroup() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<WareGroup> query = builder.createQuery(WareGroup.class);
        Root<WareGroup> root = query.from(WareGroup.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<KgtSettingsAttrValue> subqueryRoot = subquery.from(KgtSettingsAttrValue.class);
        subquery.select(subqueryRoot.get(KgtSettingsAttrValue_.WARE_GROUP_ID)).distinct(true);

        Predicate predicate = builder.in(root.get(WareGroup_.ID)).value(subquery);
        query.orderBy(builder.asc(root.get(WareGroup_.ID)));

        return em.createQuery(query.select(root).where(predicate)).getResultList();
    }

    @Override
    public List<Category> findAllCategoryByWareGroup(Long wareGroupId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<KgtSettingsAttrValue> subqueryRoot = subquery.from(KgtSettingsAttrValue.class);
        subquery.select(subqueryRoot.get(KgtSettingsAttrValue_.CATEGORY_ID)).distinct(true);
        subquery.where(builder.equal(subqueryRoot.get(KgtSettingsAttrValue_.WARE_GROUP_ID), wareGroupId));

        Predicate predicate = builder.in(root.get(Category_.ID)).value(subquery);
        query.orderBy(builder.asc(root.get(Category_.ID)));

        return em.createQuery(query.select(root).where(predicate)).getResultList();
    }

    @Override
    public List<SubCategory> findAllSubcategoryByCategory(Long categoryId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SubCategory> query = builder.createQuery(SubCategory.class);
        Root<SubCategory> root = query.from(SubCategory.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<KgtSettingsAttrValue> subqueryRoot = subquery.from(KgtSettingsAttrValue.class);
        subquery.select(subqueryRoot.get(KgtSettingsAttrValue_.SUB_CATEGORY_ID)).distinct(true);
        subquery.where(builder.equal(subqueryRoot.get(KgtSettingsAttrValue_.CATEGORY_ID), categoryId));

        Predicate predicate = builder.in(root.get(SubCategory_.ID)).value(subquery);
        query.orderBy(builder.asc(root.get(SubCategory_.ID)));

        return em.createQuery(query.select(root).where(predicate)).getResultList();
    }

    @Override
    public List<WareClass> findAllWareClassBySubCategory(Long subCategoryId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<WareClass> query = builder.createQuery(WareClass.class);
        Root<WareClass> root = query.from(WareClass.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<KgtSettingsAttrValue> subqueryRoot = subquery.from(KgtSettingsAttrValue.class);
        subquery.select(subqueryRoot.get(KgtSettingsAttrValue_.WARE_CLASS_ID)).distinct(true);
        subquery.where(builder.equal(subqueryRoot.get(KgtSettingsAttrValue_.SUB_CATEGORY_ID), subCategoryId));

        Predicate predicate = builder.in(root.get(WareClass_.ID)).value(subquery);
        query.orderBy(builder.asc(root.get(WareClass_.ID)));

        return em.createQuery(query.select(root).where(predicate)).getResultList();
    }
}
