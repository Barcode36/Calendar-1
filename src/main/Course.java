package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Course {
    private String Title;
    private String TimeOfNotify;
    private String Teacher;
    private String Faculty;
    private String Subject;
    private String Class;
    private String Room;
    private String TimeOfCourse;
    private int dateid;
    private Long startTime;
    private Long endTime;
    private Long notifyTime;
    private String description;
    private String color;

    public Course(String title, String timeOfNotify, String teacher, String faculty, String subject, String aClass, String room, String timeOfCourse) {
        Title = title;
        TimeOfNotify = timeOfNotify;
        Teacher = teacher;
        Faculty = faculty;
        Subject = subject;
        Class = aClass;
        Room = room;
        TimeOfCourse = timeOfCourse;

        String[] startHour = {"07", "08", "09", "10", "10", "13", "13", "14", "15", "16"};
        String[] startMinute = {"30", "15", "00", "00", "45", "00", "45", "30", "30", "15"};

        String[] endHour = {"08", "09", "09", "10", "11", "13", "14", "15", "16", "17"};
        String[] endMinute = {"15", "00", "45", "45", "30", "45", "30", "15", "15", "00"};

        String temp = TimeOfCourse.substring(TimeOfCourse.length() - 10, TimeOfCourse.length());

        temp = temp + " - " + startHour[Integer.parseInt("" + TimeOfCourse.charAt(29)) - 1] + ":" + startMinute[Integer.parseInt("" + TimeOfCourse.charAt(29)) - 1] + ":00";

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        try {
                Date date = dateFormat.parse(temp);
                startTime = date.getTime();
        }
        catch (ParseException e) {

        }

        temp = TimeOfCourse.substring(TimeOfCourse.length() - 10, TimeOfCourse.length());
        temp = temp + " - " + endHour[Integer.parseInt("" + TimeOfCourse.charAt(49)) - 1] + ":" + endMinute[Integer.parseInt("" + TimeOfCourse.charAt(49)) - 1] + ":00";

        try {
            Date date = dateFormat.parse(temp);
            endTime = date.getTime();
        }
        catch (ParseException e) {

        }

        temp = TimeOfNotify.substring(4, TimeOfNotify.length()) + ":00";

        try {
            Date date = dateFormat.parse(temp);
            notifyTime = date.getTime();
        }
        catch (ParseException e) {

        }

        description = Title + "\n" + Teacher + "\n" + Faculty + "\n" + Subject + "\n" + Class + "\n" + Room + "\n" + TimeOfCourse + "\n" + TimeOfNotify;
    }

    public int getDateid() {
        return dateid;
    }

    public void setDateid(int dateid) {
        this.dateid = dateid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
