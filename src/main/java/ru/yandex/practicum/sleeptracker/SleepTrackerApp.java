package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Function.ChronotypeAnalyzer;
import ru.yandex.practicum.sleeptracker.Function.SleepingSessionCounter;
import ru.yandex.practicum.sleeptracker.Function.SleeplessNightCounter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {

    private static final String SESSIONS_FILE_NAME =
            "D:/JAVA/TZPractYa/java-TZ/src/TZ8/src/main/resources/sleep_log.txt";
    public static final String SEPARATOR = ";";
    private static final DateTimeFormatter LOG_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTIC_FUNCTIONS = List.of(
            new SleepingSessionCounter(),
            new SleeplessNightCounter(),
            new ChronotypeAnalyzer()
    );

    public static void main(String[] args) {
        SleepTrackerApp app = new SleepTrackerApp();
        try {
            List<SleepingSession> sessions = app.readFile(app.getFile(SESSIONS_FILE_NAME));
            List<SleepAnalysisResult> results = app.analyzeSessions(sessions);

            for (SleepAnalysisResult result : results) {
                if (result.getChronoType() != null) {
                    System.out.println(result.getTitle() + ": " + result.getChronoType().getDisplayName());
                } else {
                    System.out.println(result);
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return ANALYTIC_FUNCTIONS.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }

    private File getFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        }

        if (!file.isFile()) {
            throw new FileNotFoundException("Указанный пусть не является файлом: " + filename);
        }

        if (!file.canRead()) {
            throw new FileNotFoundException("Нет прав на чтение файла: " + filename);
        }
        return file;
    }

    List<SleepingSession> readFile(File file) {
        List<SleepingSession> sessions = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {
            sessions = reader.lines()
                    .map(this::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (sessions.isEmpty()) {
                System.out.println("Прочитан пустой файл " + file.getName());
            }

        } catch (IOException exception) {
            System.out.println("Произошла ошибка при чтении из файла " + file.getName() +
                    ": " + exception.getMessage());
        }

        return sessions;
    }

    Optional<SleepingSession> parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            String[] parts = line.split(SEPARATOR);
            if (parts.length < 3) {
                return Optional.empty();
            }

            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), LOG_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1].trim(), LOG_TIME_FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());

            if (start.isAfter(end)) {
                end = end.plusDays(1);
            }

            return Optional.of(new SleepingSession(start, end, quality));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}