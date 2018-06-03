package main;

public class Holiday {
    private int holidayid;
    private int dateid;
    private String name;
    private boolean isDeleted;

    public void setHolidayid(int holidayid) {
        this.holidayid = holidayid;
    }

    public void setDateid(int dateid) {
        this.dateid = dateid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getHolidayid() {
        return holidayid;
    }

    public int getDateid() {
        return dateid;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
