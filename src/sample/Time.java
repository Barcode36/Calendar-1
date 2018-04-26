package sample;

public class Time {
    private int hour;
    private int minute;

    public Time() {
    }

    public Time(int h, int m) {
        hour = h;
        minute = m;
    }

    public String getTime() {
        if (hour >= 10) {
            if (minute >= 10)
                return hour + ":" + minute;
            else
                return hour + ":0" + minute;
        } else {
            if (minute >= 10)
                return "0" + hour + ":" + minute;
            else
                return "0" + hour + ":0" + minute;
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
