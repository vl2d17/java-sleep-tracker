package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Хронотип пользователя";
    private static final LocalTime OWL_BEDTIME = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKEUP = LocalTime.of(9, 0);
    private static final LocalTime LARK_BEDTIME = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKEUP = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, Chronotype.PIGEON);
        }

        Map<Chronotype, Integer> nightCounts = new HashMap<>();
        nightCounts.put(Chronotype.OWL, 0);
        nightCounts.put(Chronotype.LARK, 0);
        nightCounts.put(Chronotype.PIGEON, 0);

        for (SleepingSession session : sessions) {
            Chronotype nightType = analyzeNight(session);
            if (nightType != null) {
                nightCounts.put(nightType, nightCounts.get(nightType) + 1);
            }
        }
        Chronotype dominantChronotype = determineDominantChronotype(nightCounts);

        return new SleepAnalysisResult(TITLE, dominantChronotype);
    }

    private Chronotype analyzeNight(SleepingSession session) {
        LocalDateTime sessionStart = session.getStart();
        LocalDateTime sessionEnd = session.getEnd();

        if (!isNightSession(sessionStart, sessionEnd)) {
            return null;
        }

        LocalTime bedtime = sessionStart.toLocalTime();
        LocalTime wakeupTime = sessionEnd.toLocalTime();

        if (bedtime.isAfter(OWL_BEDTIME) && wakeupTime.isAfter(OWL_WAKEUP)) {
            return Chronotype.OWL;
        } else if (bedtime.isBefore(LARK_BEDTIME) && wakeupTime.isBefore(LARK_WAKEUP)) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }

    private boolean isNightSession(LocalDateTime sessionStart, LocalDateTime sessionEnd) {

        LocalDate currentDate = sessionStart.toLocalDate();
        LocalDate endDate = sessionEnd.toLocalDate();

        while (!currentDate.isAfter(endDate)) {

            LocalDateTime morningStart = currentDate.atStartOfDay(); // 00:00
            LocalDateTime morningEnd = currentDate.atTime(6, 0); // 06:00

            if (intersects(sessionStart, sessionEnd, morningStart, morningEnd)) {
                return true;
            }

            currentDate = currentDate.plusDays(1);
        }

        return false;
    }


    private Chronotype determineDominantChronotype(Map<Chronotype, Integer> nightCounts) {
        int owlCount = nightCounts.get(Chronotype.OWL);
        int larkCount = nightCounts.get(Chronotype.LARK);
        int doveCount = nightCounts.get(Chronotype.PIGEON);

        if (owlCount > larkCount && owlCount > doveCount) {
            return Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > doveCount) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }

    private boolean intersects(LocalDateTime start1, LocalDateTime end1,
                               LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}




