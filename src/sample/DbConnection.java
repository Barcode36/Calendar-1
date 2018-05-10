package sample;

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

//    private void createMonthTable(int month, int year) {
//        String query = "CREATE Table `" + month + "_" + year
//                + "` (dateid INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "date TEXT)";
//        try (Connection conn = this.connect();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    private void createMonthEventTable(int month, int year) {
        //createMonthTable(month, year);
        String query = "CREATE Table `" + month + "_" + year + "_event`"
                + "(eventid INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dateid INTEGER, "
                + "title TEXT, "
                + "starttime INTEGER, "
                + "endtime INTEGER, "
                + "notifytime INTEGER, "
                + "description TEXT, "
                + "color TEXT)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    private void addDateToMonthTable(int day, int month, int year) {
//        PreparedStatement preparedStatement;
//        ResultSet resultSet;
//        int dateid = getDateId(day);
//        if (dateid == -1) {
//            String query = "INSERT INTO `" + month + "_" + year + "`(dateid, date) VALUES (NULL, ?)";
//            try (Connection conn = this.connect();
//                 PreparedStatement pstmt = conn.prepareStatement(query)) {
//                pstmt.setString(1, Utils.toddMMyyyy(day, month, year));
//                pstmt.executeUpdate();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }

    public void addEventToMonthEventTable(Event event, int day, int month, int year) {
        createMonthEventTable(month, year);
        //addDateToMonthTable(day, month, year);
        String query = "INSERT INTO `" + month + "_" + year + "_event`(dateid, title, starttime, endtime, notifytime, description, color) VALUES ("
                + getDateId(day)
                + ", '" + event.getTitle()
                + "', " + event.getStartTime()
                + ", " + event.getEndTime()
                + ", " + event.getNotifyTime()
                + ", '" + event.getDescription()
                + "', '" + event.getColor()
                + "')";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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


    private int getDateId(int day) {
        String query = "SELECT * FROM FixedDateID WHERE date = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, day);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet.getInt("dateid");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }


    public List<Event> getDayEvent(int day, int month, int year) {
        int dateid = getDateId(day);
        if (dateid == -1)
            return null;
        List<Event> events = new ArrayList<Event>();

        String query = "SELECT * FROM `" + month + "_" + year + "_event` WHERE dateid = " + dateid;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
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

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Holiday> getDayHoliday(int day, int month, int year) {
        int dateid = getDateId(day);
        if (dateid == -1)
            return null;
        List<Holiday> holidays = new ArrayList<Holiday>();

        String query = "SELECT * FROM `" + month + "_" + year + "_holiday` WHERE dateid = " + dateid;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                Holiday newHoliday = new Holiday();
                newHoliday.setDateid(resultSet.getInt("dateid"));
                newHoliday.setName(resultSet.getString("name"));
                holidays.add(newHoliday);
                i++;
            }
            return holidays;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Birthday> getDayBirthday(int day, int month, int year) {
        int dateid = getDateId(day);
        if (dateid == -1)
            return null;
        List<Birthday> birthdays = new ArrayList<Birthday>();

        String query = "SELECT * FROM `" + month + "_" + year + "_birthday` WHERE dateid = " + dateid;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                Birthday newBirthday = new Birthday();
                newBirthday.setDateid(resultSet.getInt("dateid"));
                newBirthday.setName(resultSet.getString("name"));
                birthdays.add(newBirthday);
                i++;
            }
            return birthdays;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public String getDefaultColor(String type) {
        String query = "SELECT * FROM `DefaultColor` WHERE type = ?";
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
