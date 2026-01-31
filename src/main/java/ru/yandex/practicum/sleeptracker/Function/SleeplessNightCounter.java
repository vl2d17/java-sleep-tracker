package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SleeplessNightCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0);
        }

        LocalDateTime startLogging = sessions.get(0).getStart();
        LocalDateTime endLogging = sessions.get(sessions.size() - 1).getEnd();

        LocalDate firstNightDate = getFirstNightDate(startLogging);
        LocalDate lastNightDate = getLastNightDate(endLogging);

        Set<LocalDate> nightsWithSleep = findNightWithSleep(sessions);

        long totalNight = ChronoUnit.DAYS.between(firstNightDate, lastNightDate) + 1;

        long sleeplessNight = totalNight - nightsWithSleep.size();

        return new SleepAnalysisResult(TITLE, (int) sleeplessNight);
    }

    private LocalDate getLastNightDate(LocalDateTime lastSessionEnd) {
        LocalDate sessionDate = lastSessionEnd.toLocalDate();
        return sessionDate.minusDays(1);
    }

    private LocalDate getFirstNightDate(LocalDateTime firstSessionStart) {
        LocalDate sessionDate = firstSessionStart.toLocalDate();
        LocalTime sessionTime = firstSessionStart.toLocalTime();

        return sessionTime.isBefore(LocalTime.NOON) ?
                sessionDate.minusDays(1) : sessionDate;
    }

    private Set<LocalDate> findNightWithSleep(List<SleepingSession> sessions) {
        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime sessionStart = session.getStart();
            LocalDateTime sessionEnd = session.getEnd();

            LocalDate currentDate = sessionStart.toLocalDate();

            LocalDate endDate = sessionEnd.toLocalDate();

            while (!currentDate.isAfter(endDate)) {
                LocalDateTime nightStart = currentDate.atStartOfDay();
                LocalDateTime nightEnd = currentDate.atTime(6, 0);

                if (intersects(sessionStart, sessionEnd, nightStart, nightEnd)) {
                    nightsWithSleep.add(currentDate.minusDays(1));
                }

                currentDate = currentDate.plusDays(1);
            }
        }
        return nightsWithSleep;
    }

    private boolean intersects(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2,
                               LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}
