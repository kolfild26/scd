package ru.sportmaster.scd.service.caches;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class HistoryCacheKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 8152302285928107356L;

    private Class<?> entityClass;
    private Object entityId;
    private Long version;

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(entityClass);
        out.writeObject(new ObjectMapper().writeValueAsString(entityId));
        out.writeObject(version);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        entityClass = (Class<?>) in.readObject();
        entityId =
            new ObjectMapper().readValue(
                (String) in.readObject(),
                new TypeReference<>() {
                }
            );
        version = (Long) in.readObject();
    }
}
