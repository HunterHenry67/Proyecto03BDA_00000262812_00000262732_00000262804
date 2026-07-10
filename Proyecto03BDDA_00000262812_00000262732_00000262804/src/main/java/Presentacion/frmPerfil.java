/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import DTO.UsuarioDTO;
import Excepciones.PersistenciaException;
import Utilerias.Sesion;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class frmPerfil extends JFrame {

    private UsuarioDTO usuarioActual;

    private JLabel lblImagenPerfil;
    private JLabel lblNombreUsuario;
    private JLabel lblCorreo;

    private JButton btnEditarPerfil;
    private JButton btnGenerosNoDeseados;

    public frmPerfil() {
        this.usuarioActual = frmMenuPrinicipal.obtenerUsuarioActual();

        inicializarComponentes();
        cargarDatosUsuario();
    }

    private void inicializarComponentes() {
        setTitle("Perfil");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(217, 217, 217));

        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBackground(new Color(55, 55, 55));
        menu.setBounds(0, 0, 180, 600);
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
        JButton btnSalir = crearBotonMenu("Salir", 490);

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

        btnFavoritos.addActionListener(e -> {
            try {
                new frmFavoritos().setVisible(true);
            } catch (PersistenciaException ex) {
                System.getLogger(frmPerfil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            dispose();
        });

        btnPerfil.addActionListener(e -> cargarDatosUsuario());

        btnSalir.addActionListener(e -> {
            new frmLogin().setVisible(true);
            dispose();
        });

        JLabel lblTitulo = new JLabel("Perfil");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setBounds(220, 35, 300, 40);
        getContentPane().add(lblTitulo);

        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(240, 110, 170, 170);
        lblImagenPerfil.setBorder(new LineBorder(Color.GRAY, 2));
        lblImagenPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblImagenPerfil);

        JLabel lblUsuarioTexto = new JLabel("Nombre de usuario:");
        lblUsuarioTexto.setFont(new Font("Arial", Font.BOLD, 18));
        lblUsuarioTexto.setBounds(450, 130, 220, 30);
        getContentPane().add(lblUsuarioTexto);

        lblNombreUsuario = new JLabel();
        lblNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNombreUsuario.setBounds(450, 165, 350, 30);
        getContentPane().add(lblNombreUsuario);

        JLabel lblCorreoTexto = new JLabel("Correo:");
        lblCorreoTexto.setFont(new Font("Arial", Font.BOLD, 18));
        lblCorreoTexto.setBounds(450, 210, 220, 30);
        getContentPane().add(lblCorreoTexto);

        lblCorreo = new JLabel();
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 18));
        lblCorreo.setBounds(450, 245, 350, 30);
        getContentPane().add(lblCorreo);

        btnEditarPerfil = new JButton("Editar perfil");
        btnEditarPerfil.setBounds(240, 330, 180, 40);
        btnEditarPerfil.setBackground(new Color(35, 35, 35));
        btnEditarPerfil.setForeground(Color.WHITE);
        btnEditarPerfil.setFocusPainted(false);
        getContentPane().add(btnEditarPerfil);

        btnGenerosNoDeseados = new JButton("Géneros no deseados");
        btnGenerosNoDeseados.setBounds(450, 330, 220, 40);
        btnGenerosNoDeseados.setBackground(new Color(35, 35, 35));
        btnGenerosNoDeseados.setForeground(Color.WHITE);
        btnGenerosNoDeseados.setFocusPainted(false);
        getContentPane().add(btnGenerosNoDeseados);

        btnEditarPerfil.addActionListener(e -> {
            new frmEditarPerfil().setVisible(true);
            dispose();
        });

        btnGenerosNoDeseados.addActionListener(e -> {
            try {
                new frmGenerosNoDeseados().setVisible(true);
            } catch (PersistenciaException ex) {
                System.getLogger(frmPerfil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            dispose();
        });
    }

    private JButton crearBotonMenu(String texto, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(25, y, 130, 35);
        boton.setBackground(new Color(35, 35, 35));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    private void cargarDatosUsuario() {
        this.usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog( this, "No hay ningún usuario con sesión iniciada.", "Error", JOptionPane.ERROR_MESSAGE );
            new frmLogin().setVisible(true);
            dispose();
            return;
        }
        lblNombreUsuario.setText( usuarioActual.getNombreUsuario());
        lblCorreo.setText( usuarioActual.getCorreo());
        cargarImagen( lblImagenPerfil, usuarioActual.getImagenPerfil(),170, 170);
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
}
