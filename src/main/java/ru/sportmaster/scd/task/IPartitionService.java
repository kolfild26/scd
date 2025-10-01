package ru.sportmaster.scd.task;

import java.util.List;

public interface IPartitionService {
    List<Long> getBusinessPartitionDivTmaIds(Long idPartition);

    List<Long> getAllPartitionDivTmaIds(Long idPartition);
}
