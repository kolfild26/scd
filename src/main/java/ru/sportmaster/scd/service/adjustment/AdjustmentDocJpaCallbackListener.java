package ru.sportmaster.scd.service.adjustment;

import static java.util.Objects.nonNull;

import jakarta.persistence.PostLoad;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowRepository;
import ru.sportmaster.scd.utils.BeanUtil;

@Component
@NoArgsConstructor
public class AdjustmentDocJpaCallbackListener {
    @PostLoad
    public void fillEmptyRows(AdjustmentDoc document) {
        AdjustmentDocRowRepository repository = BeanUtil.getBean(AdjustmentDocRowRepository.class);
        AdjustmentDocType docType = document.getType();
        if (nonNull(docType.getType())) {
            document.setIsEmpty(!repository.hasRows(docType.getType().getRowClass(), document.getId()));
        }
    }
}
