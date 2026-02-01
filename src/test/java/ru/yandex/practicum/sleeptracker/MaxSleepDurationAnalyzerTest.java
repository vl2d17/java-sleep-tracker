package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.MaxSleepDurationAnalyzer;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxSleepDurationAnalyzerTest {

    @Test
    void apply_WithMultipleSessions_ReturnsMaxDuration() {

        MaxSleepDurationAnalyzer analyzer = new MaxSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0), // 8 часов = 480 минут
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0), // 9 часов = 540 минут
                        LocalDateTime.of(2024, 1, 3, 8, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0), // 7 часов = 420 минут
                        LocalDateTime.of(2024, 1, 4, 5, 0),
                        SleepQuality.BAD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertNotNull(result);
        assertEquals(MaxSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(540, result.getValue()); // Максимальная продолжительность 540 минут
    }

    @Test
    void apply_WithLongNightSleep_ReturnsCorrectDuration() {

        MaxSleepDurationAnalyzer analyzer = new MaxSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0), // 10 часов = 600 минут
                        LocalDateTime.of(2024, 1, 2, 8, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(600, result.getValue());
    }

    @Test
    void apply_WithVeryShortSessions_ReturnsMax() {

        MaxSleepDurationAnalyzer analyzer = new MaxSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 14, 0), // 1 час
                        LocalDateTime.of(2024, 1, 1, 15, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 15, 0), // 2 часа
                        LocalDateTime.of(2024, 1, 2, 17, 0),
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(120, result.getValue()); // 2 часа = 120 минут
    }

    @Test
    void apply_WithEmptyList_ReturnsZero() {

        MaxSleepDurationAnalyzer analyzer = new MaxSleepDurationAnalyzer();


        SleepAnalysisResult result = analyzer.apply(List.of());


        assertNotNull(result);
        assertEquals(0, result.getValue());
    }
}