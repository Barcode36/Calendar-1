package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Utils {
    /**
     * Hàm dùng để chạy file nhạc
     * @param path đường dẫn của file nhạc
     */
    public static void playSound(String path) {
        Media m = new Media("file:///" + path);
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    /**
     * Hàn dùng để chuyển 1 đối tượng Color
     * sang chuỗi màu hex
     * @param color đối tượng color cần chuyển sáng chuỗi hex
     * @return chuỗi hex từ đối tượng color truyền vào
     */
    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
