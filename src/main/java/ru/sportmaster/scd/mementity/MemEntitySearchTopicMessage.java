package ru.sportmaster.scd.mementity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemEntitySearchTopicMessage extends MemEntityTopicMessage {
    private UiViewSearchRequestDto request;
}
