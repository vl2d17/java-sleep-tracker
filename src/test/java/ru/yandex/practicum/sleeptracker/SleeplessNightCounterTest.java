package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.SleeplessNightCounter;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleeplessNightCounterTest {

    @Test
    void testApply_EmptyList_Zero() {
        SleeplessNightCounter counter = new SleeplessNightCounter();

        SleepAnalysisResult result = counter.apply(List.of());

        assertNotNull(result);
        assertEquals(SleeplessNightCounter.TITLE, result.getTitle());
        assertEquals(0, result.getValue());
    }

    @Test
    void testApply_SingleNightSleep_Zero() {
        SleeplessNightCounter counter = new SleeplessNightCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 0),
                        LocalDateTime.of(2024, 1, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );
        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals(0, result.getValue(), "Должно быть 0 бессонных ночей");
    }

    @Test
    void testApply_MultipleSessionsWithGaps_ReturnsCorrectCount() {
        // Подготовка: несколько сессий с пропусками ночей
        SleeplessNightCounter counter = new SleeplessNightCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 0),
                        LocalDateTime.of(2024, 1, 2, 7, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 14, 0), // изменено на дневной
                        LocalDateTime.of(2024, 1, 2, 15, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0),
                        LocalDateTime.of(2024, 1, 4, 2, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);
        assertEquals(1, result.getValue(), "Должна быть 1 бессонная ночь");
    }

    @Test
    void testApply_SleepCrossingMidnight_CountsAsNightSleep() {
        SleeplessNightCounter counter = new SleeplessNightCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 30),
                        LocalDateTime.of(2024, 1, 2, 0, 30), // пересекает полночь
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals(0, result.getValue());
    }
}


