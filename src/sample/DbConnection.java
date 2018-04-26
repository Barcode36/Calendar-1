package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DbConnection {
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;

    private static String hostname = "localhost"; //Enter hostname for database here as hostname
    private static int port = 3306;
    private static String dbUsername = "user"; //Enter username for hostname here
    private static String dbPassword = ""; //Enter password for hostname here. Unless no pass then leave blank
    private static String dbName = "calendar"; //Intended name of the database in the DB to be created

    private static String username;
    private static String password;
    private static String usernameDB;

    public static void connectToDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false", dbUsername, dbPassword);
            st = con.createStatement();
            //System.out.println("Connected to: "+hostname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createMonthTable(int month, int year) {
        try {
            String query = ("CREATE Table " + month + "_" + year
                    + "(dateid INT(4) NOT NULL AUTO_INCREMENT, "
                    + "date DATE NOT NULL, "
                    + "PRIMARY KEY (dateid));");
            st.executeUpdate(query);
            //System.out.println("creating table for \""+username+"\" in "+hostname);
        } catch (Exception ex) {
            //if(ex.toString().contains("already exists"))
            //System.out.println("Did not create table for \""+username+"\".  Table already exists.");
        }
    }

    public static void createMonthEventTable(int month, int year) {
        try {
            String query = ("CREATE Table " + month + "_" + year + "_event"
                    + "(eventid INT(5) NOT NULL AUTO_INCREMENT, "
                    + "dateid INT(5) NOT NULL, "
                    + "title TEXT NOT NULL, "
                    + "starttime BIGINT NOT NULL, "
                    + "endtime BIGINT NOT NULL, "
                    + "notifytime INT(5) NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "PRIMARY KEY (eventid));");
            st.executeUpdate(query);
            //System.out.println("creating table for \""+username+"\" in "+hostname);
        } catch (Exception ex) {
            //if(ex.toString().contains("already exists"))
            //System.out.println("Did not create table for \""+username+"\".  Table already exists.");
        }
    }

    public static void addDateToMonthTable(Calendar c) {
        createMonthTable(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = c.getTime();
        try {
            String selectQuery = ("SELECT dateid FROM " + c.get(Calendar.MONTH) + "_" + c.get(Calendar.YEAR) + " WHERE date = '" + format.format(date) + "'");
            rs = st.executeQuery(selectQuery);
            // Nếu ko có ngày trong bảng thì tạo mới
            if (!rs.first()) {
                try {
                    String insertQuery = ("INSERT INTO " + c.get(Calendar.MONTH) + "_" + c.get(Calendar.YEAR) + "(dateid, date) VALUES (NULL, '" + format.format(date) + "')");
                    st.executeUpdate(insertQuery);
                    //System.out.println("creating table for \""+username+"\" in "+hostname);
                } catch (Exception ex) {
                    //if(ex.toString().contains("already exists"))
                    //System.out.println("Did not create table for \""+username+"\".  Table already exists.");
                }
            }
        } catch (Exception ex) {
            //System.out.println("Could not retrieve data from database");
        }
    }

    public static void addEventToMonthEventTable(Event event, int month, int year) {
        createMonthEventTable(month, year);
        try {
            String query = "INSERT INTO " + month + "_" + year + "_event" + " (dateid, title, startime, endtime, notifytime, description) VALUES ("
                    + event.getdateid()
                    + ", '" + event.getTitle()
                    + "', " + event.getStartTime()
                    + ", " + event.getEndTime()
                    + ", " + event.getNotifyTime()
                    + ", '" + event.getDescription()
                    + "')";
            st.executeUpdate(query);
            //System.out.println("Wrote data to: "+hostname);
        } catch (Exception ex) {
        }
    }



}
