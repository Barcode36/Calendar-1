package main;

import java.sql.*;
import java.util.ArrayList;
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
                + "alarm TEXT, "
                + "isnotified INTEGER, "
                + "isdeleted INTEGER)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addEventToYearEventTable(Event event, int day, int month, int year) {
        createYearTable(year);
        //addDateToMonthTable(day, month, year);
        String query = "INSERT INTO `" + year + "_event`(dateid, title, starttime, endtime, notifytime, description, color) VALUES ( ?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getDateId(day, month));
            pstmt.setString(2, event.getTitle());
            pstmt.setLong(3, event.getStartTime());
            pstmt.setLong(4, event.getEndTime());
            pstmt.setLong(5, event.getNotifyTime());
            pstmt.setString(6, event.getDescription());
            pstmt.setString(7, event.getColor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addHoliday(Holiday holiday, int day, int month){
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

    public boolean addBirthday(Birthday birthday, int day, int month){
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

    public static void updateEvent(Event event, int month, int year) {
        try {
            String query = "UPDATE `" + month + "_" + year + "` SET name = ? , "
                    + "capacity = ? "
                    + "WHERE id = ?";
            //st.executeUpdate(query);
            //System.out.println("Wrote data to: "+hostname);
        } catch (Exception ex) {
        }
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


    public List<Event> getDayEvent(int year) {
        String query = "SELECT * FROM `" + year + "_event` WHERE isnotified = 0";
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

        String query = "SELECT * FROM `" + year + "_event` WHERE dateid = ?";
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
        String query = "SELECT * FROM `Holiday` WHERE dateid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Holiday> holidays = new ArrayList<Holiday>();

                int i = 0;
                while (resultSet.next()) {
                    Holiday newHoliday = new Holiday();
                    newHoliday.setDateid(resultSet.getInt("dateid"));
                    newHoliday.setName(resultSet.getString("name"));
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
        String query = "SELECT * FROM `Birthday` WHERE dateid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dateid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                List<Birthday> birthdays = new ArrayList<Birthday>();
                int i = 0;
                while (resultSet.next()) {
                    Birthday newBirthday = new Birthday();
                    newBirthday.setDateid(resultSet.getInt("dateid"));
                    newBirthday.setName(resultSet.getString("name"));
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

    public Boolean setDefaultColor(String type, String color) {
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