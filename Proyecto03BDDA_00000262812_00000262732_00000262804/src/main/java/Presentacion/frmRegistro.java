/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Interfaces.IUsuarioBO;
import Negocio.UsuarioBO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import javax.swing.*;


/**
 *
 * @author Andre
 */


public class frmRegistro extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JTextField txtCorreo;
    private JButton btnRegresar;
    private JButton btnRegistrarse;
    private JButton btnFoto;

    private String rutaImagenSeleccionada = "";

    private final IUsuarioBO usuarioBO;

    public frmRegistro() {
        this.usuarioBO = new UsuarioBO();
        inicializarComponentes();
        eventos();
    }

    private void inicializarComponentes() {
        setTitle("Registro de Usuario");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(null);
        add(panel);

        JLabel lblTitulo = new JLabel("Registro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBounds(50, 25, 550, 40);
        panel.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:", SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 22));
        lblUsuario.setBounds(30, 95, 130, 30);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsuario.setBounds(180, 95, 200, 35);
        panel.add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:", SwingConstants.RIGHT);
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 22));
        lblContrasena.setBounds(30, 145, 130, 30);
        panel.add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
        txtContrasena.setBounds(180, 145, 200, 35);
        panel.add(txtContrasena);

        JLabel lblCorreo = new JLabel("Correo:", SwingConstants.RIGHT);
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 22));
        lblCorreo.setBounds(30, 195, 130, 30);
        panel.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCorreo.setBounds(180, 195, 200, 35);
        panel.add(txtCorreo);

        JLabel lblFotoTitulo = new JLabel("Foto de perfil:", SwingConstants.CENTER);
        lblFotoTitulo.setFont(new Font("Arial", Font.PLAIN, 22));
        lblFotoTitulo.setBounds(410, 85, 180, 30);
        panel.add(lblFotoTitulo);

        btnFoto = new JButton("Clic aquí");
        btnFoto.setBounds(445, 125, 110, 110);
        btnFoto.setBackground(Color.WHITE);
        btnFoto.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnFoto.setFocusPainted(false);
        panel.add(btnFoto);

        JLabel lblOpcional = new JLabel("(Opcional)", SwingConstants.CENTER);
        lblOpcional.setFont(new Font("Arial", Font.PLAIN, 20));
        lblOpcional.setBounds(410, 245, 180, 30);
        panel.add(lblOpcional);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(Color.DARK_GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegresar.setBounds(340, 300, 120, 40);
        panel.add(btnRegresar);

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBackground(Color.DARK_GRAY);
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegistrarse.setBounds(480, 300, 120, 40);
        panel.add(btnRegistrarse);
    }

    private void eventos() {
        btnFoto.addActionListener(e -> cargarFoto());

        btnRegresar.addActionListener(e -> {
            new frmLogin().setVisible(true);
            dispose();
        });

        btnRegistrarse.addActionListener(e -> registrarUsuario());

        txtContrasena.addActionListener(e -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombreUsuario = txtUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombreUsuario(nombreUsuario);
        usuarioDTO.setCorreo(correo);
        usuarioDTO.setContrasena(contrasena);
        usuarioDTO.setImagenPerfil(rutaImagenSeleccionada);

        try {
            usuarioBO.registrar(usuarioDTO);

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");

            new frmLogin().setVisible(true);
            dispose();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFoto() {
        JFileChooser archivo = new JFileChooser();
        archivo.setDialogTitle("Seleccione su foto de perfil");

        int ventana = archivo.showOpenDialog(this);

        if (ventana == JFileChooser.APPROVE_OPTION) {
            File file = archivo.getSelectedFile();
            rutaImagenSeleccionada = file.getAbsolutePath();

            ImageIcon imagenOriginal = new ImageIcon(rutaImagenSeleccionada);
            Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(
                    btnFoto.getWidth(),
                    btnFoto.getHeight(),
                    Image.SCALE_SMOOTH
            );

            btnFoto.setText("");
            btnFoto.setIcon(new ImageIcon(imagenEscalada));
        }
    }
}