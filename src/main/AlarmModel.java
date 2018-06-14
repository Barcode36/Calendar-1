package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmModel {

    private List<Event> events;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    private boolean isUpdating = false;
    private DbConnection dbConnection;
    private EventDetailAlertBox eventDetailAlertBox;

    /**
     * Hàm dùng để thêm 1 event vào hàng đợi thông báo
     *
     * @param event event cần thêm vào hàng đợi
     */
    public void addAlarm(Event event) {
        if (event == null)
            return;

        // nếu event thêm vào trước giờ hiện tại
        // thì không thêm vào hàng đợi thông báo
        if (event.isIsnotified())
            return;
        Event temp;

        // bật cờ báo đang update
        isUpdating = true;

        // nếu event truyền vào là event đã bị xóa
        // thì duyệt trong hàng đợi và xóa event đó nếu có
        if (event.isIsdeleted()) {
            for (int i = 0; i < events.size(); i++) {
                temp = events.get(i);
                if (temp.getEventid() == event.getEventid()) {
                    events.remove(temp);
                    return;
                }
            }
        }

        int pos = 0;

        // tìm chổ để add event được truyền vào
        // bằng cách so thời gian bắt đầu của event truyền vào với các event có trong hàng đợi
        for (int i = 0; i < events.size(); i++) {
            temp = events.get(i);
            if (temp.getEventid() == event.getEventid()) {
                events.remove(temp);
                i--;
            } else if ((temp.getStartTime() - temp.getNotifyTime()) < (event.getStartTime() - event.getNotifyTime())) {
                pos = i;
            }
        }
        events.add(pos, event);
        System.out.println("New event added to notify queue at pos " + pos);

        // tắt cờ báo đang update
        isUpdating = false;
    }

    public void removeAlarm(Event event) {
        events.remove(event);
    }

    /**
     * Hàm để bắt đầu chạy thread thông báo
     */
    public void start() {
        eventDetailAlertBox = new EventDetailAlertBox();
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();
        events = new ArrayList<Event>();
        DbConnection dbConnection = new DbConnection();

        // lấy các event chưa được thông báo (isNotified = 0) từ DB
        List<Event> temp = dbConnection.getDayEvent(calendar.get(Calendar.YEAR));
        if (temp != null) {
            events.addAll(temp);
        }

        executorService.scheduleAtFixedRate(this::tick, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * Hàm để dừng thread thông báo
     */
    public void stop() {
        executorService.shutdownNow();
    }

    /**
     * Hàm này được chạy mỗi khoảng thời gian được quy định trong hàm start
     */
    private void tick() {
        // nếu đang update hàng đợi thì không thông báo
        // để tránh việc xung đột
        if (isUpdating)
            return;

        long current = System.currentTimeMillis() / 1000;
        Platform.runLater(() -> {
            // duyệt qua hàng đợi và kiểm tra xem event nào có thời gian bắt đầu
            // nhỏ hơn thời gian hệ thống hiện tại thì thông báo
            for (Event event : events) {
                if (event.getStartTime() - event.getNotifyTime() <= current) {
                    System.out.println("Alerting event");

                    // set cho event đã được thông báo
                    event.setIsnotified(true);
                    dbConnection.updateEvent(event);

                    // chạy nhạc chuông
                    Utils.playSound(dbConnection.getAlarm(event.getAlarmID()).getPath());

                    // loại event khỏi hàng đợi vì đã thông báo xong
                    removeAlarm(event);

                    // hiện cửa sổ thông báo
                    eventDetailAlertBox.display(event, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(),
                            primaryScreenBounds.getHeight() - eventDetailAlertBox.getStageHeight(), true);
                }
            }
        });
    }
}
