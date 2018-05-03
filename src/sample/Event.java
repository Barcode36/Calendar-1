package sample;

public class Event {
    private int eventid;
    private int dateid;
    private String title;
    private Long startTime;
    private Long endTime;
    private Long notifyTime;
    private String description;
    private String color;

    public Event() {
    }

    public Event(int _dateid, String _title, Long _startTime, Long _endTime, Long _notifyTime, String _description, String _color) {
        dateid = _dateid;
        title = _title;
        startTime = _startTime;
        endTime = _endTime;
        notifyTime = _notifyTime;
        description = _description;
        color = _color;
    }

    public int getEventid() {
        return eventid;
    }

    public int getdateid() {
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

    public String getColor() {
        return color;
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

}
