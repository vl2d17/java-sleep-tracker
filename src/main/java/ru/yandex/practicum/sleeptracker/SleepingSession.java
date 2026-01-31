package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public record SleepingSession(LocalDateTime start,
                              LocalDateTime end,
                              SleepQuality quality) {

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}

