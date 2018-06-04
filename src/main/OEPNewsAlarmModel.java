package main;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OEPNewsAlarmModel {
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    private DbConnection dbConnection;
    private EventDetailAlertBox eventDetailAlertBox;

    private long lastTime;

    public void start() {
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();
        lastTime = dbConnection.getLastOEPNewsTime(calendar.get(Calendar.YEAR));
        eventDetailAlertBox = new EventDetailAlertBox();
        executorService.scheduleAtFixedRate(this::tick, 0, 30, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void tick() {
        Alarm alarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("oepnews"));
        Calendar calendar = Calendar.getInstance();
        List<OEPNews> oepNewsList = getUpdateDAA();
        Platform.runLater(() -> {
            for (OEPNews oepNews : oepNewsList){
                Utils.playSound(alarm.getPath());
                eventDetailAlertBox.display(oepNews, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(), primaryScreenBounds.getHeight() - eventDetailAlertBox.getStageHeight(), true);
            }
        });
        lastTime = System.currentTimeMillis()/1000;
        dbConnection.updateLastOEPNewsTime(lastTime, calendar.get(Calendar.YEAR));
    }

    private List<OEPNews> getUpdateDAA() {
        List<OEPNews> oepNewsList = new ArrayList<OEPNews>();

        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-chung?page=2");
        for (int j = 0; j < arrayListURL.size(); j++) {
            try {
                Document document = Jsoup.connect(arrayListURL.get(j)).get();
                if (document != null) {
                    Elements contentElements = document.select("div.content > article");
                    contentElements = contentElements.select("h2 > a");

                    Elements contentTimeElements = document.select("div.content > article");
                    contentTimeElements = contentTimeElements.select("div.submitted");

                    for (int i = 0; i < contentElements.size(); i++) {
                        Element timeElement = contentTimeElements.get(i).getAllElements().first();
                        if (timeElement != null) {
                            String tempTime = timeElement.text();
                            tempTime = tempTime.substring(4, tempTime.length());
                            if (!(convertTimeToMili(tempTime) < lastTime)) {
                                Element contentElement = contentElements.get(i).getAllElements().first();
                                OEPNews oepNews = new OEPNews();
                                oepNews.setUrl(contentElement.attr("abs:href"));
                                oepNews.setTitle(contentElement.text());
                                oepNews.setNotifyTime(convertTimeToMili(tempTime));
                                oepNewsList.add(oepNews);
                                Calendar calendar = convertTimeToCalendarObject(tempTime);
                                if (calendar!=null){
                                    dbConnection.addOEPNews(oepNews, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return oepNewsList;
    }

    private long convertTimeToMili(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy - HH:mm").parse(time));
        } catch (ParseException e) {
            System.out.println("error converting time to mili");
            return -1;
        }
        return calendar.getTimeInMillis()/1000;
    }

    private Calendar convertTimeToCalendarObject(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy - HH:mm").parse(time));
        } catch (ParseException e) {
            System.out.println("error converting time to mili");
            return null;
        }
        return calendar;
    }

}