package ru.sportmaster.scd.mementity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "buildSuper")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemEntityTopicMessage {
    private String memberUid;
    private String nameMemEntity;
    private MemEntityTypeTopicMessage typeTopic;
    private String sessionUuid;
    private String formUuid;
}
