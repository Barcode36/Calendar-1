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

    public void addAlarm(Event event) {
        if (event.isIsnotified())
            return;
        isUpdating = true;
        int pos = 0;
        Event temp;
        for (int i = 0; i < events.size(); i++) {
            temp = events.get(i);
            if ((temp.getStartTime() - temp.getNotifyTime()) > (event.getStartTime() - event.getNotifyTime())) {
                pos = i;
            }
        }
        events.add(pos, event);
        System.out.println("New event added to notify queue at pos " + pos);
        isUpdating = false;
    }

    public void removeAlarm(Event event) {
        events.remove(event);
    }

    public void start() {
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();
        events = new ArrayList<Event>();
        DbConnection dbConnection = new DbConnection();
        List<Event> temp = dbConnection.getDayEvent(calendar.get(Calendar.YEAR));
        if (temp != null) {
            events.addAll(temp);
        }
        executorService.scheduleAtFixedRate(this::tick, 0, 30, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void tick() {
        if (isUpdating)
            return;
        long current = System.currentTimeMillis() / 1000;
        Platform.runLater(() -> {
            for (Event event : events) {
                if (event.getStartTime() - event.getNotifyTime() <= current) {
                    System.out.println("Alerting event");
                    event.setIsnotified(true);
                    dbConnection.updateEvent(event);
                    Utils.playSound(dbConnection.getAlarm(event.getAlarmID()).getPath());
                    EventDetailAlertBox eventDetailAlertBox = new EventDetailAlertBox();
                    eventDetailAlertBox.display(event, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(), primaryScreenBounds.getHeight() - eventDetailAlertBox.getStageHeight(), true);
                    removeAlarm(event);
                }
            }
        });
    }
}
