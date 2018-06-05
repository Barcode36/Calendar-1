package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Utils {
    public static void playSound(String path) {
        Media m = new Media("file:///" + path);
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
