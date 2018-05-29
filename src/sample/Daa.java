package sample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Daa {
    private String Title;
    private String TimeOfNotify;
    private int dateid;
    private String title;
    private Long notifyTime;
    private String description;
    private String color;

    public Daa(String title, String timeOfNotify) {
        Title = title;
        TimeOfNotify = timeOfNotify;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        try {
            Date date = dateFormat.parse(TimeOfNotify.substring(4, TimeOfNotify.length()));
            notifyTime = date.getTime();
        } catch (ParseException e) {
        }

        this.title = Title;
        this.description = Title + " " + TimeOfNotify;
    }
}
