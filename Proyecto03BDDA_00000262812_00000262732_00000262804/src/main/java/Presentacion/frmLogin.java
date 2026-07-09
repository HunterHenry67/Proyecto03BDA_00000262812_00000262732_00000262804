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
import javax.swing.*;

/**
 *
 * @author Andre
 */

public class frmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnRegistrarse;

    private final IUsuarioBO usuarioBO;

    public frmLogin() {
        this.usuarioBO = new UsuarioBO();
        inicializarComponentes();
        eventos();
    }

    private void inicializarComponentes() {
        setTitle("Iniciar Sesión");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(217, 217, 217));
        panel.setLayout(null);
        add(panel);

        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setBounds(50, 30, 450, 40);
        panel.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:", SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 20));
        lblUsuario.setBounds(30, 110, 130, 30);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsuario.setBounds(180, 110, 300, 30);
        panel.add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:", SwingConstants.RIGHT);
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 20));
        lblContrasena.setBounds(30, 160, 130, 30);
        panel.add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
        txtContrasena.setBounds(180, 160, 300, 30);
        panel.add(txtContrasena);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(Color.DARK_GRAY);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBounds(230, 230, 110, 35);
        panel.add(btnIngresar);

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBackground(Color.DARK_GRAY);
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setBounds(360, 230, 120, 35);
        panel.add(btnRegistrarse);
        setVisible(true);
    }

    private void eventos() {
        btnIngresar.addActionListener(e -> iniciarSesion());

        btnRegistrarse.addActionListener(e -> {
            new frmRegistro().setVisible(true);
            dispose();
        });

        txtContrasena.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuarioOCorreo = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        try {
            UsuarioDTO usuario = usuarioBO.iniciarSesion(usuarioOCorreo, contrasena);

            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getNombreUsuario());

            // new frmMenuPrincipal(usuario).setVisible(true);

            dispose();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}