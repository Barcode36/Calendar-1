package main;

public class Alarm {
    private int alarmid;
    private String name;
    private String path;

    public void setAlarmid(int alarmid) {
        this.alarmid = alarmid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getAlarmid() {
        return alarmid;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
