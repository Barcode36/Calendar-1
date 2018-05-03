package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Utils {
    public static void playSound(String fileName) {
        Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/resources/" + fileName);
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static String toddMMyyyy(int day, int month, int year) {
        if (month >= 1 && month < 10) {
            if (day >= 1 && day < 10) {
                return "0" + day + "-0" + month + "-" + year;
            } else {
                return day + "-0" + month + "-" + year;
            }
        } else {
            if (day >= 1 && day < 10) {
                return "0" + day + "-" + month + "-" + year;
            } else {
                return day + "-" + month + "-" + year;
            }
        }
    }
}
