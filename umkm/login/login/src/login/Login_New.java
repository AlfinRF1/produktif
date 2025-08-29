package login;
import halamanutama.Laporan_Owner;
import halamanutama1.Dashboard_Karyawan;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.*;
import koneksi.koneksi;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import koneksi.koneksi;




public class Login_New extends javax.swing.JFrame {
    private StringBuilder rfidInput = new StringBuilder();
    private boolean rfidMode = false;
    private Timer rfidResetTimer;
    private JLabel rfidStatusLabel;

    public Login_New() {
        initComponents();
         this.setExtendedState(Login_New.MAXIMIZED_BOTH);
        setupRfidListener();
        setupRfidResetTimer();
        tg_rfid.setText("Aktifkan RFID");
        rfidStatusLabel = new JLabel("Mode login: Username & Password");
        rfidStatusLabel.setForeground(new java.awt.Color(128, 128, 128));
        getContentPane().add(rfidStatusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 190, 400, 30));
    }
    
  private void setupRfidListener() {
    KeyAdapter rfidKeyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (rfidMode) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && rfidInput.length() > 0) {
                    processRfidLogin(rfidInput.toString());
                    rfidInput.setLength(0);
                    e.consume();
                }
            }
        }     
        @Override
        public void keyTyped(KeyEvent e) {
            if (rfidMode) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    rfidInput.append(c);
                    updateRfidStatus("Membaca RFID: " + "*".repeat(rfidInput.length()));
                    if (rfidResetTimer.isRunning()) {
                        rfidResetTimer.restart();
                    } else {
                        rfidResetTimer.start();
                    }

                    if (rfidInput.length() >= 10) {
                        SwingUtilities.invokeLater(() -> {
                            processRfidLogin(rfidInput.toString());
                            rfidInput.setLength(0);
                        });
                    }
                    
                    e.consume();
                }
            }
        }
    };
    this.addKeyListener(rfidKeyAdapter);
    this.setFocusable(true);
    this.requestFocus();
}
    private void setupRfidResetTimer() {
        rfidResetTimer = new Timer(1000, e -> {
            rfidInput.setLength(0);
            if (rfidMode) {
                updateRfidStatus("Menunggu RFID Card...");
            }
        });
        rfidResetTimer.setRepeats(false);
    }

    private void handlePossibleRfidInput(char c) {
        if (rfidResetTimer.isRunning()) {
            rfidResetTimer.restart();
        } else {
            rfidResetTimer.start();
        }
        if (Character.isDigit(c)) {
            rfidInput.append(c);
            updateRfidStatus("Membaca RFID: " + "*".repeat(rfidInput.length()));
            if (rfidInput.length() >= 10) {
                SwingUtilities.invokeLater(() -> processRfidLogin(rfidInput.toString()));
                rfidInput.setLength(0);
            }
        } else {
            rfidInput.setLength(0);
            updateRfidStatus("Menunggu RFID Card...");
        }
    }
    
    private void updateRfidStatus(String status) {
        if (rfidStatusLabel != null) {
            rfidStatusLabel.setText(status);
        }
    }
    
  private void toggleRfidMode() {
    rfidMode = !rfidMode;
    
    if (rfidMode) {
        try {
            Connection con = koneksi.koneksiDB();
            String countSql = "SELECT COUNT(*) FROM akun WHERE rfid_number IS NOT NULL AND rfid_number != ''";
            PreparedStatement countPst = con.prepareStatement(countSql);
            ResultSet countRs = countPst.executeQuery();
            
            if (countRs.next() && countRs.getInt(1) > 0) {
                tg_rfid.setText("Nonaktifkan RFID");
                updateRfidStatus("Mode RFID aktif: Silahkan tempelkan kartu RFID");

                jtxtusername.setEnabled(false);
                password.setEnabled(false);
                jtxtlogin.setEnabled(false);
 
                jtxtusername.setText("");
                password.setText("");

                this.requestFocus();

                rfidInput.setLength(0);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Tidak ada kartu RFID terdaftar di database!", 
                    "RFID Error", JOptionPane.WARNING_MESSAGE);
                rfidMode = false;
            }
            
            countRs.close();
            countPst.close();
            con.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            rfidMode = false;
        }
    } else {
        tg_rfid.setText("Aktifkan RFID");
        updateRfidStatus("Mode login: Username & Password");

        jtxtusername.setEnabled(true);
        password.setEnabled(true);
        jtxtlogin.setEnabled(true);
    }
}

