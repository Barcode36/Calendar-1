package main;

public class Birthday {
    private int birthdayid;
    private int dateid;
    private String name;
    private boolean isDeleted;

    public void setBirthdayid(int birthdayid) {
        this.birthdayid = birthdayid;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDateid(int dateid) {
        this.dateid = dateid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthdayid() {
        return birthdayid;
    }

    public int getDateid() {
        return dateid;
    }

    public String getName() {
        return name;
    }
}
