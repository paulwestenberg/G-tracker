package com.elecCoen_390_projectgroup_3.g_track.model;

public class Bin {
    String bincode;
    String binname;
    String binlocation;
    String binvalue;

    public Bin() {
    }

    public Bin(String bincode, String name, String location, String value) {
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

    public String getValue() {
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

    public void setValue(String value) {
        this.binvalue = value;
    }
}
