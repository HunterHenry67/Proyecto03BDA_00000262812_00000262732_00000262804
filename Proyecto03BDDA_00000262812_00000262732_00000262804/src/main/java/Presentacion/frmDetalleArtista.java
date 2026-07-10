/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.AlbumDTO;
import DTO.ArtistaDTO;
import DTO.IntegranteDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IFavoritoBO;
import Negocio.FavoritoBO;
import Utilerias.Sesion;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import DTO.GeneroDTO;
import Excepciones.PersistenciaException;
import Interfaces.IGeneroBO;
import Negocio.GeneroBO;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class frmDetalleArtista extends javax.swing.JFrame {

    private ArtistaDTO artista;
    private JPanel panelTarjetasIntegrantes;
    private List<AlbumDTO> albumesArtista = new ArrayList<>();
    private int paginaAlbumActual = 0;
    private final IFavoritoBO favoritoBO = new FavoritoBO();
    private final IGeneroBO generoBO = new GeneroBO();

    private static final int ALBUMES_POR_PAGINA = 4;

    public frmDetalleArtista(ArtistaDTO artista) {
        initComponents();
        this.artista = artista;
        setLocationRelativeTo(null);
        configurarPantalla();
        cargarDatosArtista();
        cargarIntegrantes();
        inicializarAlbumes();
        cargarAlbumes();
    }

    private void configurarPantalla() {
        panelTarjetasIntegrantes = new JPanel();
        panelTarjetasIntegrantes.setLayout(new GridLayout(0, 4, 12, 12));
        panelTarjetasIntegrantes.setBackground(new Color(220, 220, 220));
        panelTarjetasIntegrantes.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollpane.setViewportView(panelTarjetasIntegrantes);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void inicializarAlbumes() {
        if (artista.getAlbumes() == null) {
            albumesArtista = new ArrayList<>();
        } else {
            albumesArtista = new ArrayList<>(artista.getAlbumes());
        }
        paginaAlbumActual = 0;
    }

    private void cargarDatosArtista() {
        lblNombreArtista.setText("Nombre: " + artista.getNombre());
        lblTipoArtista.setText("Tipo: " + obtenerTipoArtista());
        lblGeneroArtista.setText("Género: "+ obtenerNombreGenero(artista.getIdGenero())
);
        cargarImagen(lblImagenArtista, artista.getImagen(), 120, 120);
    }

    private String obtenerTipoArtista() {
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
    
    private ImageIcon crearIconoTabla(
            String rutaImagen,
            int ancho,
            int alto
    ) {
        try {
            if (rutaImagen == null
                    || rutaImagen.trim().isEmpty()) {
                return null;
            }

            String ruta = rutaImagen
                    .replace("\\", "/");

            if (ruta.startsWith("/")) {
                ruta = ruta.substring(1);
            }

            URL url = getClass()
                    .getClassLoader()
                    .getResource(ruta);

            ImageIcon icono;

            if (url != null) {
                icono = new ImageIcon(url);
            } else {
                icono = new ImageIcon(rutaImagen);
            }

            if (icono.getIconWidth() <= 0) {
                return null;
            }

            Image imagen = icono.getImage()
                    .getScaledInstance(
                            ancho,
                            alto,
                            Image.SCALE_SMOOTH
                    );

            return new ImageIcon(imagen);

        } catch (Exception ex) {
            return null;
        }
    }

    private void cargarIntegrantes() {
        panelTarjetasIntegrantes.removeAll();
        List<IntegranteDTO> integrantes = artista.getIntegrantes();
        if (integrantes == null || integrantes.isEmpty()) {
            JLabel lblSinIntegrantes = new JLabel("Sin integrantes registrados.");
            lblSinIntegrantes.setHorizontalAlignment(SwingConstants.CENTER);
            panelTarjetasIntegrantes.add(lblSinIntegrantes);
            panelTarjetasIntegrantes.setPreferredSize(new Dimension(530, 100));
        } else {
            for (IntegranteDTO integrante : integrantes) {
                panelTarjetasIntegrantes.add(crearTarjetaIntegrante(integrante));
            }
            int columnas = 4;
            int filas = (int) Math.ceil(integrantes.size() / (double) columnas);
            int altoPorFila = 140;
            panelTarjetasIntegrantes.setPreferredSize(new Dimension(530, Math.max(120, filas * altoPorFila)));
        }
        panelTarjetasIntegrantes.revalidate();
        panelTarjetasIntegrantes.repaint();
        scrollpane.revalidate();
        scrollpane.repaint();
    }

    private JPanel crearTarjetaIntegrante(IntegranteDTO integrante) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setPreferredSize(new Dimension(145, 120));
        tarjeta.setMinimumSize(new Dimension(145, 120));
        tarjeta.setMaximumSize(new Dimension(145, 120));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(80, 75));
        cargarImagen(lblImagen, artista.getImagen(), 70, 70);
        String estado = integrante.isActivo() ? "Activo" : "Inactivo";
        JLabel lblTexto = new JLabel(
                "<html>"
                + "<div style='text-align:center; width:125px;'>"
                + integrante.getRol()
                + "<br>"
                + estado
                + "</div>"
                + "</html>"
        );
        lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
        lblTexto.setFont(new Font("Arial", Font.PLAIN, 10));
        tarjeta.add(lblImagen, BorderLayout.CENTER);
        tarjeta.add(lblTexto, BorderLayout.SOUTH);
        MouseAdapter eventoClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                abrirDetalleIntegrante(integrante);
            }
        };
        tarjeta.addMouseListener(eventoClick);
        lblImagen.addMouseListener(eventoClick);
        lblTexto.addMouseListener(eventoClick);

        return tarjeta;
    }

    private void abrirDetalleIntegrante(IntegranteDTO integrante) {
        frmDetalleIntegrante detalleIntegrante
                = new frmDetalleIntegrante(
                        artista,
                        integrante
                );

        detalleIntegrante.setVisible(true);
        this.dispose();
    }

    private void cargarAlbumes() {
        DefaultTableModel modelo =
                new DefaultTableModel(
                        new Object[]{
                            "Portada",
                            "Nombre",
                            "Fecha de publicación",
                            "Género"
                        },
                        0
                ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(
                    int columna
            ) {
                if (columna == 0) {
                    return ImageIcon.class;
                }

                return Object.class;
            }
        };

        int inicio =
                paginaAlbumActual
                * ALBUMES_POR_PAGINA;

        int fin = Math.min(
                inicio + ALBUMES_POR_PAGINA,
                albumesArtista.size()
        );

        for (int i = inicio; i < fin; i++) {
            AlbumDTO album =
                    albumesArtista.get(i);

            ImageIcon portada =
                    crearIconoTabla(
                            album.getImagenPortada(),
                            65,
                            65
                    );

            modelo.addRow(new Object[]{
                portada,
                album.getNombre(),
                album.getFechaLanzamiento(),
                obtenerNombreGenero(
                        album.getIdGenero()
                )
            });
        }

        tblAlbumes.setModel(modelo);
        tblAlbumes.setRowHeight(70);

        tblAlbumes.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(75);

        actualizarControlesPaginacion();
    }

    private void actualizarControlesPaginacion() {
        int totalPaginas;
        if (albumesArtista.isEmpty()) {
            totalPaginas = 1;
        } else {
            totalPaginas = (int) Math.ceil(albumesArtista.size() / (double) ALBUMES_POR_PAGINA);
        }
        lblPaginaAlbumes.setText("Página " + (paginaAlbumActual + 1) + " de " + totalPaginas);
        btnAnteriorAlbumes.setEnabled(paginaAlbumActual > 0);
        btnSiguienteAlbumes.setEnabled(paginaAlbumActual < totalPaginas - 1 && !albumesArtista.isEmpty());
    }

    private void cargarImagen(JLabel label, String rutaImagen, int ancho, int alto) {
        label.setText("");
        label.setIcon(null);
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
                return;
            }
            Image imagen = icono.getImage().getScaledInstance(ancho,alto,Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagen));
        } catch (Exception ex) {
            label.setText("Sin imagen");
        }
    }
    
    private String obtenerNombreGenero(String idGenero) {
        if (idGenero == null
                || idGenero.isBlank()
                || !ObjectId.isValid(idGenero)) {

            return "Sin género";
        }

        try {
            GeneroDTO genero =
                    generoBO.consultarPorId(
                            new ObjectId(idGenero)
                    );

            return genero.getNombre();

        } catch (PersistenciaException
                | IllegalArgumentException ex) {

            return "Sin género";
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
        lblNombreArtista = new javax.swing.JLabel();
        lblTipoArtista = new javax.swing.JLabel();
        lblGeneroArtista = new javax.swing.JLabel();
        pnlIntegrantes5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlbumes = new javax.swing.JTable();
        lblImagenArtista = new javax.swing.JLabel();
        btnAgregarFavoritos = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        scrollpane = new javax.swing.JScrollPane();
        btnAnteriorAlbumes = new javax.swing.JButton();
        btnSiguienteAlbumes = new javax.swing.JButton();
        lblPaginaAlbumes = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Detalle Artista");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Detalle Artistas");

        lblNombreArtista.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblNombreArtista.setText("Nombre:");

        lblTipoArtista.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblTipoArtista.setText("Tipo:");

        lblGeneroArtista.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblGeneroArtista.setText("Género:");

        pnlIntegrantes5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pnlIntegrantes5.setText("Integrantes:");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Colección de Álbumes:");

        tblAlbumes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblAlbumes);

        lblImagenArtista.setText("jLabel2");

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
        btnRegresar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        btnAnteriorAlbumes.setBackground(new java.awt.Color(0, 204, 255));
        btnAnteriorAlbumes.setForeground(new java.awt.Color(0, 0, 0));
        btnAnteriorAlbumes.setText("Anterior");
        btnAnteriorAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorAlbumesActionPerformed(evt);
            }
        });

        btnSiguienteAlbumes.setBackground(new java.awt.Color(0, 204, 255));
        btnSiguienteAlbumes.setForeground(new java.awt.Color(0, 0, 0));
        btnSiguienteAlbumes.setText("Siguiente");
        btnSiguienteAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteAlbumesActionPerformed(evt);
            }
        });

        lblPaginaAlbumes.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(259, 259, 259)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                                    .addComponent(lblTipoArtista)
                                    .addComponent(lblNombreArtista)
                                    .addComponent(lblGeneroArtista)
                                    .addComponent(pnlIntegrantes5)
                                    .addComponent(jLabel6)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblImagenArtista)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnAgregarFavoritos, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12))
                                    .addComponent(scrollpane))))
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(btnAnteriorAlbumes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(lblPaginaAlbumes)
                        .addGap(76, 76, 76)
                        .addComponent(btnSiguienteAlbumes)
                        .addGap(27, 27, 27)
                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImagenArtista)
                    .addComponent(btnAgregarFavoritos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(lblNombreArtista)
                .addGap(18, 18, 18)
                .addComponent(lblTipoArtista)
                .addGap(18, 18, 18)
                .addComponent(lblGeneroArtista)
                .addGap(76, 76, 76)
                .addComponent(pnlIntegrantes5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnteriorAlbumes)
                    .addComponent(btnSiguienteAlbumes)
                    .addComponent(lblPaginaAlbumes))
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
        frmMenuArtista pantallaArtista = new frmMenuArtista();
        pantallaArtista.setVisible(true);
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
            JOptionPane.showMessageDialog(this, "No existe un usuario con sesión iniciada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (artista == null || artista.getId() == null) {
            JOptionPane.showMessageDialog(this, "El artista no tiene un identificador válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            boolean agregado = favoritoBO.agregarArtistaFavorito(usuario.getId(), artista.getId());
            if (agregado) {
                JOptionPane.showMessageDialog(this, "Artista agregado a favoritos: " + artista.getNombre());
                btnAgregarFavoritos.setEnabled(false);
                btnAgregarFavoritos.setText("Ya está en favoritos");
            } else {
                JOptionPane.showMessageDialog(this, "El artista ya estaba en favoritos.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al agregar favorito", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarFavoritosActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        frmMenuArtista menu = new frmMenuArtista();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnAnteriorAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorAlbumesActionPerformed
        if (paginaAlbumActual > 0) {
            paginaAlbumActual--;
            cargarAlbumes();
        }
    }//GEN-LAST:event_btnAnteriorAlbumesActionPerformed

    private void btnSiguienteAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteAlbumesActionPerformed
        int totalPaginas = (int) Math.ceil(albumesArtista.size() / (double) ALBUMES_POR_PAGINA);
        if (paginaAlbumActual < totalPaginas - 1) {
            paginaAlbumActual++;
            cargarAlbumes();
        }
    }//GEN-LAST:event_btnSiguienteAlbumesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarFavoritos;
    private javax.swing.JButton btnAlbumes;
    private javax.swing.JButton btnAnteriorAlbumes;
    private javax.swing.JButton btnArtistas;
    private javax.swing.JButton btnFavoritos;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnSiguienteAlbumes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGeneroArtista;
    private javax.swing.JLabel lblImagenArtista;
    private javax.swing.JLabel lblNombreArtista;
    private javax.swing.JLabel lblPaginaAlbumes;
    private javax.swing.JLabel lblTipoArtista;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JLabel pnlIntegrantes5;
    private javax.swing.JScrollPane scrollpane;
    private javax.swing.JTable tblAlbumes;
    // End of variables declaration//GEN-END:variables
}
