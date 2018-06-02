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
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private boolean isUpdating = false;

    public void addAlarm(Event event) {
        int pos = 0;
        Event temp;
        for (int i = 0; i < events.size(); i++) {
            temp = events.get(i);
            if ((temp.getStartTime() - temp.getNotifyTime()) > (event.getStartTime() - event.getNotifyTime())) {
                pos = i;
            }
        }
        events.add(pos, event);
    }

    public void removeAlarm(Event event) {
        events.remove(event);
    }

    public void start() {
        Calendar calendar = Calendar.getInstance();
        events = new ArrayList<Event>();
        DbConnection dbConnection = new DbConnection();
        List<Event> temp = dbConnection.getDayEvent(calendar.get(Calendar.YEAR));
        if(temp!=null){
            events.addAll(temp);
        }
        executorService.scheduleAtFixedRate(this::tick, 0, 30, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void tick() {
        long current = System.currentTimeMillis()/1000;
        Platform.runLater(() -> {
            for (Event event : events) {
                if (event.getStartTime() - event.getNotifyTime() <= current) {
                    System.out.println("Alerting event");
                    Utils.playSound("alarm1.mp3");
                    EventDetailAlertBox eventDetailAlertBox = new EventDetailAlertBox();
                    eventDetailAlertBox.display(event, primaryScreenBounds.getWidth() / 2, primaryScreenBounds.getHeight() / 2);
                    removeAlarm(event);
                }
            }
        });
    }
}
