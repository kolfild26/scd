package ru.sportmaster.scd.algorithms;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_STEP_PARAM_NAME;

public enum AlgComponentStatus {
    NOT_FOUND,          /* Компонент не найден */
    WAITING,            /* Ожидает запуска */
    EXECUTION,          /* Выполняется */
    DONE,               /* Выполнен */
    ERROR,              /* Выполнен с ошибками */
    CANCELLED;          /* Отменен */

    public static AlgComponentStatus getAlgComponentStatus(IAlgComponent component) {
        if (isNull(component)) {
            return NOT_FOUND;
        }
        if (nonNull(component.getCancelTime())) {
            return CANCELLED;
        }
        if (isNull(component.getStartTime())) {
            return WAITING;
        }
        if (isNull(component.getEndTime())) {
            return EXECUTION;
        }
        if (nonNull(component.getResult())
            && nonNull(component.getResult().get(ERROR_STEP_PARAM_NAME))) {
            return ERROR;
        }
        return DONE;
    }
}
