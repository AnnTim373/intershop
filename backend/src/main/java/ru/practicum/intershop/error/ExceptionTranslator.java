package ru.practicum.intershop.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.intershop.dto.error.ErrorOutput;
import ru.practicum.intershop.error.custom_exception.IntershopException;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(IntershopException.class)
    public ResponseEntity<ErrorOutput> handleIntershopException(IntershopException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorOutput(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutput> handleException(Exception ex) {
        log.error("Произошла неизвестная ошибка: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorOutput("Произошла неизвестная ошибка при обработке запроса"));
    }

}
