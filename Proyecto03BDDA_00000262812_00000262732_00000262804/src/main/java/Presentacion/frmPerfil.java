/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Negocio.CargaMasivaBO;
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
    private JButton btnCerrarSesion;
    private JButton btnCargaMasiva;

    public frmPerfil() {
        this.usuarioActual = Sesion.getUsuarioActual();

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
        menu.setBackground(new Color(0, 0, 0));
        menu.setBounds(0, 0, 180, 600);
        getContentPane().add(menu);

        JButton btnMenuPrincipal = crearBotonMenu("Menú Principal", 40);
        JButton btnArtistas = crearBotonMenu("Ártistas", 95);
        JButton btnAlbumes = crearBotonMenu("Álbumes", 150);
        JButton btnFavoritos = crearBotonMenu("Favoritos", 205);
        JButton btnPerfil = crearBotonMenu("Perfil", 510);

        menu.add(btnMenuPrincipal);
        menu.add(btnArtistas);
        menu.add(btnAlbumes);
        menu.add(btnFavoritos);
        menu.add(btnPerfil);

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

        btnFavoritos.addActionListener(e -> {
            try {
                new frmFavoritos().setVisible(true);
            } catch (PersistenciaException ex) {
                System.getLogger(frmPerfil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            dispose();
        });

        btnPerfil.addActionListener(e -> cargarDatosUsuario());

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

        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBounds(240, 400, 180, 40);
        btnCerrarSesion.setBackground(new Color(120, 20, 20));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        getContentPane().add(btnCerrarSesion);

        btnCargaMasiva = new JButton("Cargar datos de ejemplo");
        btnCargaMasiva.setBounds(450, 400, 220, 40);
        btnCargaMasiva.setBackground(new Color(35, 35, 35));
        btnCargaMasiva.setForeground(Color.WHITE);
        btnCargaMasiva.setFocusPainted(false);
        getContentPane().add(btnCargaMasiva);

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

        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        btnCargaMasiva.addActionListener(e -> cargarDatosMasivos());
    }

    private void cargarDatosMasivos() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Esto insertará un catálogo de ejemplo (artistas, bandas, integrantes,\n"
                + "álbumes y canciones) en la base de datos. ¿Deseas continuar?",
                "Carga masiva de datos",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        btnCargaMasiva.setEnabled(false);
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

        try {
            CargaMasivaBO cargaMasivaBO = new CargaMasivaBO();
            String resultado = cargaMasivaBO.ejecutarCargaMasiva();
            JOptionPane.showMessageDialog(this, resultado, "Carga masiva de datos", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la carga masiva", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnCargaMasiva.setEnabled(true);
            setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        Sesion.cerrarSesion();
        frmMenuPrinicipal.establecerUsuarioActual(null);
        new frmLogin().setVisible(true);
        dispose();
    }

    private JButton crearBotonMenu(String texto, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(20, y, 140, 45);
        boton.setBackground(Color.BLACK);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Dialog", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(140, 140, 140)));
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