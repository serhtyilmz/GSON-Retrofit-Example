package com.yourapp.name.models;

public class DeviceDetailModel {
    public int id;
    public String name;
    public double temperature;
    public double humidity;
    public int smoke;
    public int flame;
    public int alarmstatus;
    public int signalstatus;

    public DeviceDetailModel(int id, String name, double temperature, double humidity, int smoke, int flame, int alarmstatus, int signalstatus){
        this.id = id;
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.smoke = smoke;
        this.flame = flame;
        this.alarmstatus = alarmstatus;
        this.signalstatus = signalstatus;
    }
}
