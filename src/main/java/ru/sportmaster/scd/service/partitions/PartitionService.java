package ru.sportmaster.scd.service.partitions;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.repository.dictionary.PartitionRepository;
import ru.sportmaster.scd.task.IPartitionService;

@Service
@RequiredArgsConstructor
public class PartitionService implements IPartitionService {
    private final PartitionRepository partitionRepository;

    @Override
    public List<Long> getBusinessPartitionDivTmaIds(Long idPartition) {
        return
            partitionRepository.findByBusiness(idPartition).stream()
                .map(Partition::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllPartitionDivTmaIds(Long idPartition) {
        return
            partitionRepository.findAll().stream()
                .map(Partition::getId)
                .collect(Collectors.toList());
    }
}
