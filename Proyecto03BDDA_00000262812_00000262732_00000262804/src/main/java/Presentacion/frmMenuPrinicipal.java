/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.AlbumDTO;
import DTO.ArtistaDTO;
import DTO.CancionDTO;
import Excepciones.NegocioException;
import Interfaces.IArtistaBO;
import Negocio.ArtistaBO;
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
import DTO.UsuarioDTO;
import Excepciones.PersistenciaException;

/**
 *
 * @author BALAMRUSH
 */
public class frmMenuPrinicipal extends javax.swing.JFrame {

    private IArtistaBO artistaBO;
    private static final int LIMITE_CANCIONES = 4;
    private static final int LIMITE_ARTISTAS = 4;
    private static final int LIMITE_ALBUMES = 4;
    private static UsuarioDTO usuarioActual;

    public static void establecerUsuarioActual(UsuarioDTO usuario) {
        usuarioActual = usuario;
    }

    public static UsuarioDTO obtenerUsuarioActual() {
        return usuarioActual;
    }

    public frmMenuPrinicipal() {
        initComponents();
        inicializarBOs();
        configurarPaneles();
        setLocationRelativeTo(null);
        cargarInicio();
    }

    private void inicializarBOs() {
        this.artistaBO = new ArtistaBO();
    }

    private void configurarPaneles() {
        pnlCancionesRecientes.setLayout(new GridLayout(1, 4, 20, 20));
        pnlArtistasRecomendados.setLayout(new GridLayout(1, 4, 20, 20));
        pnlAlbumesRecientes.setLayout(new GridLayout(1, 4, 20, 20));
        pnlCancionesRecientes.setBackground(new Color(220, 220, 220));
        pnlArtistasRecomendados.setBackground(new Color(220, 220, 220));
        pnlAlbumesRecientes.setBackground(new Color(220, 220, 220));
        txtFieldBuscar.addActionListener(e -> buscarGeneral());
    }

