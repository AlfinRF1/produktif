/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TAMBAH;
/**
 *
 * @author alvon
 */
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
public class Koneksi {
    private static Connection conn;
   public static Connection koneksiDB() {
    Connection conn = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/umkm";
        String user = "root";
        String password = "";
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("Koneksi berhasil");
    } catch (ClassNotFoundException e) {
        System.out.println("Driver tidak ditemukan: " + e.getMessage());
    } catch (SQLException e) {
        System.out.println("Koneksi gagal: " + e.getMessage());
    }
    return conn;
}
}