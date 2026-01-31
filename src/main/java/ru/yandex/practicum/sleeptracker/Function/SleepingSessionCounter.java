package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;


import java.util.List;
import java.util.function.Function;

public class SleepingSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Количество сессий сна";


    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Integer quantity = sessions.size();
        return new SleepAnalysisResult(TITLE, quantity);
    }

}
