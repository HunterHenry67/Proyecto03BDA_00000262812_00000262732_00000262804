/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.ArtistaDTO;
import Excepciones.NegocioException;
import Interfaces.IArtistaBO;
import Interfaces.IPersonaBO;
import Negocio.ArtistaBO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
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
    private List<ArtistaDTO> artistasMostrados = new ArrayList<>();
    private int paginaActual = 0;
    private static final int LIMITE_ARTISTAS = 6;

    public frmMenuArtista() {
    initComponents();
    this.artistaBO = new ArtistaBO();
    configurarPantallaArtistas();
    setLocationRelativeTo(null);
    cargarPrimerosArtistas();
}

    
    private void configurarPantallaArtistas() {
        pnlListaArtistas.setLayout(new GridLayout(2, 3, 25, 25));
        pnlListaArtistas.setBackground(new Color(220, 220, 220));
        txtFieldBuscar.addActionListener(e -> buscarArtista());
    }

    private void cargarPrimerosArtistas() {
        try {
            artistasMostrados = artistaBO.consultarTodos();
            paginaActual = 0;
            cargarPaginaArtistas();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar artistas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPaginaArtistas() {
        pnlListaArtistas.removeAll();
        if (artistasMostrados == null || artistasMostrados.isEmpty()) {
            pnlListaArtistas.add(crearMensajeSinResultados());
            while (pnlListaArtistas.getComponentCount() < LIMITE_ARTISTAS) {
                pnlListaArtistas.add(crearEspacioVacio());
            }
            actualizarControlesPaginacion();
            pnlListaArtistas.revalidate();
            pnlListaArtistas.repaint();
            return;
        }
        int inicio = paginaActual * LIMITE_ARTISTAS;
        int fin = Math.min(inicio + LIMITE_ARTISTAS, artistasMostrados.size());

        for (int i = inicio; i < fin; i++) {
            ArtistaDTO artista = artistasMostrados.get(i);
            pnlListaArtistas.add(crearTarjetaArtista(artista));
        }

        while (pnlListaArtistas.getComponentCount() < LIMITE_ARTISTAS) {
            pnlListaArtistas.add(crearEspacioVacio());
        }
        actualizarControlesPaginacion();
        pnlListaArtistas.revalidate();
        pnlListaArtistas.repaint();
    }

    private void actualizarControlesPaginacion() {
        int totalPaginas = obtenerTotalPaginas();
        if (totalPaginas == 0) {
            lblPaginaArtistas.setText("Página 0 de 0");
            btnAnteriorArtistas.setEnabled(false);
            btnSiguienteArtistas.setEnabled(false);
            return;
        }
        lblPaginaArtistas.setText("Página " + (paginaActual + 1) + " de " + totalPaginas);
        btnAnteriorArtistas.setEnabled(paginaActual > 0);
        btnSiguienteArtistas.setEnabled(paginaActual < totalPaginas - 1);
    }

    private int obtenerTotalPaginas() {
        if (artistasMostrados == null || artistasMostrados.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil(artistasMostrados.size() / (double) LIMITE_ARTISTAS);
    }

    private void buscarArtista() {
        String texto = txtFieldBuscar.getText();
        if (texto == null || texto.trim().isEmpty()) {
            cargarPrimerosArtistas();
            return;
        }
        try {
            artistasMostrados = artistaBO.buscarPorNombre(texto.trim());
            paginaActual = 0;
            cargarPaginaArtistas();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al buscar artista", JOptionPane.ERROR_MESSAGE);
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
        MouseAdapter eventoClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                abrirDetalleArtista(artista);
            }
        };
        tarjeta.addMouseListener(eventoClick);
        lblImagen.addMouseListener(eventoClick);
        lblNombre.addMouseListener(eventoClick);
        lblTipo.addMouseListener(eventoClick);
        panelTexto.addMouseListener(eventoClick);
        lblImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblNombre.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblTipo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTexto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return tarjeta;
    }

    private void cargarImagen(JLabel label, String rutaImagen) {
        try {
            ImageIcon icono = null;
            if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
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
            Image imagen = icono.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagen));
        } catch (Exception ex) {
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

    private void abrirDetalleArtista(ArtistaDTO artista) {
        frmDetalleArtista pantallaDetalleArtista = new frmDetalleArtista(artista);
        pantallaDetalleArtista.setVisible(true);
        this.dispose();
    }

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
        pnlListaArtistas = new javax.swing.JPanel();
        btnAnteriorArtistas = new javax.swing.JButton();
        btnSiguienteArtistas = new javax.swing.JButton();
        lblPaginaArtistas = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú Artistas");

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

        btnBuscar.setBackground(new java.awt.Color(51, 102, 255));
        btnBuscar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Artistas");

        javax.swing.GroupLayout pnlListaArtistasLayout = new javax.swing.GroupLayout(pnlListaArtistas);
        pnlListaArtistas.setLayout(pnlListaArtistasLayout);
        pnlListaArtistasLayout.setHorizontalGroup(
            pnlListaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlListaArtistasLayout.setVerticalGroup(
            pnlListaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );

        btnAnteriorArtistas.setBackground(new java.awt.Color(0, 204, 255));
        btnAnteriorArtistas.setForeground(new java.awt.Color(0, 0, 0));
        btnAnteriorArtistas.setText("Anterior");
        btnAnteriorArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorArtistasActionPerformed(evt);
            }
        });

        btnSiguienteArtistas.setBackground(new java.awt.Color(0, 204, 255));
        btnSiguienteArtistas.setForeground(new java.awt.Color(0, 0, 0));
        btnSiguienteArtistas.setText("Siguiente");
        btnSiguienteArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteArtistasActionPerformed(evt);
            }
        });

        lblPaginaArtistas.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(298, 298, 298))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlListaArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBuscar)
                                .addGap(18, 18, 18)
                                .addComponent(txtFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 73, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btnAnteriorArtistas)
                        .addGap(118, 118, 118)
                        .addComponent(lblPaginaArtistas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSiguienteArtistas)
                        .addGap(139, 139, 139))))
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
                .addGap(31, 31, 31)
                .addComponent(pnlListaArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnteriorArtistas)
                    .addComponent(btnSiguienteArtistas)
                    .addComponent(lblPaginaArtistas))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        frmMenuPrinicipal pantallaMenuPrincipal = new frmMenuPrinicipal();
        pantallaMenuPrincipal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarArtista();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtistasActionPerformed
        frmMenuArtista pantallaArtista = new frmMenuArtista();
        pantallaArtista.setVisible(true);
        this.dispose();
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

    private void btnAnteriorArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorArtistasActionPerformed
        if (paginaActual > 0) {
            paginaActual--;
            cargarPaginaArtistas();
        }
    }//GEN-LAST:event_btnAnteriorArtistasActionPerformed

    private void btnSiguienteArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteArtistasActionPerformed
        int totalPaginas = obtenerTotalPaginas();
        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            cargarPaginaArtistas();
        }
    }//GEN-LAST:event_btnSiguienteArtistasActionPerformed

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
    private javax.swing.JButton btnAnteriorArtistas;
    private javax.swing.JButton btnArtistas;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFavoritos;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnSiguienteArtistas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblPaginaArtistas;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel pnlListaArtistas;
    private javax.swing.JTextField txtFieldBuscar;
    // End of variables declaration//GEN-END:variables
}
