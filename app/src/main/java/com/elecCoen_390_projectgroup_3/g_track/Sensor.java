package com.elecCoen_390_projectgroup_3.g_track;

public class Sensor {
    String bincode;
    String binname;
    String binlocation;
    double binvalue;

    public Sensor() {
    }

    public Sensor(String bincode, String name, String location, double value) {
        this.bincode = bincode;
        this.binname = name;
        this.binlocation = location;
        this.binvalue = value;
    }

    public String getBinCode(){
        return bincode;
    }

    public String getName() {
        return binname;
    }

    public String getBinLocation(){
        return binlocation;
    }

    public double getValue() {
        return binvalue;
    }

    public void setbincode(String bincode) {
        this.bincode = bincode;
    }

    public void setbinname(String name) {
        this.binname = name;
    }

    public void setbinlocation(String location) {
        this.binlocation = location;
    }

    public void setValue(double value) {
        this.binvalue = value;
    }
}

