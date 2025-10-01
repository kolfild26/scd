package ru.sportmaster.scd.ui.view.type;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class JpaViewSearchKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -5233543403093145466L;

    private String view;
    private Long userId;

    public static JpaViewSearchKey buildKey(String view, Long userId) {
        return new JpaViewSearchKey(view, userId);
    }
}
