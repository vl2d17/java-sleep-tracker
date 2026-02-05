package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Function.ChronotypeAnalyzer;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChronotypeAnalyzerTest {

    @Test
    void testApply_EmptyList_ReturnsDove() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();


        SleepAnalysisResult result = analyzer.apply(List.of());


        assertNotNull(result);
        assertEquals(ChronotypeAnalyzer.TITLE, result.getTitle());
        assertEquals(Chronotype.PIGEON, result.getChronoType());
    }


    @Test
    void testApply_LarkNights_ReturnsLark() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 21, 30), // до 22:00
                        LocalDateTime.of(2024, 1, 2, 6, 30),  // до 7:00
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 20, 0),  // до 22:00
                        LocalDateTime.of(2024, 1, 3, 5, 0),   // до 7:00
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(Chronotype.LARK, result.getChronoType());
    }

    @Test
    void testApply_DoveNights_ReturnsDove() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 22, 30), // не сова и не жаворонок
                        LocalDateTime.of(2024, 1, 2, 8, 0),   // не сова и не жаворонок
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 23, 30), // сова по засыпанию
                        LocalDateTime.of(2024, 1, 3, 7, 30),  // не сова по пробуждению
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(Chronotype.PIGEON, result.getChronoType());
    }


    @Test
    void testApply_DaytimeSessions_Ignored() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 14, 0),
                        LocalDateTime.of(2024, 1, 1, 15, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 2, 10, 0),
                        LocalDateTime.of(2024, 1, 2, 11, 0),
                        SleepQuality.NORMAL
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(Chronotype.PIGEON, result.getChronoType());
    }

    @Test
    void testApply_TieBetweenOwlAndLark_ReturnsDove() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(

                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 30),
                        LocalDateTime.of(2024, 1, 2, 9, 30),
                        SleepQuality.GOOD
                ),

                new SleepingSession(
                        LocalDateTime.of(2024, 1, 3, 21, 0),
                        LocalDateTime.of(2024, 1, 4, 6, 0),
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertEquals(Chronotype.PIGEON, result.getChronoType());
    }

    @Test
    void testApply_NightSessionCrossingMidnight_ProperlyAnalyzed() {

        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, 1, 1, 23, 30), // до полуночи
                        LocalDateTime.of(2024, 1, 2, 1, 30),  // после полуночи
                        SleepQuality.GOOD
                )
        );


        SleepAnalysisResult result = analyzer.apply(sessions);


        assertNotNull(result.getChronoType());
    }
}


