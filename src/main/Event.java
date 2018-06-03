package main;

public class Event {
    private int eventid;
    private int dateid;
    private String title;
    private Long startTime;
    private Long endTime;
    private Long notifyTime;
    private String description;
    private String color;
    private int alarmid;
    private boolean isdeleted;
    private boolean isnotified;

    public Event() {
        isnotified = false;
    }

    public int getEventid() {
        return eventid;
    }

    public int getDateid() {
        return dateid;
    }

    public String getTitle() {
        return title;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getNotifyTime() {
        return notifyTime;
    }

    public String getDescription() {
        return description;
    }

    public int getAlarmID() {
        return alarmid;
    }

    public String getColor() {
        return color;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public boolean isIsnotified() {
        return isnotified;
    }

    public void setAlarmID(int alarmid) {
        this.alarmid = alarmid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public void setDateid(int _dateid) {
        dateid = _dateid;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public void setStartTime(Long _startTime) {
        startTime = _startTime;
    }

    public void setEndTime(Long _endTime) {
        endTime = _endTime;
    }

    public void setNotifyTime(Long _notifyTime) {
        notifyTime = _notifyTime;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public void setColor(String _color) {
        color = _color;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public void setIsnotified(boolean isnotified) {
        this.isnotified = isnotified;
    }
}
