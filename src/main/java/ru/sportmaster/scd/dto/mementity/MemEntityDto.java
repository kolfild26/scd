package ru.sportmaster.scd.dto.mementity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemEntityDto<T> {
    private T value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean checked;
}
