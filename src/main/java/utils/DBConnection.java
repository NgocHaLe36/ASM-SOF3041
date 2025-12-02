package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ABC;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✔ SQL Server connected!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Database connection failed");
        }
        return conn;
    }
}
