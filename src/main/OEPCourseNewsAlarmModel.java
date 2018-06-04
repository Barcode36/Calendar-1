package main;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
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

    public void start() {
        dbConnection = new DbConnection();
        Calendar calendar = Calendar.getInstance();
        lastTime = dbConnection.getLastOEPCourseNewsTime(calendar.get(Calendar.YEAR));
        eventDetailAlertBox = new EventDetailAlertBox();
        executorService.scheduleAtFixedRate(this::tick, 0, 30, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private void tick() {
        Alarm alarm = dbConnection.getAlarm(dbConnection.getDefaultAlarm("oepcoursenews"));
        Calendar calendar = Calendar.getInstance();
        List<Course> courses = getUpdate(dbConnection.getCourseList());
        Platform.runLater(() -> {
            for (Course course : courses) {
                Utils.playSound(alarm.getPath());
                eventDetailAlertBox.display(course, primaryScreenBounds.getWidth() - eventDetailAlertBox.getStageWidth(), primaryScreenBounds.getHeight() - eventDetailAlertBox.getStageHeight(), true);
            }
        });
        lastTime = System.currentTimeMillis() / 1000;
        dbConnection.updateLastOEPNewsTime(lastTime, calendar.get(Calendar.YEAR));
    }

    private List<Course> getUpdate(List<String> arrayListMMH) {
        List<Course> courses = new ArrayList<Course>();

        ArrayList<String> arrayListURL = new ArrayList<String>();
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=1");
        arrayListURL.add("https://oep.uit.edu.vn/vi/thong-bao-nghi-hoc-hoc-bu?page=2");
        boolean test;
        for (int i = 0; i < arrayListMMH.size(); i++) {//arrayListMMH.size(); i++) {
            for (int j = 0; j < arrayListURL.size(); j++) {
                test = false;
                try {
                    Document document = Jsoup.connect(arrayListURL.get(j)).get();
                    if (document != null) {
                        Elements elements = document.select("div.content > article");
                        for (Element element : elements) {
                            Element element2 = element.getElementsContainingText(arrayListMMH.get(i)).first();
                            if (element2 != null) {
                                String temptext = element2.text(); //getting text in the element

                                //Extract separated information in the whole text
                                int tempCBGD = temptext.indexOf("CBGD");
                                int tempKhoa = temptext.indexOf("Khoa/ Bộ môn");
                                int tempMon = temptext.indexOf("Môn học");
                                int tempLop = temptext.indexOf("Lớp");
                                int tempPhong = temptext.indexOf("Phòng");
                                int tempTietBatDau = temptext.indexOf("Tiết bắt đầu");
                                int tempTietKetThuc = temptext.indexOf("Tiết kết thúc");
                                int tempThoiGian = temptext.indexOf("Thời gian");
                                int tempNgay = temptext.indexOf("ngày");
                                int tempXemThem = temptext.indexOf("Xem thêm");

                                String titletemp = temptext.substring(0, tempCBGD - 22 - 2); //CThongBao.Title
                                String announceTime = temptext.substring(tempCBGD - 19, tempCBGD - 1); //CThongBao.TimeStamp
                                if (convertTimeToMili(announceTime) > lastTime) {
                                    //timetemp = timetemp.substring(4, timetemp.length()) + ":00";
                                    String teachertemp = temptext.substring(tempCBGD + 7, tempKhoa - 1);
                                    String facultytemp = temptext.substring(tempKhoa + 15, tempMon - 1);
                                    String subjecttemp = temptext.substring(tempMon + 10, tempLop - 1);
                                    String classtemp = temptext.substring(tempLop + 6, tempPhong - 1);
                                    String roomtemp = temptext.substring(tempPhong + 8, tempThoiGian - 1);
                                    String tietBatDau = temptext.substring(tempTietBatDau + 15, tempTietKetThuc - 3);
                                    String tietKetThuc = temptext.substring(tempTietKetThuc + 16, tempXemThem - 27);
                                    String courseTime = temptext.substring(tempXemThem - 11, tempXemThem - 1);
                                    //timetemp2 = "" + timetemp2.charAt(29) + " " + timetemp2.charAt(49);

                                    Course course = new Course();
                                    course.setTitle(titletemp);
                                    course.setTeacher(teachertemp);
                                    course.setFaculty(facultytemp);
                                    course.setSubject(subjecttemp);
                                    course.setClassid(classtemp);
                                    course.setRoom(roomtemp);
                                    course.setStartTime(Integer.parseInt(tietBatDau));
                                    course.setEndTime(Integer.parseInt(tietKetThuc));
                                    course.setNotifyTime(convertTimeToMili(announceTime));
                                    course.setCourseTime(convertDDMMYYYYToMili(courseTime));

                                    Calendar calendar = convertTimeToCalendarObject(announceTime);

                                    dbConnection.addOEPCourseNews(course, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

                                    courses.add(course);
                                }
                                String[] startHour = {"07", "08", "09", "10", "10", "13", "13", "14", "15", "16"};
                                String[] startMinute = {"30", "15", "00", "00", "45", "00", "45", "30", "30", "15"};

                                String[] endHour = {"08", "09", "09", "10", "11", "13", "14", "15", "16", "17"};
                                String[] endMinute = {"15", "00", "45", "45", "30", "45", "30", "15", "15", "00"};

                                //timetemp2 = timetemp2.substring(timetemp2.length() - 10, timetemp2.length()) + " - " + startHour[Integer.parseInt("" + timetemp2.charAt(29)) - 1] + ":" + startMinute[Integer.parseInt("" + timetemp2.charAt(29)) - 1] + ":00" +
                                //         " - " + endHour[Integer.parseInt("" + timetemp2.charAt(49)) - 1] + ":" + endMinute[Integer.parseInt("" + timetemp2.charAt(49)) - 1] + ":00";

                                /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                try {
                                    Date date = dateFormat.parse(timetemp2);
                                    long dateInLong = date.getTime();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                    alert1.setContentText(Long.toString(dateInLong));
                                    alert1.showAndWait();
                                } catch (ParseException e) {

                                }*/


                                //String temptext2 = titletemp + "\n" + timetemp + "\n" + teachertemp + "\n" + facultytemp + "\n" + subjecttemp + "\n" + classtemp + "\n" + roomtemp + "\n" + timetemp2;

//                                for (int y = 0; y < arrayListThongBao.size(); y++) {
//                                    if (arrayListThongBao.get(y).equals(temptext2)) {
//                                        test = true;
//                                        break;
//                                    }
//                                }
//
//                                if (test == false) {
//                                    arrayListThongBao.add(temptext2);
//                                    arrayListCourse.add(new Course(titletemp, timetemp, teachertemp, facultytemp, subjecttemp, classtemp, roomtemp, timetemp2));
//                                }

                                /*arrayListThongBao.add(temptext2);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(temptext2);
                                alert.showAndWait();*/
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

//        for (int i = 0; i < arrayListCourse.size(); i++) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText(arrayListCourse.get(i).getDescription());
//            alert.showAndWait();
//        }
        return courses;
    }

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
