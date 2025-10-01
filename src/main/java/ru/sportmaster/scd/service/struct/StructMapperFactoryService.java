package ru.sportmaster.scd.service.struct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.struct.IStructMapper;
import ru.sportmaster.scd.struct.IStructMapperFactory;

@Service
public class StructMapperFactoryService implements IStructMapperFactory {
    @Getter
    private final String schemaName;
    private final Map<Class<?>, IStructMapper<?>> mappers;

    public StructMapperFactoryService(List<IStructMapper<?>> mappers) {
        this.schemaName = "";
        this.mappers =
            mappers.stream()
                .map(this::registerStructMapper)
                .collect(
                    Collectors.toMap(
                        IStructMapper::getMappedClass,
                        mapper -> mapper
                    )
                );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IStructMapper<T> getStructMapper(Class<T> mappedClass) {
        return (IStructMapper<T>) Optional.ofNullable(mappers.get(mappedClass)).orElseThrow();
    }

    private IStructMapper<?> registerStructMapper(@NonNull IStructMapper<?> structMapper) {
        structMapper.setStructMapperFactory(this);
        return structMapper;
    }
}
