package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AvgSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Средняя продолжительность сессии (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0);
        }

        double avgDuration = sessions.stream()
                .mapToInt(session -> (int) Duration.between(session.getStart(),
                        session.getEnd()).toMinutes())
                .average()
                .orElse(0.0);

        int roundedAVG = (int) Math.round(avgDuration);

        return new SleepAnalysisResult(TITLE, Math.round(avgDuration));
    }
}
