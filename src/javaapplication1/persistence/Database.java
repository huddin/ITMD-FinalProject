package javaapplication1.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//This class is responsible for establishing a connection with database
public class Database {

    // Code database URL
    private static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
    // Database credentials
    private static final String USER = "fp411";
    private static final String PASS = "411";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException sqe) {
            throw new RuntimeException("Failed to connect to database", sqe);
        }
    }
}
