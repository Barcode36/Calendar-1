package sample;

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

        String temp = TimeOfNotify.substring(4, 6);
    }
}
