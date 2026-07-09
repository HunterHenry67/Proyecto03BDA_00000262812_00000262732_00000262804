/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Excepciones.PresentacionException;
import Interfaces.IUsuarioBO;
import Negocio.UsuarioBO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class frmEditarPerfil extends JFrame {

    private UsuarioDTO usuarioActual;
    private IUsuarioBO usuarioBO;

    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JLabel lblImagenPerfil;

    private JButton btnSeleccionarImagen;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private String rutaImagenSeleccionada;

    public frmEditarPerfil() {
        this.usuarioActual = frmMenuPrinicipal.obtenerUsuarioActual();
        this.usuarioBO = new UsuarioBO();

        inicializarComponentes();
        cargarDatosUsuario();
    }

    private void inicializarComponentes() {
        setTitle("Editar Perfil");
        setSize(850, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(217, 217, 217));

        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBackground(new Color(55, 55, 55));
        menu.setBounds(0, 0, 180, 560);
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
        JButton btnSalir = crearBotonMenu("Salir", 450);

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
                System.getLogger(frmEditarPerfil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            dispose();
        });

        btnPerfil.addActionListener(e -> {
            new frmPerfil().setVisible(true);
            dispose();
        });

        btnSalir.addActionListener(e -> {
            new frmLogin().setVisible(true);
            dispose();
        });

        JLabel lblTitulo = new JLabel("Editar Perfil");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setBounds(230, 35, 300, 40);
        getContentPane().add(lblTitulo);

        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(250, 105, 160, 160);
        lblImagenPerfil.setBorder(new LineBorder(Color.GRAY, 2));
        lblImagenPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblImagenPerfil);

        btnSeleccionarImagen = new JButton("Seleccionar imagen");
        btnSeleccionarImagen.setBounds(245, 280, 170, 35);
        btnSeleccionarImagen.setBackground(new Color(35, 35, 35));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        getContentPane().add(btnSeleccionarImagen);

        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 18));
        lblUsuario.setBounds(460, 120, 250, 30);
        getContentPane().add(lblUsuario);

        txtNombreUsuario = new JTextField();
        txtNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombreUsuario.setBounds(460, 155, 260, 35);
        getContentPane().add(txtNombreUsuario);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setFont(new Font("Arial", Font.BOLD, 18));
        lblCorreo.setBounds(460, 215, 250, 30);
        getContentPane().add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCorreo.setBounds(460, 250, 260, 35);
        getContentPane().add(txtCorreo);

        btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setBounds(460, 350, 170, 40);
        btnGuardar.setBackground(new Color(35, 35, 35));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        getContentPane().add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(650, 350, 120, 40);
        btnCancelar.setBackground(new Color(35, 35, 35));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        getContentPane().add(btnCancelar);

        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnGuardar.addActionListener(e -> guardarCambios());

        btnCancelar.addActionListener(e -> {
            new frmPerfil().setVisible(true);
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
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No hay usuario iniciado.");
            new frmLogin().setVisible(true);
            dispose();
            return;
        }

        txtNombreUsuario.setText(usuarioActual.getNombreUsuario());
        txtCorreo.setText(usuarioActual.getCorreo());
        rutaImagenSeleccionada = usuarioActual.getImagenPerfil();

        cargarImagen(lblImagenPerfil, rutaImagenSeleccionada, 160, 160);
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar imagen de perfil");

        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            rutaImagenSeleccionada = archivo.getAbsolutePath();

            cargarImagen(lblImagenPerfil, rutaImagenSeleccionada, 160, 160);
        }
    }

    private void guardarCambios() {
        try {
            validarCampos();

            UsuarioDTO usuarioEditado = new UsuarioDTO();

            usuarioEditado.setId(usuarioActual.getId());
            usuarioEditado.setNombreUsuario(txtNombreUsuario.getText().trim());
            usuarioEditado.setCorreo(txtCorreo.getText().trim());
            usuarioEditado.setImagenPerfil(rutaImagenSeleccionada);

            /*
             * IMPORTANTE:
             * Si tu UsuarioDTO tiene contrasenaHash/sal o contrasena,
             * conserva los valores anteriores para no borrar la contraseña.
             */
            usuarioEditado.setContrasena(usuarioActual.getContrasena());

            usuarioBO.actualizarPerfil(usuarioEditado);

            frmMenuPrinicipal.establecerUsuarioActual(usuarioEditado);

            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");

            new frmPerfil().setVisible(true);
            dispose();

        } catch (PresentacionException | NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void validarCampos() throws PresentacionException {
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombreUsuario.isBlank()) {
            throw new PresentacionException("Debe ingresar un nombre de usuario.");
        }

        if (correo.isBlank()) {
            throw new PresentacionException("Debe ingresar un correo.");
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            throw new PresentacionException("Ingrese un correo válido.");
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
                icono = new ImageIcon(getClass().getResource("/" + ruta));
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