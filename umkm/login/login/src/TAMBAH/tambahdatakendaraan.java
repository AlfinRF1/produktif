/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TAMBAH;

/**
 *
 * @author alvon
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class tambahdatakendaraan {

    public static void insertKendaraan(String platNomor, String jenisKendaraan, String warnaKendaraan, String tipeKendaraan, String idBarang) {
        String url = "jdbc:mysql://localhost:3306/umkm";
        String user = "root";
        String password = "";

        String sql = "INSERT INTO kendaraan (Plat_Nomor, Jenis_Kendaraan, Warna_Kendaraan, Tipe_Kendaraan, Id_Barang) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, platNomor);
            stmt.setString(2, jenisKendaraan);
            stmt.setString(3, warnaKendaraan);
            stmt.setString(4, tipeKendaraan);
            stmt.setString(5, idBarang);
            
            stmt.executeUpdate();
            System.out.println("Data kendaraan berhasil ditambahkan.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
