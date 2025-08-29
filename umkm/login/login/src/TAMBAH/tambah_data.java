package TAMBAH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import koneksi.koneksi;

public class tambah_data {
    public void handleTambah(String kode, String noKTP, String tanggal, String idBarang) {
        String sql = "INSERT INTO transaksi_beli (kode_pembelian, No_Ktp_Supplier, tanggal, Id_barang) VALUES (?, ?, ?, ?)";
        try (Connection conn = koneksi.koneksiDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kode);
            pstmt.setString(2, noKTP);
            pstmt.setString(3, tanggal);
            pstmt.setString(4, idBarang);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menambahkan data.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        }
    }
}
