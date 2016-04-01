package de.vincidev.bungeeban.util;

import javax.xml.transform.Result;
import java.sql.*;

public class SQLite {

    private String filename;

    private Connection conn;

    public SQLite(String filename) {
        this.filename = filename;
    }

    public boolean isConnected() {
        return this.conn != null;
    }

    public void openConnection() {
        if(!isConnected()) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        if(isConnected()) {
            try {
                this.conn.close();
                this.conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(String query) {
        if(isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement pst = conn.prepareStatement(query);
                        pst.executeUpdate();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Deprecated
    public ResultSet getResult(String query) {
        if(isConnected()) {
            try {
                PreparedStatement pst = conn.prepareStatement(query);
                return pst.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void createTable(String tablename, String options) {
        update("CREATE TABLE " + tablename + "(" + options + ")");
    }

    public void createTableIfNotExists(String tablename, String options) {
        update("CREATE TABLE IF NOT EXISTS " + tablename + "(" + options + ")");
    }
    public Connection getConn() {
        return conn;
    }

    public String getFilename() {
        return filename;
    }
}
