package com.lupefit.feature.nutricion.model;

public class MicroDetail {
    private String name;
    private String description;
    private String value;
    private String unit;
    private String percentage;

    public MicroDetail() {}

    public MicroDetail(String name, String description, String value, String unit, String percentage) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.unit = unit;
        this.percentage = percentage;
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
}