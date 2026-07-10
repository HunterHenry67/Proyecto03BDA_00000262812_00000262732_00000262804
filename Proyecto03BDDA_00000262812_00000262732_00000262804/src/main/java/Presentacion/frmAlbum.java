/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import DTO.AlbumDTO;
import DTO.ArtistaDTO;
import Excepciones.NegocioException;
import Interfaces.IArtistaBO;
import Negocio.ArtistaBO;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author user
 */
public class frmAlbum extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmAlbum.class.getName());

    private IArtistaBO artistaBO;

    private JLabel[] labelsImagenes;
    private JLabel[] labelsArtistas;
    private JLabel[] labelsNombresAlbum;
    private int paginaActual = 0;
    private static final int ALBUMES_POR_PAGINA = 8;

    private List<AlbumDTO> albumesMostrados = new ArrayList<>();
    private List<String> artistasAlbumes = new ArrayList<>();
    
    /**
     * Creates new form frmAlbum
     */
    public frmAlbum() {
        this(new ArtistaBO());
    }

    public frmAlbum(IArtistaBO artistaBO) {
        initComponents();

        this.artistaBO = artistaBO;
        configurarBuscador();
        inicializarLabels();
        limpiarLabels();
        cargarAlbumes();

        setLocationRelativeTo(null);
    }

    private void inicializarLabels() {

        labelsImagenes = new JLabel[]{
            lblAlbum1, lblAlbum2, lblAlbum3, lblAlbum4, lblAlbum5, lblAlbum6, lblAlbum7, lblAlbum8 };

        labelsArtistas = new JLabel[]{lblArtista1, lblArtista2, lblArtista3, lblArtista4,lblNombreArtista5, lblNombreArtista6, lblNombreArtista7,lblNombreArtista8};

        labelsNombresAlbum = new JLabel[]{lblNombreAlbum1, lblNombreAlbum2, lblNombreAlbum3, lblNombreAlbum4, lblNombreAlbum5, lblNombreAlbum6, lblNombreAlbum7, lblNombreAlbum8};

        for (int i = 0; i < labelsImagenes.length; i++) {
            labelsImagenes[i].setHorizontalAlignment(
                    SwingConstants.CENTER
            );
            
            labelsImagenes[i].setVerticalAlignment(
                    SwingConstants.CENTER
            );
            
            labelsArtistas[i].setHorizontalAlignment(
                    SwingConstants.CENTER
            );
            
            labelsNombresAlbum[i].setHorizontalAlignment(
                    SwingConstants.CENTER
            );
        }
    }
    
    private void configurarBuscador() {
        txtBuscadorAlbumes.getDocument().addDocumentListener( new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filtrarAlbumes();}
            @Override
            public void removeUpdate(DocumentEvent e) {filtrarAlbumes();}
            @Override
            public void changedUpdate(DocumentEvent e) {filtrarAlbumes();}
        });
    }

    private void cargarAlbumes() {
        try {

            List<ArtistaDTO> artistas = artistaBO.consultarTodos();
            albumesMostrados.clear();
            artistasAlbumes.clear();

            if (artistas == null) {
                mostrarAlbumes();
                return;
            }
            
            for (ArtistaDTO artista : artistas) {
                if (artista == null) {
                    continue;
                }
                if (artista.getAlbumes() == null) {
                    continue;
                }
                for (AlbumDTO album : artista.getAlbumes()) {
                    if (album == null) {
                        continue;
                    }

                    albumesMostrados.add(album);
                    artistasAlbumes.add(artista.getNombre());
                }
            }
            paginaActual = 0;
            mostrarAlbumes();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void filtrarAlbumes() {
        try {
            List<ArtistaDTO> artistas = artistaBO.consultarTodos();
            albumesMostrados.clear();
            artistasAlbumes.clear();

            String texto = txtBuscadorAlbumes.getText().trim().toLowerCase();

            for (ArtistaDTO artista : artistas) {
                if (artista == null || artista.getAlbumes() == null) {
                    continue;
                }

                for (AlbumDTO album : artista.getAlbumes()) {
                    String nombreArtista = artista.getNombre();
                    String nombreAlbum = album.getNombre();
                    String fecha = album.getFechaLanzamiento();

                    if (nombreArtista == null) {
                        nombreArtista = "";
                    }
                    if (nombreAlbum == null) {
                        nombreAlbum = "";
                    }
                    if (fecha == null) {
                        fecha = "";
                    }

                    String genero = obtenerNombreGenero(album.getIdGenero());
                    boolean coincide =
                            nombreArtista.toLowerCase().contains(texto)
                            || nombreAlbum.toLowerCase().contains(texto)
                            || fecha.toLowerCase().contains(texto)
                            || genero.toLowerCase().contains(texto);

                    if (coincide) {
                        albumesMostrados.add(album);
                        artistasAlbumes.add(nombreArtista);
                    }
                }
            }

            paginaActual = 0;
            mostrarAlbumes();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }

    }
    
    private String obtenerNombreGenero(String idGenero) {
        if (idGenero == null) {
            return "";
        }
        if (idGenero.equals("6871f8c9e7b12a001a45a101")) {
            return "Pop";
        }
        if (idGenero.equals("6871f8c9e7b12a001a45a102")) {
            return "Rock Alternativo";
        }
        if (idGenero.equals("6871f8c9e7b12a001a45a103")) {
            return "Reggaeton";
        }
        return "";
    }
    
    private void mostrarAlbumes() {
        limpiarLabels();

        int inicio = paginaActual * ALBUMES_POR_PAGINA;
        int fin = inicio + ALBUMES_POR_PAGINA;

        if (fin > albumesMostrados.size()) {
            fin = albumesMostrados.size();
        }
        int posicionLabel = 0;

        for (int i = inicio; i < fin; i++) {
            AlbumDTO album = albumesMostrados.get(i);
            String nombreArtista = artistasAlbumes.get(i);

            labelsArtistas[posicionLabel].setText(nombreArtista);
            labelsNombresAlbum[posicionLabel].setText(album.getNombre());

            cargarImagen(
                    labelsImagenes[posicionLabel],
                    album.getImagenPortada()
            );

            posicionLabel++;
        }
        actualizarBotones();
    }
    
    private void actualizarBotones() {
        btnAtras.setEnabled(paginaActual > 0);

        int totalPaginas = (int) Math.ceil(
                (double) albumesMostrados.size() / ALBUMES_POR_PAGINA
        );
        btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
    }
    
    private void limpiarLabels() {
        if (labelsImagenes == null) {
            return;
        }
        for (int i = 0; i < labelsImagenes.length; i++) {

            labelsImagenes[i].setText("");
            labelsImagenes[i].setIcon(null);
            labelsArtistas[i].setText("");
            labelsNombresAlbum[i].setText("");
        }
    }
    
    private void cargarImagen(JLabel label, String rutaImagen) {
        label.setIcon(null);
        label.setText("");
        if (rutaImagen == null || rutaImagen.isBlank()) {
            label.setText("Sin imagen");
            return;
        }
        
        try {
            String ruta = rutaImagen.replace("\\", "/");

            if (!ruta.startsWith("/")) {
                ruta = "/" + ruta;
            }

            URL urlImagen = getClass().getResource(ruta);

            if (urlImagen == null) {
                System.out.println("No se encontró la imagen: " + ruta);
                label.setText("Sin imagen");
                return;
            }
            
            ImageIcon icono = new ImageIcon(urlImagen);
            Image imagenEscalada = icono.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);

            label.setIcon(new ImageIcon(imagenEscalada));

        } catch (Exception ex) {
            System.out.println("Error cargando imagen: " + ex.getMessage());
            label.setText("Sin imagen");
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        lblNombrePerfil = new javax.swing.JLabel();
        lblFotoPerfil = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblAlbum2 = new javax.swing.JLabel();
        lblAlbum3 = new javax.swing.JLabel();
        lblAlbum5 = new javax.swing.JLabel();
        lblAlbum6 = new javax.swing.JLabel();
        lblAlbum7 = new javax.swing.JLabel();
        lblAlbum4 = new javax.swing.JLabel();
        lblAlbum8 = new javax.swing.JLabel();
        lblAlbum1 = new javax.swing.JLabel();
        lblArtista1 = new javax.swing.JLabel();
        lblNombreAlbum1 = new javax.swing.JLabel();
        lblArtista2 = new javax.swing.JLabel();
        lblNombreAlbum2 = new javax.swing.JLabel();
        lblArtista3 = new javax.swing.JLabel();
        lblNombreAlbum3 = new javax.swing.JLabel();
        lblArtista4 = new javax.swing.JLabel();
        lblNombreAlbum4 = new javax.swing.JLabel();
        lblNombreArtista5 = new javax.swing.JLabel();
        lblNombreArtista6 = new javax.swing.JLabel();
        lblNombreArtista7 = new javax.swing.JLabel();
        lblNombreArtista8 = new javax.swing.JLabel();
        lblNombreAlbum8 = new javax.swing.JLabel();
        lblNombreAlbum7 = new javax.swing.JLabel();
        lblNombreAlbum6 = new javax.swing.JLabel();
        lblNombreAlbum5 = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        lblTituloFrm = new javax.swing.JLabel();
        txtBuscadorAlbumes = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jButton1.setText("Menu Principal");

        jButton2.setText("Artistas");

        jButton3.setText("Albumes");

        jButton4.setText("Favoritos");

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
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(40, 40, 40))
        );

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("Albumes recomendados");

        lblAlbum2.setText("jLabel5");

        lblAlbum3.setText("jLabel6");

        lblAlbum5.setText("jLabel7");

        lblAlbum6.setText("jLabel8");

        lblAlbum7.setText("jLabel9");

        lblAlbum4.setText("jLabel10");

        lblAlbum8.setText("jLabel11");

        lblAlbum1.setText("jLabel4");

        lblArtista1.setText("jLabel13");

        lblNombreAlbum1.setText("jLabel14");

        lblArtista2.setText("jLabel15");

        lblNombreAlbum2.setText("jLabel16");

        lblArtista3.setText("jLabel17");

        lblNombreAlbum3.setText("jLabel18");

        lblArtista4.setText("jLabel19");

        lblNombreAlbum4.setText("jLabel20");

        lblNombreArtista5.setText("jLabel1");

        lblNombreArtista6.setText("jLabel2");

        lblNombreArtista7.setText("jLabel4");

        lblNombreArtista8.setText("jLabel5");

        lblNombreAlbum8.setText("jLabel6");

        lblNombreAlbum7.setText("jLabel7");

        lblNombreAlbum6.setText("jLabel8");

        lblNombreAlbum5.setText("jLabel9");

        btnSiguiente.setText(">");
        btnSiguiente.addActionListener(this::btnSiguienteActionPerformed);

        btnAtras.setText("<");
        btnAtras.addActionListener(this::btnAtrasActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblAlbum5, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblAlbum1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblArtista1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreArtista5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblAlbum2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblAlbum6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblArtista2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreArtista6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblAlbum3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblAlbum7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblArtista3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreArtista7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblAlbum4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(lblAlbum8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(lblNombreArtista8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNombreAlbum8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblArtista4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombreAlbum4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSiguiente)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAlbum2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAlbum3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAlbum4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblAlbum1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblArtista1)
                            .addComponent(lblArtista2)
                            .addComponent(lblArtista3)
                            .addComponent(lblArtista4))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreAlbum1)
                            .addComponent(lblNombreAlbum2)
                            .addComponent(lblNombreAlbum3)
                            .addComponent(lblNombreAlbum4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAlbum5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAlbum6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAlbum7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAlbum8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreArtista5)
                            .addComponent(lblNombreArtista6)
                            .addComponent(lblNombreArtista7)
                            .addComponent(lblNombreArtista8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreAlbum8)
                            .addComponent(lblNombreAlbum7)
                            .addComponent(lblNombreAlbum6)
                            .addComponent(lblNombreAlbum5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSiguiente)
                            .addComponent(btnAtras))))
                .addContainerGap())
        );

        lblTituloFrm.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTituloFrm.setForeground(new java.awt.Color(153, 153, 153));
        lblTituloFrm.setText("ALBUMES");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(lblTituloFrm)
                        .addGap(221, 221, 221)
                        .addComponent(txtBuscadorAlbumes)
                        .addGap(31, 31, 31)))
                .addGap(0, 148, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTituloFrm)
                            .addComponent(txtBuscadorAlbumes, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        // TODO add your handling code here:
        int totalPaginas = (int) Math.ceil((double) albumesMostrados.size() / ALBUMES_POR_PAGINA);

        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            mostrarAlbumes();
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        // TODO add your handling code here:
        if (paginaActual > 0) {
            paginaActual--;
            mostrarAlbumes();
        }
    }//GEN-LAST:event_btnAtrasActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new frmAlbum().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblAlbum1;
    private javax.swing.JLabel lblAlbum2;
    private javax.swing.JLabel lblAlbum3;
    private javax.swing.JLabel lblAlbum4;
    private javax.swing.JLabel lblAlbum5;
    private javax.swing.JLabel lblAlbum6;
    private javax.swing.JLabel lblAlbum7;
    private javax.swing.JLabel lblAlbum8;
    private javax.swing.JLabel lblArtista1;
    private javax.swing.JLabel lblArtista2;
    private javax.swing.JLabel lblArtista3;
    private javax.swing.JLabel lblArtista4;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblNombreAlbum1;
    private javax.swing.JLabel lblNombreAlbum2;
    private javax.swing.JLabel lblNombreAlbum3;
    private javax.swing.JLabel lblNombreAlbum4;
    private javax.swing.JLabel lblNombreAlbum5;
    private javax.swing.JLabel lblNombreAlbum6;
    private javax.swing.JLabel lblNombreAlbum7;
    private javax.swing.JLabel lblNombreAlbum8;
    private javax.swing.JLabel lblNombreArtista5;
    private javax.swing.JLabel lblNombreArtista6;
    private javax.swing.JLabel lblNombreArtista7;
    private javax.swing.JLabel lblNombreArtista8;
    private javax.swing.JLabel lblNombrePerfil;
    private javax.swing.JLabel lblTituloFrm;
    private javax.swing.JTextField txtBuscadorAlbumes;
    // End of variables declaration//GEN-END:variables
}
