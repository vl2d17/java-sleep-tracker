package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Минимальная продолжительность сессии (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0);
        }

        int minDuration = sessions.stream()
                .mapToInt(session -> (int) Duration.between(session.getStart(),
                        session.getEnd()).toMinutes())
                .min()
                .orElse(0);

        return new SleepAnalysisResult(TITLE, minDuration);
    }
}
