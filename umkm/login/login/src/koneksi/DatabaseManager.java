package koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/umkm";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan", e);
        } catch (SQLException e) {
            throw new SQLException("Gagal koneksi ke database: " + e.getMessage(), e);
        }
    }
}