/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.AlbumDTO;
import DTO.CancionDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Interfaces.IFavoritoBO;
import Negocio.FavoritoBO;
import Utilerias.Sesion;
import java.awt.Image;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import DTO.GeneroDTO;
import Excepciones.PersistenciaException;
import Interfaces.IGeneroBO;
import Negocio.GeneroBO;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public class frmDetalleAlbum extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmDetalleAlbum.class.getName());
    private AlbumDTO album;
    private IFavoritoBO favoritoBO;
    private String nombreArtista;
    private String imagenArtista;
    private final IGeneroBO generoBO = new GeneroBO();

    /**
     * Creates new form frmDetalleAlbum
     */
    public frmDetalleAlbum() {
        initComponents();
        favoritoBO = new FavoritoBO();
        configurarNavegacion();
        cargarPerfilLateral();
        setLocationRelativeTo(null);
    }

    public frmDetalleAlbum(AlbumDTO album, String nombreArtista, String imagenArtista) {
        initComponents();

        this.album = album;
        this.nombreArtista = nombreArtista;
        this.favoritoBO = new FavoritoBO();
        this.imagenArtista = imagenArtista;
        configurarNavegacion();
        cargarPerfilLateral();
        setLocationRelativeTo(null);
        mostrarInformacionAlbum();
        configurarClickFavoritos();
    }

    private void mostrarInformacionAlbum() {
        if (album == null) {
            return;
        }

        lblNombreArtista.setText(nombreArtista);
        lblNombreALbum.setText(album.getNombre());
        lblFechaAlbum.setText(album.getFechaLanzamiento());
        lblGeneroAlbum.setText("Género: " + obtenerNombreGenero(album.getIdGenero()));

        cargarImagen(lblFotoAlbum, album.getImagenPortada(), 165, 133);
        cargarImagen(lblFotoPerfilArtista, imagenArtista, 37, 30);
        llenarTablaCanciones();
    }

    private void llenarTablaCanciones() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"#", "Nombre", "Duración", "Favorito"}, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        if (album != null && album.getCanciones() != null) {
            int numero = 1;

            for (CancionDTO cancion : album.getCanciones()) {
                Object[] fila = {numero, cancion.getNombre(), cancion.getDuracion(), "☐"};

                modelo.addRow(fila);
                numero++;
            }
        }

        tablaInfoCanciones.setModel(modelo);
        tablaInfoCanciones.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaInfoCanciones.getColumnModel().getColumn(3).setPreferredWidth(60);
    }

    private void configurarClickFavoritos() {
        tablaInfoCanciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                int fila = tablaInfoCanciones.rowAtPoint(evento.getPoint());
                int columna = tablaInfoCanciones.columnAtPoint(evento.getPoint());
                if (fila >= 0 && columna == 3) {
                    agregarCancionFavorita(fila);
                }
            }
        });
    }

    private void agregarCancionFavorita(int fila) {
        UsuarioDTO usuario = Sesion.getUsuarioActual();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe un usuario con sesión iniciada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (album == null || album.getCanciones() == null) {
            return;
        }
        if (fila < 0 || fila >= album.getCanciones().size()) {
            return;
        }

        CancionDTO cancion = album.getCanciones().get(fila);

        if (cancion == null || cancion.getIdCancion() == null) {
            JOptionPane.showMessageDialog(this, "La canción no tiene un identificador válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean agregado = favoritoBO.agregarCancionFavorita(usuario.getId(), cancion.getIdCancion());
            if (agregado) {
                tablaInfoCanciones.setValueAt("☑", fila, 3);
                JOptionPane.showMessageDialog(this, "Canción agregada a favoritos: " + cancion.getNombre());
            } else {
                tablaInfoCanciones.setValueAt("☑", fila, 3);
                JOptionPane.showMessageDialog(this, "La canción ya estaba en favoritos.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al agregar favorito", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerNombreGenero(String idGenero) {

        if (idGenero == null || idGenero.isBlank()) {
            return "Sin género";
        }

        if (!ObjectId.isValid(idGenero)) {
            return "Sin género";
        }

        try {
            ObjectId id = new ObjectId(idGenero);

            GeneroDTO genero = generoBO.consultarPorId(id);

            if (genero == null || genero.getNombre() == null) {
                return "Sin género";
            }

            return genero.getNombre();

        } catch (PersistenciaException | IllegalArgumentException ex) {
            return "Sin género";
        }
    }

    private void cargarImagen(JLabel label, String rutaImagen, int ancho, int alto) {
        label.setText("");
        label.setIcon(null);

        if (rutaImagen == null || rutaImagen.isBlank()) {
            label.setText("Sin imagen");
            return;
        }

        try {
            String ruta = rutaImagen.trim().replace("\\", "/");
            while (ruta.startsWith("/")) {
                ruta = ruta.substring(1);
            }

            URL urlImagen = getClass().getClassLoader().getResource(ruta);
            ImageIcon icono;

            if (urlImagen != null) {
                icono = new ImageIcon(urlImagen);
            } else {
                File archivo = new File(rutaImagen);
                icono = archivo.exists()
                        ? new ImageIcon(archivo.getAbsolutePath())
                        : new ImageIcon(rutaImagen);
            }

            if (icono.getIconWidth() <= 0) {
                label.setText("Sin imagen");
                return;
            }

            Image imagenEscalada = icono.getImage().getScaledInstance(
                    ancho,
                    alto,
                    Image.SCALE_SMOOTH
            );
            label.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception ex) {
            label.setText("Sin imagen");
        }
    }

    private void configurarNavegacion() {
        btnMenuPrincipal.addActionListener(e -> {
            new frmMenuPrinicipal().setVisible(true);
            dispose();
        });

        btnArtistas.addActionListener(e -> {
            new frmMenuArtista().setVisible(true);
            dispose();
        });

        btnAlbumes.addActionListener(e -> {
            new frmAlbum().setVisible(true);
            dispose();
        });

        btnFavoritos.addActionListener(e -> abrirFavoritos());

        jButton5.addActionListener(e -> {
            new frmPerfil().setVisible(true);
            dispose();
        });
    }

    private void abrirFavoritos() {
        try {
            new frmFavoritos().setVisible(true);
            dispose();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible abrir favoritos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarPerfilLateral() {
        UsuarioDTO usuario = Sesion.getUsuarioActual();

        if (usuario == null) {
            lblNombrePerfil.setText("Sin sesión");
            lblFotoPerfil.setText("");
            lblFotoPerfil.setIcon(null);
            return;
        }

        lblNombrePerfil.setText(usuario.getNombreUsuario());
        cargarImagen(lblFotoPerfil, usuario.getImagenPerfil(), 34, 30);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblFotoAlbum = new javax.swing.JLabel();
        lblFotoPerfilArtista = new javax.swing.JLabel();
        lblNombreArtista = new javax.swing.JLabel();
        lblNombreALbum = new javax.swing.JLabel();
        lblFechaAlbum = new javax.swing.JLabel();
        lblGeneroAlbum = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaInfoCanciones = new javax.swing.JTable();
        btnVolver = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnMenuPrincipal = new javax.swing.JButton();
        btnArtistas = new javax.swing.JButton();
        btnAlbumes = new javax.swing.JButton();
        btnFavoritos = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        lblNombrePerfil = new javax.swing.JLabel();
        lblFotoPerfil = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("ALBUMES");

        lblFotoAlbum.setText("jLabel3");

        lblFotoPerfilArtista.setText("jLabel4");

        lblNombreArtista.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombreArtista.setText("jLabel5");

        lblNombreALbum.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombreALbum.setText("jLabel6");

        lblFechaAlbum.setText("jLabel7");

        lblGeneroAlbum.setText("jLabel8");

        tablaInfoCanciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "#", "Nombre", "Duración", "Favorito"
            }
        ));
        jScrollPane3.setViewportView(tablaInfoCanciones);

        btnVolver.setText("Volver");
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblFotoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblNombreALbum, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblFotoPerfilArtista, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNombreArtista, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblFechaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblGeneroAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVolver)))
                .addContainerGap(218, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFotoPerfilArtista, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreArtista))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombreALbum))
                    .addComponent(lblFotoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaAlbum)
                    .addComponent(lblGeneroAlbum))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(212, 212, 212))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnVolver)
                        .addGap(221, 221, 221))))
        );

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        btnMenuPrincipal.setText("Menu Principal");

        btnArtistas.setText("Artistas");

        btnAlbumes.setText("Albumes");

        btnFavoritos.setText("Favoritos");

        jButton5.setText("Perfil");

        lblNombrePerfil.setText("Nombre");

        lblFotoPerfil.setText("jLabel12");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAlbumes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFavoritos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addGap(17, 17, 17))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNombrePerfil)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombrePerfil)
                    .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(btnMenuPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnArtistas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAlbumes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFavoritos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel2))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        new frmAlbum().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmDetalleAlbum().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlbumes;
    private javax.swing.JButton btnArtistas;
    private javax.swing.JButton btnFavoritos;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblFechaAlbum;
    private javax.swing.JLabel lblFotoAlbum;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblFotoPerfilArtista;
    private javax.swing.JLabel lblGeneroAlbum;
    private javax.swing.JLabel lblNombreALbum;
    private javax.swing.JLabel lblNombreArtista;
    private javax.swing.JLabel lblNombrePerfil;
    private javax.swing.JTable tablaInfoCanciones;
    // End of variables declaration//GEN-END:variables
}