    private void cargarInicio() {
        try {
            List<ArtistaDTO> artistas = artistaBO.consultarTodos();
            cargarCancionesRecientes(artistas);
            cargarArtistasRecomendados(artistas);
            cargarAlbumesRecientes(artistas);

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar menú principal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCancionesRecientes(List<ArtistaDTO> artistas) {
        pnlCancionesRecientes.removeAll();
        int contador = 0;
        for (ArtistaDTO artista : artistas) {
            if (artista.getAlbumes() == null) {
                continue;
            }
            for (AlbumDTO album : artista.getAlbumes()) {
                if (album.getCanciones() == null) {
                    continue;
                }
                for (CancionDTO cancion : album.getCanciones()) {
                    pnlCancionesRecientes.add(crearTarjeta(cancion.getNombre(), artista.getNombre(), album.getImagenPortada()));
                    contador++;
                    if (contador == LIMITE_CANCIONES) {
                        llenarEspacios(pnlCancionesRecientes, LIMITE_CANCIONES);
                        refrescarPanel(pnlCancionesRecientes);
                        return;
                    }
                }
            }
        }

        if (contador == 0) {
            pnlCancionesRecientes.add(crearMensaje("No hay canciones."));
        }
        llenarEspacios(pnlCancionesRecientes, LIMITE_CANCIONES);
        refrescarPanel(pnlCancionesRecientes);
    }

    private void cargarArtistasRecomendados(List<ArtistaDTO> artistas) {
        pnlArtistasRecomendados.removeAll();
        int limite = Math.min(LIMITE_ARTISTAS, artistas.size());
        for (int i = 0; i < limite; i++) {
            ArtistaDTO artista = artistas.get(i);
            pnlArtistasRecomendados.add(crearTarjeta(artista.getNombre(), obtenerTipoArtista(artista), artista.getImagen()));
        }

        if (artistas.isEmpty()) {
            pnlArtistasRecomendados.add(crearMensaje("No hay artistas."));
        }
        llenarEspacios(pnlArtistasRecomendados, LIMITE_ARTISTAS);
        refrescarPanel(pnlArtistasRecomendados);
    }

    private void cargarAlbumesRecientes(List<ArtistaDTO> artistas) {
        pnlAlbumesRecientes.removeAll();
        int contador = 0;
        for (ArtistaDTO artista : artistas) {
            if (artista.getAlbumes() == null) {
                continue;
            }
            for (AlbumDTO album : artista.getAlbumes()) {
                pnlAlbumesRecientes.add(crearTarjeta(album.getNombre(), artista.getNombre(), album.getImagenPortada()));
                contador++;
                if (contador == LIMITE_ALBUMES) {
                    llenarEspacios(pnlAlbumesRecientes, LIMITE_ALBUMES);
                    refrescarPanel(pnlAlbumesRecientes);
                    return;
                }
            }
        }
        if (contador == 0) {
            pnlAlbumesRecientes.add(crearMensaje("No hay álbumes."));
        }
        llenarEspacios(pnlAlbumesRecientes, LIMITE_ALBUMES);
        refrescarPanel(pnlAlbumesRecientes);
    }

    private void buscarGeneral() {
        String texto = txtFieldBuscar.getText();
        if (texto == null || texto.trim().isEmpty()) {
            cargarInicio();
            return;
        }
        texto = texto.trim().toLowerCase();
        try {
            List<ArtistaDTO> artistas = artistaBO.consultarTodos();
            pnlCancionesRecientes.removeAll();
            pnlArtistasRecomendados.removeAll();
            pnlAlbumesRecientes.removeAll();
            int contadorArtistas = 0;
            int contadorAlbumes = 0;
            int contadorCanciones = 0;
            for (ArtistaDTO artista : artistas) {

                if (coincide(artista.getNombre(), texto) && contadorArtistas < LIMITE_ARTISTAS) {
                    pnlArtistasRecomendados.add(crearTarjeta(artista.getNombre(), obtenerTipoArtista(artista), artista.getImagen()));
                    contadorArtistas++;
                }
                if (artista.getAlbumes() == null) {
                    continue;
                }
                for (AlbumDTO album : artista.getAlbumes()) {
                    if ((coincide(album.getNombre(), texto) || coincide(artista.getNombre(), texto)) && contadorAlbumes < LIMITE_ALBUMES) {
                        pnlAlbumesRecientes.add(crearTarjeta(album.getNombre(), artista.getNombre(), album.getImagenPortada()));
                        contadorAlbumes++;
                    }
                    if (album.getCanciones() == null) {
                        continue;
                    }
                    for (CancionDTO cancion : album.getCanciones()) {
                        if ((coincide(cancion.getNombre(), texto) || coincide(album.getNombre(), texto) || coincide(artista.getNombre(), texto)) && contadorCanciones < LIMITE_CANCIONES) {
                            pnlCancionesRecientes.add(crearTarjeta(cancion.getNombre(), artista.getNombre(), album.getImagenPortada()));
                            contadorCanciones++;
                        }
                    }
                }
            }

            if (contadorCanciones == 0) {
                pnlCancionesRecientes.add(crearMensaje("Sin canciones."));
            }
            if (contadorArtistas == 0) {
                pnlArtistasRecomendados.add(crearMensaje("Sin artistas."));
            }
            if (contadorAlbumes == 0) {
                pnlAlbumesRecientes.add(crearMensaje("Sin álbumes."));
            }
            llenarEspacios(pnlCancionesRecientes, LIMITE_CANCIONES);
            llenarEspacios(pnlArtistasRecomendados, LIMITE_ARTISTAS);
            llenarEspacios(pnlAlbumesRecientes, LIMITE_ALBUMES);
            refrescarPanel(pnlCancionesRecientes);
            refrescarPanel(pnlArtistasRecomendados);
            refrescarPanel(pnlAlbumesRecientes);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al buscar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JPanel crearTarjeta(String titulo, String subtitulo, String rutaImagen) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.BLACK);
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(110, 100));
        cargarImagen(lblImagen, rutaImagen);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.LIGHT_GRAY);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 10));
        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setBackground(Color.BLACK);
        panelTexto.add(lblTitulo);
        panelTexto.add(lblSubtitulo);
        tarjeta.add(lblImagen, BorderLayout.CENTER);
        tarjeta.add(panelTexto, BorderLayout.SOUTH);
        return tarjeta;
    }

    private void cargarImagen(JLabel label, String rutaImagen) {
        try {
            label.setText("");
            label.setIcon(null);
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
            Image imagen = icono.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
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

    private boolean coincide(String valor, String texto) {
        return valor != null && valor.toLowerCase().contains(texto);
    }

    private JLabel crearMensaje(String texto) {
        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JPanel crearEspacioVacio() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(220, 220, 220));
        return panel;
    }

    private void llenarEspacios(JPanel panel, int limite) {
        while (panel.getComponentCount() < limite) {
            panel.add(crearEspacioVacio());
        }
    }

    private void refrescarPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
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
        jLabel2 = new javax.swing.JLabel();
        pnlCancionesRecientes = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlArtistasRecomendados = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pnlAlbumesRecientes = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú Principal");

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

        btnBuscar.setBackground(new java.awt.Color(51, 102, 255));
        btnBuscar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Menú Principal");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Canciones Recomendades");

        javax.swing.GroupLayout pnlCancionesRecientesLayout = new javax.swing.GroupLayout(pnlCancionesRecientes);
        pnlCancionesRecientes.setLayout(pnlCancionesRecientesLayout);
        pnlCancionesRecientesLayout.setHorizontalGroup(
            pnlCancionesRecientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlCancionesRecientesLayout.setVerticalGroup(
            pnlCancionesRecientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Artistas Recomendados");

        javax.swing.GroupLayout pnlArtistasRecomendadosLayout = new javax.swing.GroupLayout(pnlArtistasRecomendados);
        pnlArtistasRecomendados.setLayout(pnlArtistasRecomendadosLayout);
        pnlArtistasRecomendadosLayout.setHorizontalGroup(
            pnlArtistasRecomendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
        );
        pnlArtistasRecomendadosLayout.setVerticalGroup(
            pnlArtistasRecomendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Álbumes Recomendados");

        javax.swing.GroupLayout pnlAlbumesRecientesLayout = new javax.swing.GroupLayout(pnlAlbumesRecientes);
        pnlAlbumesRecientes.setLayout(pnlAlbumesRecientesLayout);
        pnlAlbumesRecientesLayout.setHorizontalGroup(
            pnlAlbumesRecientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlAlbumesRecientesLayout.setVerticalGroup(
            pnlAlbumesRecientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );

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
                        .addGap(269, 269, 269))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(pnlArtistasRecomendados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlCancionesRecientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBuscar)
                                .addGap(18, 18, 18)
                                .addComponent(txtFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addComponent(pnlAlbumesRecientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 53, Short.MAX_VALUE))))
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
                .addGap(47, 47, 47)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCancionesRecientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlArtistasRecomendados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAlbumesRecientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarGeneral();
    }//GEN-LAST:event_btnBuscarActionPerformed

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
            System.getLogger(frmMenuPrinicipal.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_btnFavoritosActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        new frmPerfil().setVisible(true);
        dispose();
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
            java.util.logging.Logger.getLogger(frmMenuPrinicipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrinicipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrinicipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrinicipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenuPrinicipal().setVisible(true);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel pnlAlbumesRecientes;
    private javax.swing.JPanel pnlArtistasRecomendados;
    private javax.swing.JPanel pnlCancionesRecientes;
    private javax.swing.JTextField txtFieldBuscar;
    // End of variables declaration//GEN-END:variables
}
