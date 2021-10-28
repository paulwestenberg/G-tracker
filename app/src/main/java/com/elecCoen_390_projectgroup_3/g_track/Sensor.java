package com.elecCoen_390_projectgroup_3.g_track;

public class Sensor {
    String name;
    double value;

    public Sensor() {
    }

    public Sensor(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
