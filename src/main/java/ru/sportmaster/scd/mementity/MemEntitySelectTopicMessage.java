package ru.sportmaster.scd.mementity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemEntitySelectTopicMessage extends MemEntityTopicMessage {
    private SelectionRequestDto request;
}
