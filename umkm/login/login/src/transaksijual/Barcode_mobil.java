
package transaksijual;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

    
public class Barcode_mobil extends javax.swing.JFrame {

    public Barcode_mobil() {
        initComponents();
        loadTransaksiJual();
        loadBarangComboBox();
        loadMemberComboBox();
        this.setExtendedState(Barcode_mobil.MAXIMIZED_BOTH); 
        loadStatusComboBox();
        initializeComboBoxListeners();
    }
    
    private void loadDataByKode(String kode) {
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT * FROM transaksi_jual WHERE kode_penjualan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                kodepenjualan.setText(rs.getString("kode_penjualan"));
                
                Date tanggal = rs.getDate("tanggal_penjualan");
                if (tanggal != null) {
                    tanggalpenjualan.setDate(tanggal);
                }
                
                hargajasamobil.setText(String.valueOf(rs.getDouble("harga_jasa")));
                jenis_kendaraan.setText(rs.getString("jenis_kendaraan"));
                PLATNOMOR.setText(rs.getString("Plat_Nomor"));
                
                setSelectedComboBoxItem(STATUS, rs.getString("status"));
                setSelectedComboBoxItem(ID_BARANG1, rs.getString("Id_Barang"));
                setSelectedComboBoxItem(ID_MEMBER1, rs.getString("id_member"));
                
                // Load nama barang dan member setelah data dimuat
                if (rs.getString("Id_Barang") != null) {
                    loadNamaBarang(rs.getString("Id_Barang"));
                }
                if (rs.getString("id_member") != null) {
                    loadNamaMember(rs.getString("id_member"));
                }
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to set selected item in combobox
    private void setSelectedComboBoxItem(javax.swing.JComboBox<String> comboBox, String value) {
        if (value != null) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                if (comboBox.getItemAt(i).equals(value)) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    // Load items for ID_BARANG combobox
    private void loadBarangComboBox() {
        ID_BARANG1.removeAllItems();
        
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT Id_Barang FROM barang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            ID_BARANG1.addItem("");
            
            while (rs.next()) {
                ID_BARANG1.addItem(rs.getString("Id_Barang"));
                
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading barang data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ActionListener untuk ComboBox ID Barang
    private void setupBarangComboBoxListener() {
        ID_BARANG1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) ID_BARANG1.getSelectedItem();
                if (selectedId != null && !selectedId.isEmpty()) {
                    loadNamaBarang(selectedId);
                } else {
                    LB_BARANG.setText("");
                }
            }
        });
    }
    
