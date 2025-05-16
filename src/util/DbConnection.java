package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
public class DbConnection {

    private static DbConnection instance = null;
    private Connection conn = null;

    private DbConnection() {}

    private void init() throws SQLException {
        final String DB_URL = "jdbc:mysql://localhost:3306/flights";
        final String USER = "root";
        final String PASS = "1234";

        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connected to database");
    }

    public Connection getConnection() {
        return conn;
    }

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DbConnection();
            instance.init();
        }
        return instance.getConnection();
    }
}