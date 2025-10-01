package ru.sportmaster.scd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sportmaster.scd.dto.ErrorDto;
import ru.sportmaster.scd.exceptions.AlgComponentNotFoundException;
import ru.sportmaster.scd.exceptions.AlgorithmExecutionProcessAlreadyRunningException;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.exceptions.QueueOperationError;
import ru.sportmaster.scd.exceptions.UnknownDefinitionException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorDto handleUnauthorizedException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDto.builder()
            .code(HttpStatus.UNAUTHORIZED.value())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({BadCredentialsException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDto handleBadRequestException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({UnknownDefinitionException.class, AlgComponentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDto handleNotFoundException(Exception e) {
        return ErrorDto.builder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({AlgorithmExecutionProcessAlreadyRunningException.class, CannotAcquireLockException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    ErrorDto handleUnavailableException(Exception e) {
        return ErrorDto.builder()
            .code(HttpStatus.SERVICE_UNAVAILABLE.value())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({QueueOperationError.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorDto handleInternalException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDto.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(e.getMessage())
            .build();
    }
}