    // ActionListener untuk ComboBox ID Member
    private void setupMemberComboBoxListener() {
        ID_MEMBER1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) ID_MEMBER1.getSelectedItem();
                if (selectedId != null && !selectedId.isEmpty()) {
                    loadNamaMember(selectedId);
                } else {
                    LB_MEMBER.setText("");
                }
            }
        });
    }
    
    // Method untuk load nama barang berdasarkan ID
    private void loadNamaBarang(String idBarang) {
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT Nama_Barang FROM barang WHERE Id_Barang = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idBarang);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String namaBarang = rs.getString("Nama_Barang");
                LB_BARANG.setText(namaBarang);
            } else {
                LB_BARANG.setText("Barang tidak ditemukan");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading barang data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            LB_BARANG.setText("Error");
        }
    }
    
    // Method untuk load nama member berdasarkan ID
    private void loadNamaMember(String idMember) {
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT Nama FROM member WHERE id_member = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idMember);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String namaMember = rs.getString("Nama");
                LB_MEMBER.setText(namaMember);
            } else {
                LB_MEMBER.setText("Member tidak ditemukan");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading member data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            LB_MEMBER.setText("Error");
        }
    }
    
    // Method untuk inisialisasi semua listener
    private void initializeComboBoxListeners() {
        setupBarangComboBoxListener();
        setupMemberComboBoxListener();
    }
    
    // Load items for ID_MEMBER combobox
    private void loadMemberComboBox() {
        ID_MEMBER1.removeAllItems();
        
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT id_member FROM member";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            // Add empty item for nullable field
            ID_MEMBER1.addItem("");
            
            while (rs.next()) {
                ID_MEMBER1.addItem(rs.getString("id_member"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading member data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Load items for STATUS combobox
    private void loadStatusComboBox() {
        STATUS.removeAllItems();
        STATUS.addItem("PENDING");
        STATUS.addItem("LUNAS");
        STATUS.addItem("GAGAL");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_trjual = new javax.swing.JTable();
        kodepenjualan = new javax.swing.JTextField();
        hargajasamobil = new javax.swing.JTextField();
        PLATNOMOR = new javax.swing.JTextField();
        jenis_kendaraan = new javax.swing.JTextField();
        tanggalpenjualan = new com.toedter.calendar.JDateChooser();
        ID_BARANG1 = new javax.swing.JComboBox<>();
        ID_MEMBER1 = new javax.swing.JComboBox<>();
        STATUS = new javax.swing.JComboBox<>();
        button_scanner = new javax.swing.JToggleButton();
        LB_BARANG = new javax.swing.JLabel();
        LB_MEMBER = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_cetak = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_kembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1728, 972));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabel_trjual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "kode penjualan", "tanggal penjualan", "harga jasa", "jenis kendaraan", "status", "plat nomor", "id barang", "id member"
            }
        ));
        tabel_trjual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_trjualMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabel_trjual);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 350, 790, 490));

        kodepenjualan.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        getContentPane().add(kodepenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 302, 270, 30));

        hargajasamobil.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        hargajasamobil.setText("30,000");
        getContentPane().add(hargajasamobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 280, 30));
        getContentPane().add(PLATNOMOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 612, 280, 30));

        jenis_kendaraan.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jenis_kendaraan.setText("MOBIL");
        getContentPane().add(jenis_kendaraan, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 280, 30));
        getContentPane().add(tanggalpenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 362, 270, 30));

        ID_BARANG1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ID_BARANG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_BARANG1ActionPerformed(evt);
            }
        });
        getContentPane().add(ID_BARANG1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 680, 140, 30));

        ID_MEMBER1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(ID_MEMBER1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 745, 140, 30));

        STATUS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        STATUS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                STATUSActionPerformed(evt);
            }
        });
        getContentPane().add(STATUS, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 555, 280, 30));

        button_scanner.setBackground(new java.awt.Color(153, 204, 255));
        button_scanner.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        button_scanner.setText("Scanner ");
        button_scanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_scannerActionPerformed(evt);
            }
        });
        getContentPane().add(button_scanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(1680, 880, 100, 30));

        LB_BARANG.setAlignmentX(0.5F);
        LB_BARANG.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, new java.awt.Color(255, 255, 255), java.awt.Color.white, new java.awt.Color(255, 255, 255)));
        getContentPane().add(LB_BARANG, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 680, 130, 30));

        LB_MEMBER.setAlignmentX(0.5F);
        LB_MEMBER.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        getContentPane().add(LB_MEMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 745, 130, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/transaksi jual owner.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        btn_simpan.setText("jButton1");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        getContentPane().add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 910, 200, 50));

        btn_cetak.setText("jButton2");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });
        getContentPane().add(btn_cetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 910, 210, 50));

        btn_hapus.setText("jButton3");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });
        getContentPane().add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(1420, 910, 210, 60));

        btn_kembali.setText("jButton1");
        btn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btn_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(1690, 30, 150, 90));

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void loadTransaksiJual() {
        DefaultTableModel model = (DefaultTableModel) tabel_trjual.getModel();
        model.setRowCount(0);
        
        try {
            Connection conn = koneksi.koneksiDB();
            String sql = "SELECT * FROM transaksi_jual WHERE jenis_kendaraan = 'MOBIL'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
           while (rs.next()) {
                Object[] row = {
                    rs.getString("kode_penjualan"),
                    rs.getDate("tanggal_penjualan"),
                    rs.getDouble("harga_jasa"),
                    rs.getString("jenis_kendaraan"),
                    rs.getString("status"),
                    rs.getString("Plat_Nomor"),
                    rs.getString("Id_Barang"),
                    rs.getString("id_member")
                };
                model.addRow(row);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
         String kode = kodepenjualan.getText();
        java.util.Date tanggal = tanggalpenjualan.getDate();
        String hargaText = hargajasamobil.getText().replace(",", "");
        double harga = 0;
        
        try {
            harga = Double.parseDouble(hargaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga jasa harus berupa angka!");
            return;
        }
        
        
        String jenisKendaraan = jenis_kendaraan.getText();
        String status = STATUS.getSelectedItem().toString();
        String platNomor = PLATNOMOR.getText();
        String idBarang = ID_BARANG1.getSelectedItem().toString();
        String idMember = ID_MEMBER1.getSelectedItem() == null || ID_MEMBER1.getSelectedItem().toString().isEmpty() ? 
                          null : ID_MEMBER1.getSelectedItem().toString();
        
        // Validasi input
        if (kode.isEmpty() || tanggal == null || platNomor.isEmpty() || idBarang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua field yang diperlukan!");
            return;
        }
        
        try {
            Connection conn = koneksi.koneksiDB();
            String sql;
            PreparedStatement pstmt;
            
            // Check if record already exists
            String checkSql = "SELECT COUNT(*) FROM transaksi_jual WHERE kode_penjualan = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, kode);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            if (count > 0) {
                // Update existing record
                sql = "UPDATE transaksi_jual SET tanggal_penjualan = ?, harga_jasa = ?, jenis_kendaraan = ?, " +
                      "status = ?, Plat_Nomor = ?, Id_Barang = ?, id_member = ? WHERE kode_penjualan = ?";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setDate(1, new java.sql.Date(tanggal.getTime()));
                pstmt.setDouble(2, harga);
                pstmt.setString(3, jenisKendaraan);
                pstmt.setString(4, status);
                pstmt.setString(5, platNomor);
                pstmt.setString(6, idBarang);
                
                if (idMember == null) {
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(7, idMember);
                }
                
                pstmt.setString(8, kode);
                
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            } else {
                // Insert new record
                sql = "INSERT INTO transaksi_jual (kode_penjualan, tanggal_penjualan, harga_jasa, " +
                      "jenis_kendaraan, status, Plat_Nomor, Id_Barang, id_member) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, kode);
                pstmt.setDate(2, new java.sql.Date(tanggal.getTime()));
                pstmt.setDouble(3, harga);
                pstmt.setString(4, jenisKendaraan);
                pstmt.setString(5, status);
                pstmt.setString(6, platNomor);
                pstmt.setString(7, idBarang);
                
                if (idMember == null) {
                    pstmt.setNull(8, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(8, idMember);
                }
                
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            }
            
            pstmt.close();
            conn.close();

            loadTransaksiJual();
          
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error menyimpan data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
           String kodePenjualan = kodepenjualan.getText();

        if (kodePenjualan.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data yang akan dicetak terlebih dahulu!");
            return;
        }

        int selectedRow = tabel_trjual.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Tidak ada baris yang dipilih.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report1.jasper");

            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, "File report tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn = koneksi.koneksiDB();

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("kode_penjualan", kodePenjualan);

            JasperPrint print = JasperFillManager.fillReport(reportStream, parameters, conn);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menampilkan laporan: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_cetakActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
       String kode = kodepenjualan.getText();
        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data dengan kode " + kode + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = koneksi.koneksiDB();
                String sql = "DELETE FROM transaksi_jual WHERE kode_penjualan = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, kode);
                
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    loadTransaksiJual();

                } else {
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
                }
                
                pstmt.close();
                conn.close();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error menghapus data: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void tabel_trjualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_trjualMouseClicked
        
                int row = tabel_trjual.getSelectedRow();
                if (row >= 0) {
                    String kode = tabel_trjual.getValueAt(row, 0).toString();
                    loadDataByKode(kode);
                }
            
    }//GEN-LAST:event_tabel_trjualMouseClicked

    private void btn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kembaliActionPerformed
        TR_Jual_Owner tr = new TR_Jual_Owner();
        tr.setVisible(true);
    }//GEN-LAST:event_btn_kembaliActionPerformed

    private void button_scannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_scannerActionPerformed
    JDialog scannerDialog = new JDialog(this, "Barcode Scanner", true);
    scannerDialog.setSize(400, 200);
    scannerDialog.setLocationRelativeTo(this);
    scannerDialog.setLayout(new BorderLayout());
    

    JPanel panel = new JPanel(new BorderLayout());
    JTextField barcodeField = new JTextField();
    JLabel instructions = new JLabel("Scan atau masukkan kode transaksi:");
    JButton processButton = new JButton("Proses");
    

    panel.add(instructions, BorderLayout.NORTH);
    panel.add(barcodeField, BorderLayout.CENTER);
    panel.add(processButton, BorderLayout.SOUTH);
    
    scannerDialog.add(panel);

    processButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String scannedCode = barcodeField.getText().trim();
            
            if (scannedCode.isEmpty()) {
                JOptionPane.showMessageDialog(scannerDialog, 
                    "Masukkan kode transaksi yang valid!", 
                    "Error Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Connection conn = koneksi.koneksiDB();
                
                // First check if the transaction exists and is in PENDING status
                String checkSql = "SELECT status, tanggal_penjualan, Plat_Nomor FROM transaksi_jual WHERE kode_penjualan = ? AND jenis_kendaraan = 'MOBIL'";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, scannedCode);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String currentStatus = rs.getString("status");
                    String platNomor = rs.getString("Plat_Nomor");
                    Date tanggalPenjualan = rs.getDate("tanggal_penjualan");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = dateFormat.format(tanggalPenjualan);
                    
                    if ("PENDING".equals(currentStatus)) {
                        // Show confirmation dialog with transaction details
                        String confirmMessage = "Detail Transaksi:\n" +
                                              "Kode: " + scannedCode + "\n" +
                                              "Tanggal: " + formattedDate + "\n" +
                                              "Plat Nomor: " + platNomor + "\n\n" +
                                              "Apakah Anda ingin mengubah status dari PENDING menjadi LUNAS?";
                        
                        int confirmation = JOptionPane.showConfirmDialog(
                            scannerDialog,
                            confirmMessage,
                            "Konfirmasi Perubahan Status",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (confirmation == JOptionPane.YES_OPTION) {
                   
                            String updateSql = "UPDATE transaksi_jual SET status = 'LUNAS' WHERE kode_penjualan = ?";
                            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                            updateStmt.setString(1, scannedCode);
                            
                            int rowsUpdated = updateStmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(scannerDialog, 
                                    "Status transaksi " + scannedCode + " berhasil diubah menjadi LUNAS!", 
                                    "Sukses", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                    
                
                                loadTransaksiJual();
                                scannerDialog.dispose();
                      
                                loadDataByKode(scannedCode);
                            }
                            
                            updateStmt.close();
                        } else {
                    
                            JOptionPane.showMessageDialog(scannerDialog, 
                                "Perubahan status dibatalkan", 
                                "Dibatalkan", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if ("LUNAS".equals(currentStatus)) {
                        JOptionPane.showMessageDialog(scannerDialog, 
                            "Transaksi " + scannedCode + " sudah berstatus LUNAS!", 
                            "Informasi", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(scannerDialog, 
                            "Transaksi " + scannedCode + " memiliki status: " + currentStatus + " dan tidak dapat diubah.", 
                            "Status Tidak Valid", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(scannerDialog, 
                        "Kode transaksi tidak ditemukan!", 
                        "Tidak Ditemukan", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                rs.close();
                checkStmt.close();
                conn.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(scannerDialog, 
                    "Error memproses kode: " + ex.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    barcodeField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                processButton.doClick();
            }
        }
    });
    
    // Show dialog
    scannerDialog.setVisible(true);
    }//GEN-LAST:event_button_scannerActionPerformed

    private void ID_BARANG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_BARANG1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_BARANG1ActionPerformed

    private void STATUSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_STATUSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_STATUSActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Barcode_mobil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Barcode_mobil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Barcode_mobil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Barcode_mobil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Barcode_mobil().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ID_BARANG1;
    private javax.swing.JComboBox<String> ID_MEMBER1;
    private javax.swing.JLabel LB_BARANG;
    private javax.swing.JLabel LB_MEMBER;
    private javax.swing.JTextField PLATNOMOR;
    private javax.swing.JComboBox<String> STATUS;
    private javax.swing.JButton btn_cetak;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_kembali;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JToggleButton button_scanner;
    private javax.swing.JTextField hargajasamobil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jenis_kendaraan;
    private javax.swing.JTextField kodepenjualan;
    private javax.swing.JTable tabel_trjual;
    private com.toedter.calendar.JDateChooser tanggalpenjualan;
    // End of variables declaration//GEN-END:variables
}
