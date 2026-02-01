package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.AvgSleepDurationAnalyzer;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvgSleepDurationAnalyzerTest {

    @Test
    void apply_WithMultipleSessions_ReturnsAverageDuration() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 6, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0),
                        LocalDateTime.of(2024, 1, 4, 4, 0),
                        SleepQuality.BAD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertNotNull(result);
        assertEquals(AvgSleepDurationAnalyzer.TITLE, result.getTitle());
        assertEquals(420, result.getValue());
    }

    @Test
    void apply_WithSingleSession_ReturnsSessionDuration() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 6, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(480, result.getValue());
    }

    @Test
    void apply_WithEmptyList_ReturnsZero() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();


        SleepAnalysisResult result = analyzer.apply(List.of());


        assertNotNull(result);
        assertEquals(0, result.getValue());
    }

    @Test
    void apply_WithNull_ReturnsZero() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();


        SleepAnalysisResult result = analyzer.apply(null);


        assertNotNull(result);
        assertEquals(0, result.getValue());
    }

    @Test
    void apply_WithDifferentDurations_AveragesCorrectly() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 5, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 8, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 22, 0),
                        LocalDateTime.of(2024, 1, 4, 4, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 4, 14, 0),
                        LocalDateTime.of(2024, 1, 4, 16, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(360, result.getValue());
    }

    @Test
    void apply_WithDecimalAverage_RoundsCorrectly() {

        AvgSleepDurationAnalyzer analyzer = new AvgSleepDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 0),
                        LocalDateTime.of(2024, 1, 2, 5, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 0),
                        LocalDateTime.of(2024, 1, 3, 7, 0),
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(465, result.getValue());
    }
}