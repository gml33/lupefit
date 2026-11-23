package com.lupefit.core.domain.model;
public final class WorkoutSummary {
    private final String name, type; private final int durationMinutes, calories;
    public WorkoutSummary(String name, String type, int durationMinutes, int calories) { this.name=name; this.type=type; this.durationMinutes=durationMinutes; this.calories=calories; }
    public String getName(){return name;} public String getType(){return type;} public int getDurationMinutes(){return durationMinutes;} public int getCalories(){return calories;}
}
