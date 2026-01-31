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

    public String getTitle() {
        return title;
    }

    public Integer getValue() {
        return (Integer) result;
    }

    public Chronotype getChronoType() {
        if (result instanceof Chronotype) {
            return (Chronotype) result;
        }
        return null;
    }
}


