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

public class CTSVNewsAlarmModel {
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    private DbConnection dbConnection;
    private EventDetailAlertBox eventDetailAlertBox;

    private long lastTime;

    /**
     * Hàm này dùng để bắt đầu thread kiểm tra và thông báo các thông báo chung từ OEP
     */
    public void start() {
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();

        // lấy thời gian quét kiểm tra thông báo gần nhất từ csdl
        lastTime = dbConnection.getLastCTSVNewsTime(calendar.get(Calendar.YEAR));

        eventDetailAlertBox = new EventDetailAlertBox();
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
        // Nếu người dùng tắt chức năng thông báo chung OEP
        // thì không kiểm tra
        if (!dbConnection.getCTSVNewsNotifyStatus())
            return;

        Calendar calendar = Calendar.getInstance();
        Alarm alarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("ctsvnews"));

        // tạo hàng đợi chứa các thông báo chung mới từ OEP
        // qua hàm getUpdateDAA
        List<CTSVNews> ctsvNewsList = getUpdateCTSV();
        Platform.runLater(() -> {
            // với mỗi thông báo mới thì bật nhạc chuông và hiển thị thông báo
            for (CTSVNews ctsvNews : ctsvNewsList) {
                Utils.playSound(alarm.getPath());
                eventDetailAlertBox.display(ctsvNews, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(),
                        primaryScreenBounds.getHeight() - eventDetailAlertBox.getStageHeight(), true);
            }
        });

        // lưu lại thời gian quét kiểm tra thông báo gần nhất
        // vào csdl
        lastTime = System.currentTimeMillis() / 1000;
        dbConnection.updateLastCTSVNewsTime(lastTime, calendar.get(Calendar.YEAR));
    }

    /**
     * Hàm dùng để lấy thông tin trang web thông báo chung OEP
     *
     * @return danh sách các thông báo chung mới
     */
    private List<CTSVNews> getUpdateCTSV() {
        List<CTSVNews> ctsvNewsList = new ArrayList<CTSVNews>();

        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://ctsv.uit.edu.vn/thong-bao");
        arrayListURL.add("https://ctsv.uit.edu.vn/thong-bao?page=1");
        arrayListURL.add("https://ctsv.uit.edu.vn/thong-bao?page=2");

        // Duyệt qua từng trang web
        for (int j = 0; j < arrayListURL.size(); j++) {
            try {
                Document document = Jsoup.connect(arrayListURL.get(j)).get();
                if (document != null) {

                    // lấy thông tin về thông báo (tiêu đề và đượng link) trong 1 trang web
                    Elements contentElements = document.select("div.content > article");
                    contentElements = contentElements.select("h2 > a");

                    // lấy ngày đăng của thông báo
                    Elements contentTimeElements = document.select("div.content > article");
                    contentTimeElements = contentTimeElements.select("div.submitted");

                    // duyệt qua từng thông báo lấy được từ trang web
                    for (int i = 0; i < contentElements.size(); i++) {
                        Element timeElement = contentTimeElements.get(i).getAllElements().first();
                        if (timeElement != null) {
                            // lấy chuỗi string thời gian đăng của thông báo thứ i
                            String tempTime = timeElement.text();

                            // không lấy thứ từ chuỗi thơi gian (thứ 2, thử ba,...)
                            tempTime = tempTime.substring(4, tempTime.length());

                            // so thời gian đăng thông báo với thời gian quét thông báo gần nhất
                            // nếu thời gian đăng lớn hơn thì lấy thông báo
                            if (!(convertTimeToMili(tempTime) < lastTime)) {
                                Element contentElement = contentElements.get(i).getAllElements().first();
                                CTSVNews ctsvNews = new CTSVNews();

                                // lấy đường link của thông báo
                                ctsvNews.setUrl(contentElement.attr("abs:href"));

                                // lấy tiêu đề của thông báo
                                ctsvNews.setTitle(contentElement.text());

                                // lấy thời gian đăng của thông báo
                                ctsvNews.setNotifyTime(convertTimeToMili(tempTime));
                                ctsvNewsList.add(ctsvNews);
                                Calendar calendar = convertTimeToCalendarObject(tempTime);
                                if (calendar != null) {
                                    // thêm thông báo mới lấy vào csdl
                                    dbConnection.addCTSVNews(ctsvNews, calendar.get(Calendar.DATE),
                                            calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return ctsvNewsList;
    }


    /**
     * @param time Chuỗi ký tự thời gian dạng "dd/MM/yyyy - HH:mm"
     * @return Số miligiây của chuổi ký tự thời gian
     */
    private long convertTimeToMili(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy - HH:mm").parse(time));
        } catch (ParseException e) {
            System.out.println("error converting time to mili");
            return -1;
        }
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * @param time - Chuỗi ký tự thời gian dạng "dd/MM/yyyy - HH:mm"
     * @return Đối tượng Calendar được tạo từ thông tin của chuỗi ký tự thời gian
     */
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
