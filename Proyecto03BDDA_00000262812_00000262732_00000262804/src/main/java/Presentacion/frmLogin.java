/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Andre
 */

public class frmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnRegistrarse;

    public frmLogin() {
        setTitle("Iniciar Sesión");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(217, 217, 217));
        panel.setLayout(null); 
        add(panel);

        JLabel lblTitulo = new JLabel("Iniciar Sesion", SwingConstants.CENTER);
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

        JLabel lblPassword = new JLabel("Contraseña:", SwingConstants.RIGHT);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 20));
        lblPassword.setBounds(30, 160, 130, 30);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBounds(180, 160, 300, 30);
        panel.add(txtPassword);

        Color colorBotones = new Color(45, 45, 45);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(colorBotones);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBounds(230, 230, 110, 35);
        panel.add(btnIngresar);

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBackground(colorBotones);
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setBounds(360, 230, 120, 35);
        panel.add(btnRegistrarse);
        
        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmRegistro ventanaRegistro = new frmRegistro();
                ventanaRegistro.setVisible(true);
                dispose();
            }
        });  
        
        setVisible(true);
    }

}