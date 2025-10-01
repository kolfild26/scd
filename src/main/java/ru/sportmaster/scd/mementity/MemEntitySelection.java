package ru.sportmaster.scd.mementity;

import static java.util.Objects.isNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@Builder(toBuilder = true)
public class MemEntitySelection<K> {
    @Builder.Default
    private TypeSelection typeSelection = TypeSelection.UNCHECKED;
    @Builder.Default
    private Set<K> choices = new HashSet<>();
    private Function<Object, K> keyMapper;

    public static <K> MemEntitySelection<K> create(Function<Object, K> keyMapper) {
        return
            MemEntitySelection.<K>builder()
                .keyMapper(keyMapper)
                .build();
    }

    public boolean isChecked(K key) {
        if (typeSelection.equals(TypeSelection.CHECKED)) {
            return !choices.contains(key);
        }
        return choices.contains(key);
    }

    public void selectAll() {
        typeSelection = TypeSelection.CHECKED;
        choices.clear();
    }

    public void unselectAll() {
        typeSelection = TypeSelection.UNCHECKED;
        choices.clear();
    }

    public void select(List<Object> ids, boolean multiselect) {
        if (isNull(ids) || ids.isEmpty()) {
            return;
        }
        if (!multiselect) {
            unselectAll();
        }
        typeSelection.select(ids, this);
    }

    public void unSelect(List<Object> ids) {
        if (isNull(ids) || ids.isEmpty()) {
            return;
        }
        typeSelection.unSelect(ids, this);
    }

    public boolean isTypeChecked() {
        return TypeSelection.CHECKED == this.typeSelection;
    }

    private void addToChoices(@NonNull List<Object> ids) {
        choices.addAll(
            ids.stream()
                .map(keyMapper)
                .collect(Collectors.toSet())
        );
    }

    private void removeFromChoices(@NonNull List<Object> ids) {
        choices.removeAll(
            ids.stream()
                .map(keyMapper)
                .collect(Collectors.toSet())
        );
    }

    public enum TypeSelection {
        CHECKED {
            @Override
            void select(@NonNull List<Object> ids,
                        @NonNull MemEntitySelection<?> selection) {
                selection.removeFromChoices(ids);
            }

            @Override
            void unSelect(@NonNull List<Object> ids,
                          @NonNull MemEntitySelection<?> selection) {
                selection.addToChoices(ids);
            }
        },
        UNCHECKED {
            @Override
            void select(@NonNull List<Object> ids,
                        @NonNull MemEntitySelection<?> selection) {
                selection.addToChoices(ids);
            }

            @Override
            void unSelect(@NonNull List<Object> ids,
                          @NonNull MemEntitySelection<?> selection) {
                selection.removeFromChoices(ids);
            }
        };

        abstract void select(@NonNull List<Object> ids,
                             @NonNull MemEntitySelection<?> selection);

        abstract void unSelect(@NonNull List<Object> ids,
                               @NonNull MemEntitySelection<?> selection);
    }
}
