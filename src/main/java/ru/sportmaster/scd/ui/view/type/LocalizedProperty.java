package ru.sportmaster.scd.ui.view.type;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedProperty {
    private List<String> messageKeys;

    public LocalizedProperty(String... messageKeys) {
        this.messageKeys = Arrays.asList(messageKeys);
    }

    public static LocalizedProperty of(String... messageKeys) {
        return new LocalizedProperty(messageKeys);
    }

    public String getFirstKey() {
        if (messageKeys == null || messageKeys.isEmpty()) {
            return null;
        }
        return messageKeys.get(0);
    }

    public String getLastKey() {
        if (messageKeys == null || messageKeys.isEmpty()) {
            return null;
        }
        return messageKeys.get(messageKeys.size() - 1);
    }
}
