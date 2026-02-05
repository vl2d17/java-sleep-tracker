package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {

    private final String functionTitle;
    private final Object result;
    private final String title;

    public SleepAnalysisResult(String functionTitle, Object result) {
        this.functionTitle = functionTitle;
        this.result = result;
        this.title = functionTitle;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return functionTitle + ": " + result;
    }

    public Integer getValue() {
        if (result == null) {
            return 0;
        }

        if (result instanceof Integer) {
            return (Integer) result;
        }

        if (result instanceof Long) {

            long longValue = (Long) result;
            return (int) longValue;
        }

        if (result instanceof Number) {

            return ((Number) result).intValue();
        }

        return null;
    }

    public Chronotype getChronoType() {
        if (result instanceof Chronotype) {
            return (Chronotype) result;
        }
        return null;
    }

    public String getTitle() {
        return functionTitle;
    }
}


