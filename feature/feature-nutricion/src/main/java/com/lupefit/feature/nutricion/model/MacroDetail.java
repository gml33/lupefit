package com.lupefit.feature.nutricion.model;

public class MacroDetail {
    private String name;
    private String description;
    private String value;
    private String unit;
    private String percentage;
    private int color;

    public MacroDetail() {}

    public MacroDetail(String name, String description, String value, String unit, String percentage, int color) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.percentage = percentage;
        this.color = color;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getPercentage() { return percentage; }
    public void setPercentage(String percentage) { this.percentage = percentage; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}