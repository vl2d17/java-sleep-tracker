package ru.yandex.practicum.sleeptracker;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {

    @TempDir
    Path tempDir;

    @Test
    void testParseLine_ValidLine() {
        SleepTrackerApp app = new SleepTrackerApp();
        String line = "01.01.24 22:00;02.01.24 06:00;GOOD";

        var result = app.parseLine(line);

        assertTrue(result.isPresent());
        SleepingSession session = result.get();
        assertEquals(LocalDateTime.of(2024, 1, 1, 22, 0),
                session.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 2, 6, 0),
                session.getEnd());
        assertEquals(SleepQuality.GOOD, session.getQuality());
    }

    @Test
    void testParseLine_InvalidLine() {
        SleepTrackerApp app = new SleepTrackerApp();
        String line = "01.01.24 22:00;BAD";

        var result = app.parseLine(line);
        assertFalse(result.isPresent());
    }

    @Test
    void testParseLine_InvalidFormat_ReturnsEmpty() {

        SleepTrackerApp app = new SleepTrackerApp();
        String line = "01.01.24 22:00;02.01.24";


        var result = app.parseLine(line);


        assertFalse(result.isPresent());
    }

    @Test
    void testParseLine_NullOrEmpty_ReturnsEmpty() {

        SleepTrackerApp app = new SleepTrackerApp();


        var result1 = app.parseLine(null);
        assertFalse(result1.isPresent());


        var result2 = app.parseLine("");
        assertFalse(result2.isPresent());


        var result3 = app.parseLine("   ");
        assertFalse(result3.isPresent());
    }

    @Test
    void testParseLine_SleepCrossesMidnight_AdjustsEndDate() {

        SleepTrackerApp app = new SleepTrackerApp();
        String line = "01.01.24 22:00;01.01.24 06:00;GOOD";


        var result = app.parseLine(line);


        assertTrue(result.isPresent());
        SleepingSession session = result.get();
        assertEquals(LocalDateTime.of(2024, 1, 1, 22, 0), session.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 2, 6, 0), session.getEnd());
    }

    @Test
    void testAnalyzeSessions_WithSessions_ReturnsResults() {

        SleepTrackerApp app = new SleepTrackerApp();
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


        List<SleepAnalysisResult> results = app.analyzeSessions(sessions);


        assertNotNull(results);
        assertFalse(results.isEmpty());

        assertEquals(6, results.size(), "Должно быть 6 результатов от всех аналитических функций");

    }

    @Test
    void testAnalyzeSessions_EmptyList_ReturnsZero() {

        SleepTrackerApp app = new SleepTrackerApp();
        List<SleepingSession> emptySessions = List.of();


        List<SleepAnalysisResult> results = app.analyzeSessions(emptySessions);


        assertNotNull(results);


        assertEquals(6, results.size(), "Должно быть 6 результатов от всех аналитических функций");


        SleepAnalysisResult sessionCountResult = results.stream()
                .filter(r -> r.getTitle().equals("Количество сессий сна"))
                .findFirst()
                .orElse(null);

        assertNotNull(sessionCountResult, "Должен быть результат подсчета сессий");
        assertEquals(0, sessionCountResult.getValue(), "Для пустого списка должно быть 0 сессий");


        SleepAnalysisResult sleeplessResult = results.stream()
                .filter(r -> r.getTitle().equals("Количество бессонных ночей"))
                .findFirst()
                .orElse(null);

        assertNotNull(sleeplessResult, "Должен быть результат подсчета бессонных ночей");
        assertEquals(0, sleeplessResult.getValue(), "Для пустого списка должно быть 0 бессонных ночей");


        SleepAnalysisResult chronotypeResult = results.stream()
                .filter(r -> r.getTitle().equals("Хронотип пользователя"))
                .findFirst()
                .orElse(null);

        assertNotNull(chronotypeResult, "Должен быть результат определения хронотипа");
        assertEquals("Голубь", chronotypeResult.getResult().toString(),
                "Для пустого списка хронотип должен быть Голубь");
    }

    @Test
    void testReadFile_ValidFile_ReturnsSessions() throws IOException {

        File tempFile = tempDir.resolve("test_sleep_log.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("01.01.24 22:00;02.01.24 06:00;GOOD\n");
            writer.write("02.01.24 23:00;03.01.24 07:00;BAD\n");
        }


        SleepTrackerApp app = new SleepTrackerApp();


        List<SleepingSession> sessions = app.readFile(tempFile);


        assertNotNull(sessions);
        assertEquals(2, sessions.size());
    }

    @Test
    void testReadFile_FileWithInvalidLines_ReturnsOnlyValidSessions() throws IOException {

        File tempFile = tempDir.resolve("test_sleep_log_mixed.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("01.01.24 22:00;02.01.24 06:00;GOOD\n");
            writer.write("invalid line\n");
            writer.write("02.01.24 23:00;03.01.24 07:00;NORMAL\n");
            writer.write("01.01.24 22:00;02.01.24 06:00;INVALID\n");
        }


        SleepTrackerApp app = new SleepTrackerApp();


        List<SleepingSession> sessions = app.readFile(tempFile);


        assertNotNull(sessions);
        assertEquals(2, sessions.size());


        assertEquals(2, sessions.size(), "Должны быть прочитаны только 2 валидные сессии");


        assertEquals(LocalDateTime.of(2024, 1, 1, 22, 0), sessions.get(0).getStart());
        assertEquals(LocalDateTime.of(2024, 1, 2, 23, 0), sessions.get(1).getStart());
    }
}



