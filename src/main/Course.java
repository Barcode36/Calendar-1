package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Course {
    private int courseid;
    private String title;
    private String teacher;
    private String faculty;
    private String subject;
    private String classid;
    private String room;
    private int dateid;
    private int startTime;
    private int endTime;
    private Long notifyTime;
    private Long courseTime;
    private String url;

//    public Course(String title, String timeOfNotify, String teacher, String faculty, String subject, String aClass, String room, String timeOfCourse) {
//        Title = title;
//        TimeOfNotify = timeOfNotify;
//        Teacher = teacher;
//        Faculty = faculty;
//        Subject = subject;
//        Class = aClass;
//        Room = room;
//        TimeOfCourse = timeOfCourse;
//
//        String[] startHour = {"07", "08", "09", "10", "10", "13", "13", "14", "15", "16"};
//        String[] startMinute = {"30", "15", "00", "00", "45", "00", "45", "30", "30", "15"};
//
//        String[] endHour = {"08", "09", "09", "10", "11", "13", "14", "15", "16", "17"};
//        String[] endMinute = {"15", "00", "45", "45", "30", "45", "30", "15", "15", "00"};
//
//        String temp = TimeOfCourse.substring(TimeOfCourse.length() - 10, TimeOfCourse.length());
//
//        temp = temp + " - " + startHour[Integer.parseInt("" + TimeOfCourse.charAt(29)) - 1] + ":" + startMinute[Integer.parseInt("" + TimeOfCourse.charAt(29)) - 1] + ":00";
//
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
//        try {
//                Date date = dateFormat.parse(temp);
//                startTime = date.getTime();
//        }
//        catch (ParseException e) {
//
//        }
//
//        temp = TimeOfCourse.substring(TimeOfCourse.length() - 10, TimeOfCourse.length());
//        temp = temp + " - " + endHour[Integer.parseInt("" + TimeOfCourse.charAt(49)) - 1] + ":" + endMinute[Integer.parseInt("" + TimeOfCourse.charAt(49)) - 1] + ":00";
//
//        try {
//            Date date = dateFormat.parse(temp);
//            endTime = date.getTime();
//        }
//        catch (ParseException e) {
//
//        }
//
//        temp = TimeOfNotify.substring(4, TimeOfNotify.length()) + ":00";
//
//        try {
//            Date date = dateFormat.parse(temp);
//            notifyTime = date.getTime();
//        }
//        catch (ParseException e) {
//
//        }
//
//        description = Title + "\n" + Teacher + "\n" + Faculty + "\n" + Subject + "\n" + Class + "\n" + Room + "\n" + TimeOfCourse + "\n" + TimeOfNotify;
//    }


    public Long getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(Long courseTime) {
        this.courseTime = courseTime;
    }

    public String getTitle() {
        return title;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public Long getNotifyTime() {
        return notifyTime;
    }

    public int getDateid() {
        return dateid;
    }

    public int getCourseid() {
        return courseid;
    }

    public String getClassid() {
        return classid;
    }

    public String getFaculty() {
        return faculty;
    }

    public long getNotifytime() {
        return notifyTime;
    }

    public String getRoom() {
        return room;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotifyTime(Long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public void setDateid(int dateid) {
        this.dateid = dateid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
