package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaxSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Максимальная продолжительность сессии (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0);
        }

        int maxDuration = sessions.stream()
                .mapToInt(session -> (int) Duration.between(session.getStart(),
                        session.getEnd()).toMinutes())
                .max()
                .orElse(0);

        return new SleepAnalysisResult(TITLE, maxDuration);
    }
}
