/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Andre
 */
package Presentacion;

import DTO.FavoritoDTO;
import DTO.GeneroDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Negocio.FavoritoBO;
import Negocio.GeneroBO;
import Interfaces.IFavoritoBO;
import Interfaces.IGeneroBO;
import Utilerias.Sesion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class frmFavoritos extends JFrame {

    private UsuarioDTO usuarioActual;
    private final IFavoritoBO favoritoBO;
    private IGeneroBO generoBO;

    private JTextField txtBuscar;
    private JComboBox<String> cmbTipo;
    private JComboBox<GeneroDTO> cmbGenero;
    private JPanel panelResultados;
    private JScrollPane scrollResultados;

    public frmFavoritos() throws PersistenciaException {
        this.usuarioActual = frmMenuPrinicipal.obtenerUsuarioActual();
        this.favoritoBO = new FavoritoBO();
        this.generoBO = new GeneroBO();

        inicializarComponentes();
        cargarGeneros();
        buscarFavoritos();
    }

    private void inicializarComponentes() {
        setTitle("Favoritos");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(217, 217, 217));

        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBackground(new Color(55, 55, 55));
        menu.setBounds(0, 0, 180, 700);
        getContentPane().add(menu);

        JLabel lblLogo = new JLabel("♪ Music");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 18));
        lblLogo.setBounds(25, 20, 130, 30);
        menu.add(lblLogo);

        JButton btnArtistas = crearBotonMenu("Artistas", 70);
        JButton btnAlbumes = crearBotonMenu("Álbumes", 120);
        JButton btnFavoritos = crearBotonMenu("Favoritos", 170);
        JButton btnPerfil = crearBotonMenu("Perfil", 220);
        JButton btnSalir = crearBotonMenu("Salir", 590);

        menu.add(btnArtistas);
        menu.add(btnAlbumes);
        menu.add(btnFavoritos);
        menu.add(btnPerfil);
        menu.add(btnSalir);

        btnArtistas.addActionListener(e -> {
            new frmMenuArtista().setVisible(true);
            dispose();
        });

        btnAlbumes.addActionListener(e -> {
            new frmAlbum().setVisible(true);
            dispose();
        });

        btnFavoritos.addActionListener(e -> buscarFavoritos());

        btnPerfil.addActionListener(e -> {
            new frmPerfil().setVisible(true);
            dispose();
        });

        btnSalir.addActionListener(e -> {
            new frmLogin().setVisible(true);
            dispose();
        });

        JLabel lblTitulo = new JLabel("Favoritos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBounds(220, 25, 250, 40);
        getContentPane().add(lblTitulo);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(220, 85, 280, 32);
        getContentPane().add(txtBuscar);

        cmbTipo = new JComboBox<>();
        cmbTipo.addItem("TODOS");
        cmbTipo.addItem("ARTISTA");
        cmbTipo.addItem("ALBUM");
        cmbTipo.addItem("CANCION");
        cmbTipo.setBounds(520, 85, 130, 32);
        getContentPane().add(cmbTipo);

        cmbGenero = new JComboBox<>();
        cmbGenero.setBounds(670, 85, 180, 32);
        getContentPane().add(cmbGenero);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(870, 85, 100, 32);
        getContentPane().add(btnBuscar);

        btnBuscar.addActionListener(e -> buscarFavoritos());
        txtBuscar.addActionListener(e -> buscarFavoritos());
        cmbTipo.addActionListener(e -> buscarFavoritos());
        cmbGenero.addActionListener(e -> buscarFavoritos());

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(new Color(217, 217, 217));

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setBounds(220, 140, 820, 480);
        scrollResultados.setBorder(null);
        getContentPane().add(scrollResultados);
    }

    private JButton crearBotonMenu(String texto, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(25, y, 130, 35);
        boton.setBackground(new Color(35, 35, 35));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    private void cargarGeneros() throws PersistenciaException {
        cmbGenero.removeAllItems();
        cmbGenero.addItem(null);
        List<GeneroDTO> generos = generoBO.consultarTodos();
        for (GeneroDTO genero : generos) {
            cmbGenero.addItem(genero);
        }
    }

    private void buscarFavoritos() {
        this.usuarioActual = Sesion.getUsuarioActual();
        if (this.usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No existe una sesión iniciada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String texto = txtBuscar.getText().trim();
            String tipo = cmbTipo.getSelectedItem() != null ? cmbTipo.getSelectedItem().toString() : "TODOS";
            String idGenero = null;
            if (cmbGenero.getSelectedItem() instanceof GeneroDTO) {
                GeneroDTO generoSeleccionado = (GeneroDTO) cmbGenero.getSelectedItem();
                idGenero = generoSeleccionado.getId();
            }
            List<FavoritoDTO> favoritosEncontrados = favoritoBO.buscarFavoritos(this.usuarioActual.getId(), texto, tipo, idGenero);
            cargarFavoritos(favoritosEncontrados);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al buscar favoritos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFavoritos(List<FavoritoDTO> favoritos) {
        panelResultados.removeAll();
        if (favoritos == null || favoritos.isEmpty()) {
            JLabel lblMensaje = new JLabel("No se encontraron favoritos.");
            lblMensaje.setFont(new Font("Arial", Font.PLAIN, 18)
            );
            panelResultados.add(lblMensaje);
        } else {
            for (FavoritoDTO favorito : favoritos) {
                panelResultados.add(crearTarjetaFavorito(favorito));
            }
        }
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private JPanel crearTarjetaFavorito(FavoritoDTO favorito) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setPreferredSize(new Dimension(780, 130));
        tarjeta.setMaximumSize(new Dimension(780, 130));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new LineBorder(Color.GRAY, 1));

        JLabel lblImagen = new JLabel();
        lblImagen.setBounds(15, 15, 100, 100);
        lblImagen.setBorder(new LineBorder(Color.LIGHT_GRAY));
        cargarImagen(lblImagen, favorito.getImagen(), 100, 100);
        tarjeta.add(lblImagen);

        JLabel lblNombre = new JLabel(favorito.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setBounds(135, 15, 360, 30);
        tarjeta.add(lblNombre);

        JLabel lblTipo = new JLabel("Tipo: " + favorito.getTipo());
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 15));
        lblTipo.setBounds(135, 50, 250, 25);
        tarjeta.add(lblTipo);

        JLabel lblArtista = new JLabel("Artista: " + textoSeguro(favorito.getNombreArtista()));
        lblArtista.setFont(new Font("Arial", Font.PLAIN, 15));
        lblArtista.setBounds(135, 75, 350, 25);
        tarjeta.add(lblArtista);

        JLabel lblFecha = new JLabel("Agregado: " + favorito.getFechaAgregacion());
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 15));
        lblFecha.setBounds(135, 100, 300, 25);
        tarjeta.add(lblFecha);

        JButton btnEliminar = new JButton("★ Quitar");
        btnEliminar.setBounds(620, 45, 130, 40);
        btnEliminar.setBackground(new Color(35, 35, 35));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        tarjeta.add(btnEliminar);

        btnEliminar.addActionListener(e -> eliminarFavorito(favorito));

        return tarjeta;
    }

    private void eliminarFavorito(FavoritoDTO favorito) {
        UsuarioDTO usuario = Sesion.getUsuarioActual();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe una sesión iniciada.", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }
        int opcion = JOptionPane.showConfirmDialog( this,"¿Deseas eliminar este elemento de favoritos?", "Confirmar",JOptionPane.YES_NO_OPTION );
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean eliminado = favoritoBO.eliminarElementoFavorito( usuario.getId(), favorito.getTipo(), favorito.getIdElemento() );
            if (eliminado) {
                JOptionPane.showMessageDialog( this,"Favorito eliminado.");
                buscarFavoritos();
            } else {
                JOptionPane.showMessageDialog(this,"No se encontró el favorito para eliminar.");
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al eliminar favorito",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarImagen(JLabel label, String ruta, int ancho, int alto) {
        try {
            if (ruta == null || ruta.isBlank()) {
                label.setText("Sin imagen");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return;
            }

            ImageIcon icono;

            if (ruta.startsWith("imagenes/")) {
                icono = new ImageIcon(
                        getClass().getResource("/" + ruta)
                );
            } else {
                icono = new ImageIcon(ruta);
            }

            Image imagen = icono.getImage().getScaledInstance(
                    ancho,
                    alto,
                    Image.SCALE_SMOOTH
            );

            label.setText("");
            label.setIcon(new ImageIcon(imagen));

        } catch (Exception e) {
            label.setText("Sin imagen");
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private String textoSeguro(String texto) {
        if (texto == null || texto.isBlank()) {
            return "N/A";
        }
        return texto;
    }
}
