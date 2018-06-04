package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbConnection {

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:Calendar.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createYearTable(int year) {
        //createMonthTable(month, year);
        String query = "CREATE Table `" + year + "_event`"
                + "(eventid INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dateid INTEGER, "
                + "title TEXT, "
                + "starttime INTEGER, "
                + "endtime INTEGER, "
                + "notifytime INTEGER, "
                + "description TEXT, "
                + "color TEXT, "
                + "alarmid int, "
                + "isnotified INTEGER, "
                + "isdeleted INTEGER)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createOEPNewsYearTable(int year, long lastTime) {
        boolean result = true;
        String query = "CREATE Table `OEP_News_" + year
                + "( `newsid` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`dateid` INTEGER, "
                + "`title` TEXT, "
                + "`notifytime` INTEGER, "
                + "`url` TEXT )";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result =false;
            System.out.println(e.getMessage());
        }
        if (result){
            OEPNews oepNews = new OEPNews();
            oepNews.setTitle("");
            oepNews.setNotifyTime(lastTime);
            oepNews.setUrl("");
            addOEPNews(oepNews, 1, 1, year);
        }
    }

    public boolean addEventToYearEventTable(Event event, int day, int month, int year) {
        createYearTable(year);
        //addDateToMonthTable(day, month, year);
        String query = "INSERT INTO `" + year + "_event`(dateid, title, starttime, endtime, notifytime, description, color, alarmid, isnotified) VALUES ( ?,?,?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(day, month));
            pstmt.setString(2, event.getTitle());
            pstmt.setLong(3, event.getStartTime());
            pstmt.setLong(4, event.getEndTime());
            pstmt.setLong(5, event.getNotifyTime());
            pstmt.setString(6, event.getDescription());
            pstmt.setString(7, event.getColor());
            pstmt.setInt(8, event.getAlarmID());
            if (event.isIsnotified())
                pstmt.setInt(9, 1);
            else
                pstmt.setInt(9, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addHoliday(Holiday holiday, int day, int month) {
        String query = "INSERT INTO `Holiday`(dateid, name) VALUES (?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(day, month));
            pstmt.setString(2, holiday.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addBirthday(Birthday birthday, int day, int month) {
        String query = "INSERT INTO `Birthday`(dateid, name) VALUES (?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(day, month));
            pstmt.setString(2, birthday.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addOEPNews(OEPNews oepNews, int day, int month, int year) {
        createOEPNewsYearTable(year, oepNews.getNotifyTime());
        String query = "INSERT INTO `OEP_News_"+year+"`(dateid, title, notifytime, url) VALUES (?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(day, month));
            pstmt.setString(2, oepNews.getTitle());
            pstmt.setLong(3, oepNews.getNotifyTime());
            pstmt.setString(4, oepNews.getUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Event getLastEvent(int year) {
        String query = "SELECT * FROM `" + year + "_event` order by eventid desc limit 1";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                Event event = new Event();
                event.setEventid(resultSet.getInt("eventid"));
                event.setDateid(resultSet.getInt("dateid"));
                event.setTitle(resultSet.getString("title"));
                event.setStartTime(resultSet.getLong("starttime"));
                event.setEndTime(resultSet.getLong("endtime"));
                event.setEventid(resultSet.getInt("eventid"));
                event.setNotifyTime(resultSet.getLong("notifytime"));
                event.setDescription(resultSet.getString("description"));
                event.setColor(resultSet.getString("color"));
                event.setAlarmID(resultSet.getInt("alarmid"));
                int isnotified = resultSet.getInt("isnotified");
                if (isnotified == 0)
                    event.setIsnotified(false);
                else
                    event.setIsnotified(true);
                int isdeleted = resultSet.getInt("isdeleted");
                if (isdeleted == 0)
                    event.setIsdeleted(false);
                else
                    event.setIsdeleted(true);
                return event;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean updateEvent(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime() * 1000);
        createYearTable(calendar.get(Calendar.YEAR));
        String query = "UPDATE `" + calendar.get(Calendar.YEAR) + "_event` SET dateid = ?, title = ?, starttime = ?, endtime = ?, notifytime = ?, description = ?, color = ?, alarmid = ?, isnotified = ?, isdeleted = ? WHERE eventid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1));
            pstmt.setString(2, event.getTitle());
            pstmt.setLong(3, event.getStartTime());
            pstmt.setLong(4, event.getEndTime());
            pstmt.setLong(5, event.getNotifyTime());
            pstmt.setString(6, event.getDescription());
            pstmt.setString(7, event.getColor());
            pstmt.setInt(8, event.getAlarmID());
            if (event.isIsnotified()) {
                pstmt.setInt(9, 1);
            } else {
                pstmt.setInt(9, 0);
            }
            if (event.isIsdeleted()) {
                pstmt.setInt(10, 1);
            } else {
                pstmt.setInt(10, 0);
            }
            pstmt.setInt(11, event.getEventid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateBirthday(Birthday birthday) {
        String query = "UPDATE `Birthday` SET name = ?, isdeleted = ? WHERE birthdayid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, birthday.getName());
            if (birthday.isDeleted()) {
                pstmt.setInt(2, 1);
            } else {
                pstmt.setInt(2, 0);
            }
            pstmt.setInt(3, birthday.getBirthdayid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateHoliday(Holiday holiday) {
        String query = "UPDATE `Holiday` SET name = ?, isdeleted = ? WHERE holidayid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, holiday.getName());
            if (holiday.isDeleted()) {
                pstmt.setInt(2, 1);
            } else {
                pstmt.setInt(2, 0);
            }
            pstmt.setInt(3, holiday.getHolidayid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private int getDateId(int day, int month) {
        String query = "SELECT * FROM FixedDateID WHERE date = ? and month = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, day);
            pstmt.setInt(2, month);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getInt("dateid");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public int getHolidayDateId(int holidayid) {
        String query = "SELECT * FROM Holiday WHERE holidayid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, holidayid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getInt("dateid");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public int getBirthdayDateId(int birthdayid) {
        String query = "SELECT * FROM Birthday WHERE birthdayid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, birthdayid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getInt("dateid");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public Calendar getDate(int dateid) {
        Calendar calendar = Calendar.getInstance();
        String query = "SELECT * FROM `FixedDateID` WHERE dateid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                calendar.set(Calendar.DATE, resultSet.getInt("date"));
                calendar.set(Calendar.MONTH, resultSet.getInt("month") - 1);
                return calendar;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Event> getDayEvent(int year) {
        String query = "SELECT * FROM `" + year + "_event` WHERE isnotified = 0 and isdeleted = 0";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Event> events = new ArrayList<Event>();
                int i = 0;
                while (resultSet.next()) {
                    Event newEvent = new Event();
                    newEvent.setEventid(resultSet.getInt("eventid"));
                    newEvent.setDateid(resultSet.getInt("dateid"));
                    newEvent.setTitle(resultSet.getString("title"));
                    newEvent.setStartTime(resultSet.getLong("starttime"));
                    newEvent.setEndTime(resultSet.getLong("endtime"));
                    newEvent.setNotifyTime(resultSet.getLong("notifytime"));
                    newEvent.setDescription(resultSet.getString("description"));
                    newEvent.setColor(resultSet.getString("color"));
                    newEvent.setAlarmID(resultSet.getInt("alarmid"));
                    events.add(newEvent);
                    i++;
                }
                sortEventTimeDesc(events);
                return events;
            }

        } catch (SQLException e) {
            if (e.toString().contains("no such table"))
                System.out.println("error");
            //System.out.println(e.getMessage());
        }

        return null;

    }

    public List<Event> getDayEvent(int day, int month, int year) {
        int dateid = getDateId(day, month);
        if (dateid == -1)
            return null;

        String query = "SELECT * FROM `" + year + "_event` WHERE dateid = ? and isdeleted = 0";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Event> events = new ArrayList<Event>();
                int i = 0;
                while (resultSet.next()) {
                    Event newEvent = new Event();
                    newEvent.setEventid(resultSet.getInt("eventid"));
                    newEvent.setDateid(resultSet.getInt("dateid"));
                    newEvent.setTitle(resultSet.getString("title"));
                    newEvent.setStartTime(resultSet.getLong("starttime"));
                    newEvent.setEndTime(resultSet.getLong("endtime"));
                    newEvent.setNotifyTime(resultSet.getLong("notifytime"));
                    newEvent.setDescription(resultSet.getString("description"));
                    newEvent.setColor(resultSet.getString("color"));
                    newEvent.setAlarmID(resultSet.getInt("alarmid"));
                    events.add(newEvent);
                    i++;
                }
                sortEventTimeDesc(events);
                return events;
            }

        } catch (SQLException e) {
            if (e.toString().contains("no such table"))
                System.out.println("error");
            //System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Holiday> getDayHoliday(int day, int month, int year) {
        int dateid = getDateId(day, month);
        if (dateid == -1)
            return null;
        String query = "SELECT * FROM `Holiday` WHERE dateid = ? and isdeleted = 0";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Holiday> holidays = new ArrayList<Holiday>();

                int i = 0;
                while (resultSet.next()) {
                    Holiday newHoliday = new Holiday();
                    newHoliday.setHolidayid(resultSet.getInt("holidayid"));
                    newHoliday.setDateid(resultSet.getInt("dateid"));
                    newHoliday.setName(resultSet.getString("name"));
                    int isDeleted = resultSet.getInt("isdeleted");
                    if (isDeleted == 1)
                        newHoliday.setDeleted(true);
                    else
                        newHoliday.setDeleted(false);
                    holidays.add(newHoliday);
                    i++;
                }
                return holidays;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Birthday> getDayBirthday(int day, int month, int year) {
        int dateid = getDateId(day, month);
        if (dateid == -1)
            return null;
        String query = "SELECT * FROM `Birthday` WHERE dateid = ? and isdeleted = 0";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Birthday> birthdays = new ArrayList<Birthday>();
                int i = 0;
                while (resultSet.next()) {
                    Birthday newBirthday = new Birthday();
                    newBirthday.setBirthdayid(resultSet.getInt("birthdayid"));
                    newBirthday.setDateid(resultSet.getInt("dateid"));
                    newBirthday.setName(resultSet.getString("name"));
                    newBirthday.setName(resultSet.getString("name"));
                    int isDeleted = resultSet.getInt("isdeleted");
                    if (isDeleted == 1)
                        newBirthday.setDeleted(true);
                    else
                        newBirthday.setDeleted(false);
                    birthdays.add(newBirthday);
                    i++;
                }
                return birthdays;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public long getLastOEPNewsTime(int year){
        String query = "SELECT * FROM `OEP_News_`"+ year +" WHERE newsid = 1";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getLong("notifytime");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }


    public String getDefaultColor(String type) {
        String query = "SELECT * FROM `DefaultSettings` WHERE type = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, type);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getString("color");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public boolean setDefaultColor(String type, String color) {
        String query = "UPDATE DefaultSettings SET color = ? WHERE type = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, color);
            pstmt.setString(2, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public int getDefaultAlarm(String type) {
        String query = "SELECT * FROM `DefaultSettings` WHERE type = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, type);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getInt("alarmid");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean setDefaultAlarm(String type, int alarmid) {
        String query = "UPDATE DefaultSettings SET alarmid = ? WHERE type = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, alarmid);
            pstmt.setString(2, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addAlarm(Alarm alarm) {
        String query = "INSERT INTO `Alarm`(name, path) VALUES (?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, alarm.getName());
            pstmt.setString(2, alarm.getPath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public List<Alarm> getAlarmList() {
        String query = "SELECT * FROM `Alarm`";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Alarm> alarms = new ArrayList<Alarm>();
                int i = 0;
                while (resultSet.next()) {
                    Alarm newAlarm = new Alarm();
                    newAlarm.setAlarmid(resultSet.getInt("alarmid"));
                    newAlarm.setName(resultSet.getString("name"));
                    newAlarm.setPath(resultSet.getString("path"));
                    alarms.add(newAlarm);
                    i++;
                }
                return alarms;
            }

        } catch (SQLException e) {
            if (e.toString().contains("no such table"))
                System.out.println("error");
            //System.out.println(e.getMessage());
        }

        return null;
    }

    public Alarm getAlarm(int alarmid) {
        String query = "SELECT * FROM `Alarm` WHERE alarmid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, alarmid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                Alarm alarm = new Alarm();
                alarm.setAlarmid(resultSet.getInt("alarmid"));
                alarm.setName(resultSet.getString("name"));
                alarm.setPath(resultSet.getString("path"));
                return alarm;
            }
        } catch (SQLException e) {
            if (e.toString().contains("no such table"))
                System.out.println("error");
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public Boolean checkUser(String user, String password) {
        String query = "SELECT * FROM `User` WHERE UserName = ? and Password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void sortEventTimeDesc(List<Event> events) {
        for (int i = 0; i <= events.size() - 2; i++) {
            for (int j = i + 1; j <= events.size() - 1; j++) {
                if (events.get(i).getStartTime() > events.get(j).getStartTime()) {
                    Event temp = events.get(i);
                    events.set(i, events.get(j));
                    events.set(j, temp);
                }
            }
        }
    }

}