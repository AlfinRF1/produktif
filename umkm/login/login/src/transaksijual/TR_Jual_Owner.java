/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package transaksijual;
import barang.Barang_Owner;
import transaksijual.TR_Jual_Owner;
import transaksibeli.TR_Beli_Owner;
import Member_Owner.Member;
import login.Login_New;
import halamanutama.Laporan_Owner;
import javax.swing.JOptionPane;
import transaksijual.Barcode_mobil;
import transaksijual.Barcode_motor;


public class TR_Jual_Owner extends javax.swing.JFrame {


    public TR_Jual_Owner() {
        initComponents();
        this.setExtendedState(TR_Jual_Owner.MAXIMIZED_BOTH); 
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        btnlogout = new javax.swing.JButton();
        btnmobil = new javax.swing.JButton();
        btnmotor = new javax.swing.JButton();
        btndashboard = new javax.swing.JButton();
        btnbarang = new javax.swing.JButton();
        btntrbrli = new javax.swing.JButton();
        btnmember = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/kendaraan.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        btnlogout.setText("jButton7");
        btnlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogoutActionPerformed(evt);
            }
        });
        getContentPane().add(btnlogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1740, 20, 130, 80));

        btnmobil.setText("jButton5");
        btnmobil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmobilActionPerformed(evt);
            }
        });
        getContentPane().add(btnmobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 400, 610, 500));

        btnmotor.setText("jButton6");
        btnmotor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmotorActionPerformed(evt);
            }
        });
        getContentPane().add(btnmotor, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 420, 610, 460));

        btndashboard.setText("jButton1");
        btndashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndashboardActionPerformed(evt);
            }
        });
        getContentPane().add(btndashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 160, 170, 100));

        btnbarang.setText("jButton2");
        btnbarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbarangActionPerformed(evt);
            }
        });
        getContentPane().add(btnbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 300, 170, 110));

        btntrbrli.setText("jButton3");
        btntrbrli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntrbrliActionPerformed(evt);
            }
        });
        getContentPane().add(btntrbrli, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 553, 170, 110));

        btnmember.setText("jButton4");
        btnmember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmemberActionPerformed(evt);
            }
        });
        getContentPane().add(btnmember, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 690, 170, 100));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
  int pilihan = JOptionPane.showConfirmDialog(this, 
            "Apakah anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
            
    if (pilihan == JOptionPane.YES_OPTION) {
        this.dispose();
        
        Login_New loginFrame = new Login_New();
        loginFrame.setVisible(true);
    }
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnmobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmobilActionPerformed
        Barcode_mobil bm = new Barcode_mobil();
                bm.setVisible(true);
    }//GEN-LAST:event_btnmobilActionPerformed

    private void btnmotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmotorActionPerformed
         Barcode_motor br = new Barcode_motor();
                br.setVisible(true);
    }//GEN-LAST:event_btnmotorActionPerformed

    private void btndashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndashboardActionPerformed
         Laporan_Owner d = new Laporan_Owner();
        d.setVisible(true);
    }//GEN-LAST:event_btndashboardActionPerformed

    private void btnbarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbarangActionPerformed
      Barang_Owner b = new Barang_Owner();
        b.setVisible(true);
    }//GEN-LAST:event_btnbarangActionPerformed

    private void btntrbrliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntrbrliActionPerformed
        TR_Beli_Owner tb = new TR_Beli_Owner();
        tb.setVisible(true);
    }//GEN-LAST:event_btntrbrliActionPerformed

    private void btnmemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmemberActionPerformed
       Member m = new Member();
        m.setVisible(true);
    }//GEN-LAST:event_btnmemberActionPerformed

  
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TR_Jual_Owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TR_Jual_Owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TR_Jual_Owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TR_Jual_Owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(() -> {
            new TR_Jual_Owner().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbarang;
    private javax.swing.JButton btndashboard;
    private javax.swing.JButton btnlogout;
    private javax.swing.JButton btnmember;
    private javax.swing.JButton btnmobil;
    private javax.swing.JButton btnmotor;
    private javax.swing.JButton btntrbrli;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
