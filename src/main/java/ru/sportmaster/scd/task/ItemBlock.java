package ru.sportmaster.scd.task;

import java.util.List;

public interface ItemBlock {
    boolean nonBlocking(List<Long> partitionIds);
}