private void processRfidLogin(String rfidNumber) {
    try {
        Connection con = koneksi.koneksiDB();

        if (rfidNumber.length() < 8) {
            updateRfidStatus("RFID tidak valid! Terlalu pendek.");
            return;
        }
        
        String sql = "SELECT username, posisi FROM akun WHERE rfid_number = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, rfidNumber);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String username = rs.getString("username");
            String posisi = rs.getString("posisi");

            updateRfidStatus("RFID terdeteksi: Login sebagai " + username);

            System.out.println("RFID detected: " + rfidNumber + " for user: " + username);

            Timer delayTimer = new Timer(800, event -> {
                JOptionPane.showMessageDialog(this, 
                    "Login berhasil sebagai " + posisi + "!",
                    "Login Sukses", JOptionPane.INFORMATION_MESSAGE);

                if ("owner".equals(posisi)) {
                    new Laporan_Owner().setVisible(true);
                } else if ("karyawan".equals(posisi)) {
                    new Dashboard_Karyawan().setVisible(true);
                }        
                this.dispose();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
            
        } else {
            updateRfidStatus("RFID tidak terdaftar! Silahkan coba lagi.");
            JOptionPane.showMessageDialog(this, 
                "RFID tidak terdaftar di database!", 
                "Login Gagal", JOptionPane.ERROR_MESSAGE);

            System.out.println("Unrecognized RFID: " + rfidNumber);
        }
        
        rs.close();
        pst.close();
        con.close();
        
    } catch (SQLException ex) {
        updateRfidStatus("Error database!");
        JOptionPane.showMessageDialog(this, 
            "Database error: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxtusername = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        btnshow = new javax.swing.JButton();
        btnhide = new javax.swing.JButton();
        jtxtlogin = new javax.swing.JButton();
        tg_rfid = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtxtusername.setBackground(new java.awt.Color (0, 0, 0, 0));
        jtxtusername.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jtxtusername.setBorder(null);
        jtxtusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtusernameActionPerformed(evt);
            }
        });
        getContentPane().add(jtxtusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 460, 430, 50));

        password.setBackground(new java.awt.Color (0, 0, 0, 0));
        password.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        password.setBorder(null);
        getContentPane().add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 580, 370, 50));

        btnshow.setBackground(new java.awt.Color (0, 0, 0, 0));
        btnshow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Eye Show.png"))); // NOI18N
        btnshow.setBorder(null);
        btnshow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnshowMouseClicked(evt);
            }
        });
        btnshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnshowActionPerformed(evt);
            }
        });
        getContentPane().add(btnshow, new org.netbeans.lib.awtextra.AbsoluteConstraints(1590, 580, 50, 50));

        btnhide.setBackground(new java.awt.Color (0, 0, 0, 0));
        btnhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Hide.png"))); // NOI18N
        btnhide.setBorder(null);
        btnhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnhideMouseClicked(evt);
            }
        });
        btnhide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhideActionPerformed(evt);
            }
        });
        getContentPane().add(btnhide, new org.netbeans.lib.awtextra.AbsoluteConstraints(1590, 580, 50, 50));

        jtxtlogin.setBackground(new java.awt.Color (0, 0, 0, 0));
        jtxtlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtloginActionPerformed(evt);
            }
        });
        getContentPane().add(jtxtlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(1340, 650, 160, 50));

        tg_rfid.setBackground(new java.awt.Color(153, 204, 255));
        tg_rfid.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tg_rfid.setForeground(new java.awt.Color(255, 255, 255));
        tg_rfid.setText("RFID");
        tg_rfid.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tg_rfid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tg_rfidActionPerformed(evt);
            }
        });
        getContentPane().add(tg_rfid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1520, 660, 100, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/login fix (1).png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void btnshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnshowActionPerformed
 
    }//GEN-LAST:event_btnshowActionPerformed

    private void btnhideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhideActionPerformed
       
    }//GEN-LAST:event_btnhideActionPerformed

    private void jtxtloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtloginActionPerformed
    login();
    }

    private void autoFillPassword() {
        String usernameText = jtxtusername.getText().trim();

        if (usernameText.isEmpty()) {
            password.setText("");
            return;
        }

        try {
            Connection con = koneksi.koneksiDB();
            String sql = "SELECT password FROM akun WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, usernameText);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
                    password.setText("*");
                } else {
                    password.setText(storedPassword);
                }
            } else {
                password.setText(""); 
            }
            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void login() {
        String usernameText = jtxtusername.getText().trim();
        String passwordText = new String(password.getPassword());

        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.koneksiDB();
            String sql = "SELECT * FROM akun WHERE username = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, usernameText);
            pst.setString(2, passwordText);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String posisi = rs.getString("posisi");
                JOptionPane.showMessageDialog(this, "Login berhasil. Selamat datang, " + posisi + "!");

                if ("owner".equals(posisi)) {
                    new Laporan_Owner().setVisible(true);
                } else if ("karyawan".equals(posisi)) {
                    new Dashboard_Karyawan().setVisible(true);
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
                     
        
    
    
    }//GEN-LAST:event_jtxtloginActionPerformed

    private void jtxtusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtusernameActionPerformed

    private void btnshowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnshowMouseClicked
        btnshow.setVisible(false);
        btnhide.setVisible(true);
        password.setEchoChar((char)0);
    }//GEN-LAST:event_btnshowMouseClicked

    private void btnhideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnhideMouseClicked
        btnshow.setVisible(true);
        btnhide.setVisible(false);
        password.setEchoChar('*');
    }//GEN-LAST:event_btnhideMouseClicked

    private void tg_rfidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tg_rfidActionPerformed
toggleRfidMode();
    if (rfidMode) {
        this.requestFocus();
    }
 
    }//GEN-LAST:event_tg_rfidActionPerformed
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
            java.util.logging.Logger.getLogger(Login_New.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_New.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_New.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_New.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login_New().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnhide;
    private javax.swing.JButton btnshow;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton jtxtlogin;
    private javax.swing.JTextField jtxtusername;
    private javax.swing.JPasswordField password;
    private javax.swing.JToggleButton tg_rfid;
    // End of variables declaration//GEN-END:variables
}
