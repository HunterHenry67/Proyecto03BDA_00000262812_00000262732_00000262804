/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import DTO.FavoritoDTO;
import DTO.GeneroDTO;
import DTO.GeneroNoDeseadoDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Excepciones.PresentacionException;
import Interfaces.IUsuarioBO;
import Negocio.FavoritoBO;
import Negocio.GeneroBO;
import Interfaces.IFavoritoBO;
import Interfaces.IGeneroBO;
import Negocio.UsuarioBO;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class frmGenerosNoDeseados extends JFrame {

    private UsuarioDTO usuarioActual;

    private IUsuarioBO usuarioBO;
    private IGeneroBO generoBO;
    private IFavoritoBO favoritoBO;

    private JComboBox<GeneroDTO> cmbGeneros;
    private JTable tblGenerosNoDeseados;
    private DefaultTableModel modeloTabla;

    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnRegresar;

    public frmGenerosNoDeseados() throws PersistenciaException {
        this.usuarioActual = frmMenuPrinicipal.obtenerUsuarioActual();
        this.usuarioBO = new UsuarioBO();
        this.generoBO = new GeneroBO();
        this.favoritoBO = new FavoritoBO();

        inicializarComponentes();
        cargarGeneros();
        cargarGenerosNoDeseados();
    }

    private void inicializarComponentes() {
        setTitle("Géneros no deseados");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(217, 217, 217));

        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBackground(new Color(55, 55, 55));
        menu.setBounds(0, 0, 180, 620);
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
        JButton btnSalir = crearBotonMenu("Salir", 510);

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
                System.getLogger(frmGenerosNoDeseados.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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

        JLabel lblTitulo = new JLabel("Géneros no deseados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setBounds(220, 35, 400, 40);
        getContentPane().add(lblTitulo);

        JLabel lblInfo = new JLabel("Al agregar un género, se eliminarán favoritos relacionados.");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 15));
        lblInfo.setBounds(220, 80, 600, 30);
        getContentPane().add(lblInfo);

        JLabel lblGenero = new JLabel("Seleccionar género:");
        lblGenero.setFont(new Font("Arial", Font.BOLD, 17));
        lblGenero.setBounds(220, 130, 200, 30);
        getContentPane().add(lblGenero);

        cmbGeneros = new JComboBox<>();
        cmbGeneros.setBounds(220, 165, 260, 35);
        getContentPane().add(cmbGeneros);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(500, 165, 120, 35);
        btnAgregar.setBackground(new Color(35, 35, 35));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        getContentPane().add(btnAgregar);

        btnEliminar = new JButton("★ Eliminar seleccionado");
        btnEliminar.setBounds(640, 165, 200, 35);
        btnEliminar.setBackground(new Color(35, 35, 35));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        getContentPane().add(btnEliminar);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID Género", "Fecha agregación"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblGenerosNoDeseados = new JTable(modeloTabla);
        tblGenerosNoDeseados.setRowHeight(28);
        tblGenerosNoDeseados.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tblGenerosNoDeseados);
        scroll.setBounds(220, 230, 620, 250);
        getContentPane().add(scroll);

        btnRegresar = new JButton("Regresar al perfil");
        btnRegresar.setBounds(220, 510, 170, 40);
        btnRegresar.setBackground(new Color(35, 35, 35));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        getContentPane().add(btnRegresar);

        btnAgregar.addActionListener(e -> agregarGeneroNoDeseado());
        btnEliminar.addActionListener(e -> eliminarGeneroNoDeseado());

        btnRegresar.addActionListener(e -> {
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

    private void cargarGeneros() throws PersistenciaException {
        cmbGeneros.removeAllItems();
        List<GeneroDTO> generos = generoBO.consultarTodos();
        for (GeneroDTO genero : generos) {
            cmbGeneros.addItem(genero);
        }
    }

    private void cargarGenerosNoDeseados() {
        modeloTabla.setRowCount(0);

        if (usuarioActual == null) {
            mostrarError("No hay usuario iniciado.");
            return;
        }

        try {
            List<GeneroNoDeseadoDTO> generos =
                    usuarioBO.consultarGenerosNoDeseados(usuarioActual.getId());

            for (GeneroNoDeseadoDTO genero : generos) {
                modeloTabla.addRow(new Object[]{
                    genero.getIdGenero(),
                    genero.getFechaAgregacion()
                });
            }

        } catch (NegocioException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void agregarGeneroNoDeseado() {
        try {
            validarUsuario();
            validarSeleccionGenero();

            GeneroDTO genero = (GeneroDTO) cmbGeneros.getSelectedItem();

            List<FavoritoDTO> favoritos = favoritoBO.buscarFavoritos(
                    usuarioActual.getId(),
                    "",
                    "TODOS",
                    genero.getId()
            );

            String mensaje;

            if (favoritos == null || favoritos.isEmpty()) {
                mensaje = "¿Deseas agregar este género a no deseados?";
            } else {
                mensaje = "Tienes " + favoritos.size()
                        + " favorito(s) de este género.\n"
                        + "Si continúas, se eliminarán de favoritos.\n\n"
                        + "¿Deseas continuar?";
            }

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    mensaje,
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }

            GeneroNoDeseadoDTO dto = new GeneroNoDeseadoDTO();
            dto.setIdGenero(genero.getId());
            dto.setFechaAgregacion(LocalDate.now());

            usuarioBO.agregarGeneroNoDeseado(usuarioActual.getId(), dto);

            favoritoBO.eliminarFavoritosPorGenero(
                    usuarioActual.getId(),
                    genero.getId()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Género agregado correctamente."
            );

            cargarGenerosNoDeseados();

        } catch (PresentacionException | NegocioException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarGeneroNoDeseado() {
        try {
            validarUsuario();

            int fila = tblGenerosNoDeseados.getSelectedRow();

            if (fila == -1) {
                throw new PresentacionException("Seleccione un género de la tabla.");
            }

            String idGenero = modeloTabla.getValueAt(fila, 0).toString();

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas quitar este género de no deseados?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }

            usuarioBO.eliminarGeneroNoDeseado(
                    usuarioActual.getId(),
                    idGenero
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Género eliminado correctamente."
            );

            cargarGenerosNoDeseados();

        } catch (PresentacionException | NegocioException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void validarUsuario() throws PresentacionException {
        if (usuarioActual == null || usuarioActual.getId() == null) {
            throw new PresentacionException("No hay usuario iniciado.");
        }
    }

    private void validarSeleccionGenero() throws PresentacionException {
        if (cmbGeneros.getSelectedItem() == null) {
            throw new PresentacionException("Seleccione un género.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}