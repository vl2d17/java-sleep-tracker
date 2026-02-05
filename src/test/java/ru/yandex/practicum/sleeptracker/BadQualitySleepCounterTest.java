package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.BadQualitySleepCounter;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BadQualitySleepCounterTest {

    @Test
    void apply_CountsOnlyBADQualitySessions() {

        BadQualitySleepCounter counter = new BadQualitySleepCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 7, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0),
                        LocalDateTime.of(2024, 1, 4, 4, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 4, 23, 0),
                        LocalDateTime.of(2024, 1, 5, 5, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 5, 22, 0),
                        LocalDateTime.of(2024, 1, 6, 6, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = counter.apply(sessions);


        assertNotNull(result);
        assertEquals(BadQualitySleepCounter.TITLE, result.getTitle());
        assertEquals(2, result.getValue());
    }

    @Test
    void apply_NoBADQuality_ReturnsZero() {

        BadQualitySleepCounter counter = new BadQualitySleepCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 7, 0),
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = counter.apply(sessions);


        assertEquals(0, result.getValue());
    }

    @Test
    void apply_AllBADQuality_ReturnsAll() {

        BadQualitySleepCounter counter = new BadQualitySleepCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 7, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0),
                        LocalDateTime.of(2024, 1, 4, 4, 0),
                        SleepQuality.BAD
                )
        );


        SleepAnalysisResult result = counter.apply(sessions);


        assertEquals(3, result.getValue()); // все 3 сессии
    }

    @Test
    void apply_EmptyList_ReturnsZero() {

        BadQualitySleepCounter counter = new BadQualitySleepCounter();


        SleepAnalysisResult result = counter.apply(List.of());


        assertNotNull(result);
        assertEquals(0, result.getValue());
    }
}

