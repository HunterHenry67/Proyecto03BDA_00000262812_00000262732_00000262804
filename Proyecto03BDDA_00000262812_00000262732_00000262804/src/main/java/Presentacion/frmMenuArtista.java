/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.ArtistaDTO;
import Excepciones.NegocioException;
import Interfaces.IArtistaBO;
import Interfaces.IArtistaDAO;
import Interfaces.IConexionBD;
import Negocio.ArtistaBO;
import Persistencia.ArtistaDAO;
import Persistencia.ConexionBD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author BALAMRUSH
 */
public class frmMenuArtista extends javax.swing.JFrame {

    private IArtistaBO artistaBO;
    private static final int LIMITE_ARTISTAS = 6;
    
    public frmMenuArtista() {
        initComponents();
        inicializarBOs();
        configurarPantallaArtistas();
        setLocationRelativeTo(null);
        cargarPrimerosArtistas();
    }

    private void inicializarBOs() {
        IConexionBD conexionBD = new ConexionBD();
        IArtistaDAO artistaDAO = new ArtistaDAO(conexionBD);
        this.artistaBO = new ArtistaBO(artistaDAO);
    }

    private void configurarPantallaArtistas() {
        pnlListaArtistas.setLayout(new GridLayout(2, 3, 25, 25));
        pnlListaArtistas.setBackground(new Color(220, 220, 220));

        btnBuscar.addActionListener(e -> buscarArtista());
        txtBusqueda.addActionListener(e -> buscarArtista());
    }

    private void cargarPrimerosArtistas() {
        try {
            pnlListaArtistas.removeAll();
            List<ArtistaDTO> artistas = artistaBO.consultarTodos();
            int limite = Math.min(LIMITE_ARTISTAS, artistas.size());
            for (int i = 0; i < limite; i++) {
                ArtistaDTO artista = artistas.get(i);
                pnlListaArtistas.add(crearTarjetaArtista(artista));
            }
            while (pnlListaArtistas.getComponentCount() < LIMITE_ARTISTAS) {
                pnlListaArtistas.add(crearEspacioVacio());
            }
            pnlListaArtistas.revalidate();
            pnlListaArtistas.repaint();
        } catch(NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al cargar artistas",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarArtista() {
        String texto = txtBusqueda.getText();
        if (texto == null || texto.trim().isEmpty()) {
            cargarPrimerosArtistas();
            return;
        }
        try {
            pnlListaArtistas.removeAll();
            List<ArtistaDTO> artistas = artistaBO.buscarPorNombre(texto.trim());
            int limite = Math.min(LIMITE_ARTISTAS, artistas.size());
            for (int i = 0; i < limite; i++) {
                ArtistaDTO artista = artistas.get(i);
                pnlListaArtistas.add(crearTarjetaArtista(artista));
            }
            if (artistas.isEmpty()) {
                pnlListaArtistas.add(crearMensajeSinResultados());
            }
            while (pnlListaArtistas.getComponentCount() < LIMITE_ARTISTAS) {
                pnlListaArtistas.add(crearEspacioVacio());
            }
            pnlListaArtistas.revalidate();
            pnlListaArtistas.repaint();
        } catch(NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al buscar artista",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JPanel crearTarjetaArtista(ArtistaDTO artista) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.BLACK);
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(150, 120));
        cargarImagen(lblImagen, artista.getImagen());

        JLabel lblNombre = new JLabel(artista.getNombre());
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblTipo = new JLabel(obtenerTipoArtista(artista));
        lblTipo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTipo.setForeground(Color.LIGHT_GRAY);
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setBackground(Color.BLACK);
        panelTexto.add(lblNombre);
        panelTexto.add(lblTipo);

        tarjeta.add(lblImagen, BorderLayout.CENTER);
        tarjeta.add(panelTexto, BorderLayout.SOUTH);

        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirDetalleArtista(artista);
            }
        });

        return tarjeta;
    }

    private void cargarImagen(JLabel label, String rutaImagen) {
        try {
            ImageIcon icono = null;
            if(rutaImagen != null && !rutaImagen.trim().isEmpty()) {
                URL url = getClass().getClassLoader().getResource(rutaImagen);
                if (url != null) {
                    icono = new ImageIcon(url);
                } else {
                    icono = new ImageIcon(rutaImagen);
                }
            }
            if (icono == null || icono.getIconWidth() <= 0) {
                label.setText("Sin imagen");
                label.setForeground(Color.WHITE);
                return;
            }
            Image imagen = icono.getImage().getScaledInstance(110,110,Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagen));
        } catch(Exception ex) {
            label.setText("Sin imagen");
            label.setForeground(Color.WHITE);
        }
    }

    private String obtenerTipoArtista(ArtistaDTO artista) {
        if (artista.getIntegrantes() == null) {
            return "Artista";
        }
        if (artista.getIntegrantes().size() == 1) {
            return "Solista";
        }
        if (artista.getIntegrantes().size() > 1) {
            return "Banda";
        }
        return "Artista";
    }

    private JPanel crearEspacioVacio() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(220, 220, 220));
        return panel;
    }

    private JLabel crearMensajeSinResultados() {
        JLabel label = new JLabel("No se encontraron artistas.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBotones = new javax.swing.JPanel();
        btnMenuPrincipal = new javax.swing.JButton();
        btnArtistas = new javax.swing.JButton();
        btnAlbumes = new javax.swing.JButton();
        btnFavoritos = new javax.swing.JButton();
        btnPerfil = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtFieldBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBotones.setBackground(new java.awt.Color(0, 0, 0));

        btnMenuPrincipal.setBackground(new java.awt.Color(0, 0, 0));
        btnMenuPrincipal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnMenuPrincipal.setForeground(new java.awt.Color(255, 255, 255));
        btnMenuPrincipal.setText("Menú Principal");
        btnMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuPrincipalActionPerformed(evt);
            }
        });

        btnArtistas.setBackground(new java.awt.Color(0, 0, 0));
        btnArtistas.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnArtistas.setForeground(new java.awt.Color(255, 255, 255));
        btnArtistas.setText("Ártistas");
        btnArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtistasActionPerformed(evt);
            }
        });

        btnAlbumes.setBackground(new java.awt.Color(0, 0, 0));
        btnAlbumes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnAlbumes.setForeground(new java.awt.Color(255, 255, 255));
        btnAlbumes.setText("Álbumes");
        btnAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlbumesActionPerformed(evt);
            }
        });

        btnFavoritos.setBackground(new java.awt.Color(0, 0, 0));
        btnFavoritos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnFavoritos.setForeground(new java.awt.Color(255, 255, 255));
        btnFavoritos.setText("Favoritos");
        btnFavoritos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFavoritosActionPerformed(evt);
            }
        });

        btnPerfil.setBackground(new java.awt.Color(0, 0, 0));
        btnPerfil.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnPerfil.setForeground(new java.awt.Color(255, 255, 255));
        btnPerfil.setText("Perfil");
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFavoritos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlbumes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAlbumes, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFavoritos, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        btnBuscar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Artistas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btnBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(txtFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 73, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(298, 298, 298))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFieldBuscar)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtistasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArtistasActionPerformed

    private void btnAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlbumesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlbumesActionPerformed

    private void btnFavoritosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFavoritosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFavoritosActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPerfilActionPerformed

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
            java.util.logging.Logger.getLogger(frmMenuArtista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenuArtista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenuArtista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenuArtista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenuArtista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlbumes;
    private javax.swing.JButton btnArtistas;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFavoritos;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JTextField txtFieldBuscar;
    // End of variables declaration//GEN-END:variables
}
