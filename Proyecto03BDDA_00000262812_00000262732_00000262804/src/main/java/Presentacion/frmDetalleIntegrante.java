/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.ArtistaDTO;
import DTO.IntegranteDTO;
import DTO.PersonaDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Excepciones.PresentacionException;
import Interfaces.IConexionBD;
import Interfaces.IFavoritoBO;
import Interfaces.IPersonaBO;
import Interfaces.IPersonaDAO;
import Negocio.FavoritoBO;
import Negocio.PersonaBO;
import Persistencia.ConexionBD;
import Persistencia.PersonaDAO;
import Utilerias.Sesion;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class frmDetalleIntegrante extends javax.swing.JFrame {

    private ArtistaDTO artista;
    private IntegranteDTO integrante;
    private PersonaDTO persona;
    private IPersonaBO personaBO;
    private final IFavoritoBO favoritoBO = new FavoritoBO();

    public frmDetalleIntegrante(ArtistaDTO artista, IntegranteDTO integrante) {
        initComponents();
        this.artista = artista;
        this.integrante = integrante;
        inicializarBOs();
        configurarPantalla();
        setLocationRelativeTo(null);
        try {
            cargarPersona();
        } catch (PresentacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        cargarDatosIntegrante();
    }

    private void inicializarBOs() {
        IConexionBD conexionBD = new ConexionBD();
        IPersonaDAO personaDAO = new PersonaDAO(conexionBD);
        this.personaBO = new PersonaBO(personaDAO);
    }

    private void configurarPantalla() {
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        lblImagenIntegrante.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenIntegrante.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void cargarPersona() throws PresentacionException {
        if (integrante == null) {
            throw new PresentacionException("No se recibió la información del integrante.");
        }
        String idPersona = integrante.getIdPersona();
        if (idPersona == null || idPersona.trim().isEmpty()) {
            throw new PresentacionException("El integrante no tiene una persona relacionada.");
        }

        if (!ObjectId.isValid(idPersona)) {
            throw new PresentacionException("El identificador de la persona no es válido.");
        }

        try {
            persona = personaBO.consultarPorId(
                    new ObjectId(idPersona)
            );
            if (persona == null) {
                throw new PresentacionException(
                        "No se encontró la información de la persona."
                );
            }

        } catch (NegocioException ex) {
            throw new PresentacionException("No fue posible consultar la información del integrante." + ex.getMessage());
        }
    }

    private void cargarDatosIntegrante() {
        if (integrante == null) {
            return;
        }

        if (persona != null) {
            lblNombreIntegrante.setText(persona.getNombre() != null ? persona.getNombre() : "Nombre no disponible");
            txtDescripcion.setText(persona.getDescripcion() != null ? persona.getDescripcion() : "Sin descripción registrada.");
            cargarImagen(lblImagenIntegrante, persona.getImagen(), 140, 140);

        } else {
            lblNombreIntegrante.setText("Nombre no disponible");
            txtDescripcion.setText("Sin descripción registrada.");
            lblImagenIntegrante.setIcon(null);
            lblImagenIntegrante.setText("Sin imagen");
        }
        lblRolIntegrante.setText("Rol: " + valorSeguro(integrante.getRol()));
        lblFechaIngreso.setText("Fecha ingreso: " + (integrante.getFechaIngreso() != null ? integrante.getFechaIngreso().toString() : "N/A"));
        lblFechaSalida.setText("Fecha salida: " + (integrante.getFechaSalida() != null ? integrante.getFechaSalida().toString() : "N/A"));
        lblEstado.setText("Estado: " + (integrante.isActivo() ? "Activo" : "Inactivo"));
    }

    private String valorSeguro(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return "N/A";
        }
        return valor;
    }

    private void cargarImagen(JLabel label, String rutaImagen, int ancho, int alto) {
        label.setText("");
        label.setIcon(null);
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            label.setText("Sin imagen");
            return;
        }
        try {
            String rutaNormalizada = rutaImagen.trim().replace("\\", "/");
            while (rutaNormalizada.startsWith("/")) {
                rutaNormalizada = rutaNormalizada.substring(1);
            }
            ImageIcon icono = null;
            URL url = Thread.currentThread().getContextClassLoader().getResource(rutaNormalizada);
            if (url != null) {
                icono = new ImageIcon(url);
            } else {
                File archivo = new File(rutaImagen);
                if (archivo.exists()) {
                    icono = new ImageIcon(archivo.getAbsolutePath());
                }
            }
            if (icono == null || icono.getIconWidth() <= 0) {
                label.setText("Sin imagen");
                return;
            }
            Image imagenEscalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception ex) {
            label.setIcon(null);
            label.setText("Sin imagen");
            label.setForeground(Color.BLACK);
        }
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
        jLabel1 = new javax.swing.JLabel();
        lblNombreIntegrante = new javax.swing.JLabel();
        lblRolIntegrante = new javax.swing.JLabel();
        lblFechaIngreso = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblImagenIntegrante = new javax.swing.JLabel();
        btnAgregarFavoritos = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        lblFechaSalida = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Detalle Integrante");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Detalle Artistas");

        lblNombreIntegrante.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblNombreIntegrante.setText("Nombre Integrante:");

        lblRolIntegrante.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblRolIntegrante.setText("Rol: ");

        lblFechaIngreso.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblFechaIngreso.setText("Fecha Ingreso: ");

        lblEstado.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblEstado.setText("Estado: ");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Descripción:");

        lblImagenIntegrante.setText("jLabel2");

        btnAgregarFavoritos.setBackground(new java.awt.Color(0, 255, 0));
        btnAgregarFavoritos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnAgregarFavoritos.setForeground(new java.awt.Color(0, 0, 0));
        btnAgregarFavoritos.setText("Agregar Favoritos");
        btnAgregarFavoritos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFavoritosActionPerformed(evt);
            }
        });

        btnRegresar.setBackground(new java.awt.Color(0, 0, 0));
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        lblFechaSalida.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblFechaSalida.setText("Fecha Salida:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(jLabel1)
                        .addGap(0, 277, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblImagenIntegrante)
                                            .addComponent(lblFechaSalida)
                                            .addComponent(lblRolIntegrante)
                                            .addComponent(lblFechaIngreso)
                                            .addComponent(lblEstado)
                                            .addComponent(jLabel6)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblNombreIntegrante)
                                                .addGap(207, 207, 207)
                                                .addComponent(btnAgregarFavoritos, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addGap(80, 80, 80)
                .addComponent(lblImagenIntegrante)
                .addGap(84, 84, 84)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreIntegrante)
                    .addComponent(btnAgregarFavoritos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblRolIntegrante)
                .addGap(18, 18, 18)
                .addComponent(lblFechaIngreso)
                .addGap(18, 18, 18)
                .addComponent(lblFechaSalida)
                .addGap(18, 18, 18)
                .addComponent(lblEstado)
                .addGap(40, 40, 40)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        frmMenuPrinicipal pantallaMenuPrincipal = new frmMenuPrinicipal();
        pantallaMenuPrincipal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    private void btnArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtistasActionPerformed
        frmMenuArtista menuArtista = new frmMenuArtista();
        menuArtista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnArtistasActionPerformed

    private void btnAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlbumesActionPerformed
        new frmAlbum().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnAlbumesActionPerformed

    private void btnFavoritosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFavoritosActionPerformed
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
    }//GEN-LAST:event_btnFavoritosActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        new frmPerfil().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnPerfilActionPerformed

    private void btnAgregarFavoritosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFavoritosActionPerformed
        UsuarioDTO usuario = Sesion.getUsuarioActual();

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existe un usuario con sesión iniciada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (artista == null || artista.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "El artista no tiene un identificador válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            boolean agregado = favoritoBO.agregarArtistaFavorito(
                    usuario.getId(),
                    artista.getId()
            );

            if (agregado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Artista agregado a favoritos: " + artista.getNombre()
                );
                btnAgregarFavoritos.setEnabled(false);
                btnAgregarFavoritos.setText("Ya está en favoritos");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "El artista ya estaba en favoritos.",
                        "Favoritos",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al agregar favorito",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnAgregarFavoritosActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        frmDetalleArtista detalleArtista = new frmDetalleArtista(artista);
        detalleArtista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarFavoritos;
    private javax.swing.JButton btnAlbumes;
    private javax.swing.JButton btnArtistas;
    private javax.swing.JButton btnFavoritos;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaIngreso;
    private javax.swing.JLabel lblFechaSalida;
    private javax.swing.JLabel lblImagenIntegrante;
    private javax.swing.JLabel lblNombreIntegrante;
    private javax.swing.JLabel lblRolIntegrante;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JTextArea txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
