package ru.sportmaster.scd.repository.replenishment;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.Category;
import ru.sportmaster.scd.entity.dictionary.SubCategory;
import ru.sportmaster.scd.entity.dictionary.WareClass;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.entity.replenishment.KgtSettingsAttrValue;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface KgtSettingsAttrValueRepository extends AbstractRepository<KgtSettingsAttrValue, Long> {
    List<WareGroup> findAllWareGroup();

    List<Category> findAllCategoryByWareGroup(Long wareGroupId);

    List<SubCategory> findAllSubcategoryByCategory(Long categoryId);

    List<WareClass> findAllWareClassBySubCategory(Long subCategoryId);
}
