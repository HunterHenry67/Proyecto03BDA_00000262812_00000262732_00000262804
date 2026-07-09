/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Interfaces.IArtistaBO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author BALAMRUSH
 */
public class frmMenuPrincipal extends javax.swing.JFrame {

    private IArtistaBO artistaBO;
    private JPanel panelMenu;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    public frmMenuPrincipal() {
        this.artistaBO = artistaBO;
        setTitle("Biblioteca Musical");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        crearMenuLateral();
        crearPanelContenido();
        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
        mostrarArtistas();
    }

    private void crearMenuLateral() {
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(220, 750));
        panelMenu.setBackground(new Color(25, 25, 25));
        panelMenu.setLayout(null);
        JButton btnArtistas = crearBoton("Artistas", 30);
        JButton btnAlbumes = crearBoton("Álbumes", 90);
        JButton btnCanciones = crearBoton("Canciones", 150);
        JButton btnFavoritos = crearBoton("Favoritos", 210);
        JButton btnPerfil = crearBoton("Perfil", 270);
        btnArtistas.addActionListener(e -> mostrarArtistas());
        btnAlbumes.addActionListener(e -> mostrarAlbumes());
        btnCanciones.addActionListener(e -> mostrarCanciones());
        panelMenu.add(btnArtistas);
        panelMenu.add(btnAlbumes);
        panelMenu.add(btnCanciones);
        panelMenu.add(btnFavoritos);
        panelMenu.add(btnPerfil);
    }
    private JButton crearBoton(String texto, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(20, y, 180, 40);
        boton.setFocusPainted(false);
        return boton;
    }

    private void crearPanelContenido() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
    }

    private void mostrarArtistas() {
        panelContenido.add(new PanelArtistas(artistaBO), "ARTISTAS");
        cardLayout.show(panelContenido, "ARTISTAS");
    }

    private void mostrarAlbumes() {
        panelContenido.add(new PanelAlbumes(artistaBO), "ALBUMES");
        cardLayout.show(panelContenido, "ALBUMES");
    }

    private void mostrarCanciones() {
        panelContenido.add(new PanelCanciones(artistaBO), "CANCIONES");
        cardLayout.show(panelContenido, "CANCIONES");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 801, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 738, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
