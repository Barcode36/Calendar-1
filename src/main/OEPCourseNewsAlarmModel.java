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

public class OEPCourseNewsAlarmModel {
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    private DbConnection dbConnection;
    private EventDetailAlertBox eventDetailAlertBox;

    private long lastTime;

    /**
     * Hàm này dùng để bắt đầu thread kiểm tra và thông báo các thông báo nghỉ, học bù từ OEP
     */
    public void start() {
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();

        // lấy thời gian quét kiểm tra thông báo gần nhất từ csdl
        lastTime = dbConnection.getLastOEPCourseNewsTime(calendar.get(Calendar.YEAR));

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
        // Nếu người dùng tắt chức năng thông báo nghỉ bù OEP
        // thì không kiểm tra
        if (!dbConnection.getOEPCourseNewsNotifyStatus())
            return;

        Calendar calendar = Calendar.getInstance();
        Alarm alarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("oepcoursenews"));

        // tạo hàng đợi chứa các thông báo nghỉ học bù mới từ OEP
        // qua hàm getUpdate và truyền vào danh sách lớp đăng ký lấy từ csdl
        List<Class> cours = getUpdate(dbConnection.getClassList());
        Platform.runLater(() -> {
            // với mỗi thông báo mới thì bật nhạc chuông và hiển thị thông báo
            for (Class aClass : cours) {
                Utils.playSound(alarm.getPath());
                eventDetailAlertBox.display(aClass, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(),
                        primaryScreenBounds.getHeight() - 450, true);
            }
        });

        // lưu lại thời gian quét kiểm tra thông báo gần nhất
        // vào csdl
        lastTime = System.currentTimeMillis() / 1000;
        dbConnection.updateLastOEPCourseNewsTime(lastTime, calendar.get(Calendar.YEAR));
    }

    /**
     * Hàm này dùng để lấy các thông báo nghỉ, học bù OEP
     * @param arrayListMMH: danh sách các mã lớp mà người dùng nhật
     * @return danh sách các thông báo nghỉ, học bù mới
     */
    private List<Class> getUpdate(List<String> arrayListMMH) {
        List<Class> classes = new ArrayList<Class>();

        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=2");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=3");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=4");
        for (int i = 0; i < arrayListMMH.size(); i++) {//arrayListMMH.size(); i++) {
            for (int j = 0; j < arrayListURL.size(); j++) {
                try {
                    Document document = Jsoup.connect(arrayListURL.get(j)).get();
                    if (document != null) {
                        Elements elements = document.select("div.content > article");
                        for (Element element : elements) {
                            element = element.getElementsContainingText(arrayListMMH.get(i)).first();
                            if (element != null) {
                                String temptext = element.text(); //getting text in the element

                                //Extract separated information in the whole text
                                int tempCBGD = temptext.indexOf("CBGD");
                                int tempKhoa = temptext.indexOf("Khoa/ Bộ môn");
                                int tempMon = temptext.indexOf("Môn học");
                                int tempLop = temptext.indexOf("Lớp");
                                int tempPhong = temptext.indexOf("Phòng");
                                int tempTietBatDau = temptext.indexOf("Tiết bắt đầu");
                                int tempTietKetThuc = temptext.indexOf("Tiết kết thúc");
                                int tempThoiGian = temptext.indexOf("Thời gian");
                                int tempXemThem = temptext.indexOf("Xem thêm");

                                String titletemp = temptext.substring(0, tempCBGD - 22 - 2); //CThongBao.Title
                                String announceTime = temptext.substring(tempCBGD - 19, tempCBGD - 1); //CThongBao.TimeStamp
                                if (convertTimeToMili(announceTime) > lastTime) {
                                    String teachertemp = temptext.substring(tempCBGD + 7, tempKhoa - 1);
                                    String facultytemp = temptext.substring(tempKhoa + 15, tempMon - 1);
                                    String subjecttemp = temptext.substring(tempMon + 10, tempLop - 1);
                                    String classtemp = temptext.substring(tempLop + 6, tempPhong - 1);
                                    String roomtemp = temptext.substring(tempPhong + 8, tempThoiGian - 1);
                                    String tietBatDau = temptext.substring(tempTietBatDau + 15, tempTietKetThuc - 3);
                                    String tietKetThuc = temptext.substring(tempTietKetThuc + 16, tempXemThem - 27);
                                    String courseTime = temptext.substring(tempXemThem - 11, tempXemThem - 1);

                                    Class aClass = new Class();
                                    aClass.setTitle(titletemp);
                                    aClass.setTeacher(teachertemp);
                                    aClass.setFaculty(facultytemp);
                                    aClass.setSubject(subjecttemp);
                                    aClass.setClassid(classtemp);
                                    aClass.setRoom(roomtemp);
                                    aClass.setStartTime(Integer.parseInt(tietBatDau));
                                    aClass.setEndTime(Integer.parseInt(tietKetThuc));
                                    aClass.setNotifyTime(convertTimeToMili(announceTime));
                                    aClass.setCourseTime(convertDDMMYYYYToMili(courseTime));

                                    Calendar calendar = convertDDMMYYYYToCalendarObject(courseTime);

                                    dbConnection.addOEPCourseNews(aClass, calendar.get(Calendar.DATE),
                                            calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

                                    classes.add(aClass);
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        return classes;
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
     * @param time Chuỗi ký tự thời gian dạng "dd/MM/yyyy"
     * @return Số miligiây của chuổi ký tự thời gian
     */
    private long convertDDMMYYYYToMili(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(time));
        } catch (ParseException e) {
            System.out.println("error converting time to mili");
            return -1;
        }
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * @param time - Chuỗi ký tự thời gian dạng "dd/MM/yyyy"
     * @return Đối tượng Calendar được tạo từ thông tin của chuỗi ký tự thời gian
     */
    private Calendar convertDDMMYYYYToCalendarObject(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(time));
        } catch (ParseException e) {
            System.out.println("error converting time to mili");
        }
        return calendar;
    }
}
