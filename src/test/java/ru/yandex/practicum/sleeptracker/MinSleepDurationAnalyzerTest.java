package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.MinSleepDurationAnalyzer;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinSleepDurationAnalyzerTest {

    @Test
    void apply_WithMultipleSessions_ReturnsMinDuration() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0), // 8 часов = 480 минут
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0), // 7 часов = 420 минут
                        LocalDateTime.of(2024, 1, 3, 6, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0), // 6 часов = 360 минут
                        LocalDateTime.of(2024, 1, 4, 4, 0),
                        SleepQuality.BAD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertNotNull(result);
        assertEquals(MinSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(360, result.getValue()); // Минимальная продолжительность 360 минут
    }

    @Test
    void apply_WithSingleSession_ReturnsSessionDuration() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0), // 8 часов = 480 минут
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertNotNull(result);
        assertEquals(MinSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(480, result.getValue());
    }

    @Test
    void apply_WithEmptyList_ReturnsZero() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();
        List<SleepingSession> emptySessions = List.of();


        SleepAnalysisResult result = analyzer.apply(emptySessions);


        assertNotNull(result);
        assertEquals(MinSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(0, result.getValue());
    }

    @Test
    void apply_WithNull_ReturnsZero() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();


        SleepAnalysisResult result = analyzer.apply(null);


        assertNotNull(result);
        assertEquals(MinSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(0, result.getValue());
    }

    @Test
    void apply_WithVeryShortSession_ReturnsCorrectDuration() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 14, 0), // 1 час = 60 минут
                        LocalDateTime.of(2024, 1, 1, 15, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 14, 30), // 30 минут
                        LocalDateTime.of(2024, 1, 2, 15, 0),
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(30, result.getValue()); // Минимальная 30 минут
    }

    @Test
    void apply_WithSessionCrossingMidnight_CalculatesCorrectDuration() {

        MinSleepDurationAnalyzer analyzer = new MinSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 0), // 7 часов = 420 минут
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(420, result.getValue());
    }
}