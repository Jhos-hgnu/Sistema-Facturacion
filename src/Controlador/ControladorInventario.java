/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conector.DBConnection;
import Implementacion.InventarioDAO;
import Implementacion.ProductoImp;
import Interfaces.Iinventario;
import Modelo.ModeloInventario;
import Modelo.ModeloProducto;
import Vistas.PanelInventario;
import Vistas.PanelMostrarInventario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;


/**
 * Controlador para gestionar acciones del inventario
 * Compatible con InventarioDAO y PanelMostrarInventario
 * @author Luis
 */
public class ControladorInventario {

    private final PanelMostrarInventario vista;
    private final InventarioDAO dao;
    private ModeloInventario modelo;

    public ControladorInventario(PanelMostrarInventario vista) {
        this.vista = vista;
        this.dao = new InventarioDAO();

        // === Eventos de botones ===
        this.vista.btnRegistroInventario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarInventario();
            }
        });

        if (vista.btnAgregar != null) {
            this.vista.btnAgregar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    agregarInventario();
                }
            });
        }

        if (vista.btnActualizar != null) {
            this.vista.btnActualizar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    actualizarInventario();
                }
            });
        }

        if (vista.btnEliminar != null) {
            this.vista.btnEliminar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    eliminarInventario();
                }
            });
        }

        if (vista.btnBuscar != null) {
            this.vista.btnBuscar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buscarInventario();
                }
            });
        }
    }

    // === MOSTRAR TODOS LOS REGISTROS ===
    private void mostrarInventario() {
        List<ModeloInventario> lista = dao.listarInventario();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "ID", "Producto", "Cantidad", "Stock Anterior", "Stock Actual",
            "Movimiento", "Motivo", "Usuario", "Fecha"
        });

        for (ModeloInventario inv : lista) {
            model.addRow(new Object[]{
                inv.getIdInventario(),
                inv.getIdProducto(),
                inv.getCantidad(),
                inv.getStockAnterior(),
                inv.getStockActual(),
                inv.getMovimiento(),
                inv.getMotivo(),
                inv.getIdUsuario(),
                inv.getFecha()
            });
        }

        vista.tblInventario.setModel(model);
    }

    // === AGREGAR NUEVO REGISTRO ===
    private void agregarInventario() {
        try {
            modelo = new ModeloInventario();
            modelo.setIdProducto(Long.parseLong(vista.txtIdProducto.getText()));
            modelo.setCantidad(Integer.parseInt(vista.txtCantidad.getText()));
            modelo.setStockAnterior(Integer.parseInt(vista.txtStockAnterior.getText()));
            modelo.setStockActual(Integer.parseInt(vista.txtStockActual.getText()));
            modelo.setMovimiento(vista.txtMovimiento.getText());
            modelo.setMotivo(vista.txtMotivo.getText());
            modelo.setIdUsuario(Long.parseLong(vista.txtIdUsuario.getText()));
            modelo.setFecha(new Date(System.currentTimeMillis())); // Fecha actual

            boolean exito = dao.insertarInventario(modelo);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Inventario agregado correctamente.");
                mostrarInventario();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar el inventario.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Verifica que los campos numéricos sean válidos.");
        }
    }

    // === ACTUALIZAR REGISTRO EXISTENTE ===
    private void actualizarInventario() {
        try {
            modelo = new ModeloInventario();
            modelo.setIdInventario(Long.parseLong(vista.txtIdInventario.getText()));
            modelo.setIdProducto(Long.parseLong(vista.txtIdProducto.getText()));
            modelo.setCantidad(Integer.parseInt(vista.txtCantidad.getText()));
            modelo.setStockAnterior(Integer.parseInt(vista.txtStockAnterior.getText()));
            modelo.setStockActual(Integer.parseInt(vista.txtStockActual.getText()));
            modelo.setMovimiento(vista.txtMovimiento.getText());
            modelo.setMotivo(vista.txtMotivo.getText());
            modelo.setIdUsuario(Long.parseLong(vista.txtIdUsuario.getText()));
            modelo.setFecha(new Date(System.currentTimeMillis()));

            boolean exito = dao.actualizarInventario(modelo);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Inventario actualizado correctamente.");
                mostrarInventario();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar el inventario.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa valores numéricos válidos para actualizar.");
        }
    }

    // === ELIMINAR REGISTRO ===
    private void eliminarInventario() {
        try {
            long id = Long.parseLong(vista.txtIdInventario.getText());

            int confirm = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Seguro que deseas eliminar este registro?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = dao.eliminarInventario(id);
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "🗑️ Registro eliminado correctamente.");
                    mostrarInventario();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar el registro.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un ID válido para eliminar.");
        }
    }

    // === BUSCAR POR ID ===
    private void buscarInventario() {
        try {
            long id = Long.parseLong(vista.txtIdInventario.getText());
            ModeloInventario inv = dao.buscarPorId(id);

            if (inv != null) {
                vista.txtIdProducto.setText(String.valueOf(inv.getIdProducto()));
                vista.txtCantidad.setText(String.valueOf(inv.getCantidad()));
                vista.txtStockAnterior.setText(String.valueOf(inv.getStockAnterior()));
                vista.txtStockActual.setText(String.valueOf(inv.getStockActual()));
                vista.txtMovimiento.setText(inv.getMovimiento());
                vista.txtMotivo.setText(inv.getMotivo());
                vista.txtIdUsuario.setText(String.valueOf(inv.getIdUsuario()));
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se encontró el inventario con ese ID.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un ID válido para buscar.");
        }
    }

    // === LIMPIAR CAMPOS ===
    private void limpiarCampos() {
        vista.txtIdInventario.setText("");
        vista.txtIdProducto.setText("");
        vista.txtCantidad.setText("");
        vista.txtStockAnterior.setText("");
        vista.txtStockActual.setText("");
        vista.txtMovimiento.setText("");
        vista.txtMotivo.setText("");
        vista.txtIdUsuario.setText("");
    }
}
