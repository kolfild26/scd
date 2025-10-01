package ru.sportmaster.scd.task;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.NonNull;

@EqualsAndHashCode
@ToString
public class PartitionItemBlock implements ItemBlock {
    private final List<Long> partitionIds;

    private PartitionItemBlock(@NonNull List<Long> partitionIds) {
        this.partitionIds = partitionIds;
    }

    public static PartitionItemBlock create(List<Long> partitionIds) {
        return new PartitionItemBlock(partitionIds);
    }

    @Override
    public boolean nonBlocking(List<Long> partitionIds) {
        return
            this.partitionIds.stream()
                .distinct()
                .noneMatch(partitionIds::contains);
    }
}
