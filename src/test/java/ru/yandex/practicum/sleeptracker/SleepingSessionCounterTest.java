package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.SleepingSessionCounter;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SleepingSessionCounterTest {

    @Test
    void testApply_WithMultipleSessions() {
        SleepingSessionCounter counter = new SleepingSessionCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2025, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 1, 2, 23, 0),
                        LocalDateTime.of(2023, 1, 3, 8, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(SleepingSessionCounter.TITLE, result.getFunctionTitle(),
                "Заголовок должен соответствовать TITLE");
        assertEquals(2, result.getValue(),
                "Количество сессий должно быть равно 2");
    }

    @Test
    void testApply_WithEmptyList_ReturnZero() {
        SleepingSessionCounter counter = new SleepingSessionCounter();
        List<SleepingSession> emptySessions = Collections.emptyList();
        SleepAnalysisResult result = counter.apply(emptySessions);

        assertNotNull(result, "Результат не должен быть null даже для пустого списка");
        assertEquals(SleepingSessionCounter.TITLE, result.getTitle(),
                "Заголовой должен соответствовать константе TITLE");
        assertEquals(0, result.getValue(),
                "Для пустого списка должно возвращать 0");
    }
}
