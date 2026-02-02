package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Function.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {

    public static final String SEPARATOR = ";";
    private static final DateTimeFormatter LOG_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyticFunctions = List.of(
            new SleepingSessionCounter(),
            new BadQualitySleepCounter(),
            new MinSleepDurationAnalyzer(),
            new MaxSleepDurationAnalyzer(),
            new AvgSleepDurationAnalyzer(),
            new SleeplessNightCounter(),
            new ChronotypeAnalyzer()
    );

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Укажите путь к файлу с логом сна.");
            System.out.println("Использование: java SleepTrackerApp <путь к файлу>");
            System.out.println("Пример: java SleepTrackerApp sleep_log.txt");
            return;
        }
        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            System.out.println("Загрузка файла: " + filePath);
            List<SleepingSession> sessions = app.readFile(app.getFile(filePath));
            System.out.println("Загружено сессий сна: " + sessions.size());

            System.out.println("\nРезультаты анализа:");
            List<SleepAnalysisResult> results = app.analyzeSessions(sessions);


            results.stream()
                    .forEach(result -> {
                        if (result.getChronoType() != null) {
                            System.out.println(result.getTitle() + ": " + result.getChronoType()
                                    .getDisplayName());
                        } else {
                            System.out.println(result);
                        }
                    });


        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл не найден - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return analyticFunctions.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }

    private File getFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        }

        if (!file.isFile()) {
            throw new FileNotFoundException("Указанный путь не является файлом: " + filename);
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