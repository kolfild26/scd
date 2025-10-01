package ru.sportmaster.scd.algorithms.executing;

import java.util.List;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;

public record AlgorithmExec(Algorithm algorithm, List<AlgorithmStageExec> stageExecs) {
}
