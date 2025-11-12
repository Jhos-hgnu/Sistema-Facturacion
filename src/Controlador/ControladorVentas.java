/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.DetalleVentasDAO;
import Implementacion.InventarioDAO;
import Implementacion.VentasDAO;
import Modelo.ModeloClientesVentas;
import Modelo.ModeloDetalleVenta;
import Modelo.ModeloInicioUsuario;
import Modelo.ModeloInventario;
import Modelo.ModeloProducto;
import Modelo.ModeloRegistroCliente;
import Modelo.ModeloVenta;
import Modelo.ModeloVistaInicio;
import Vistas.PanelVentas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhosu
 */
public class ControladorVentas {

    private final PanelVentas vista;
    private final VentasDAO dao = new VentasDAO();
    private final DetalleVentasDAO daoDetalle = new DetalleVentasDAO();

    public ControladorVentas(PanelVentas vista) {
        this.vista = vista;

        // Botón guardar venta
        vista.btnHacerVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                guardarVenta();
            }
        });

        // Botón limpiar
        vista.btnEliminarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                limpiarCampos();
            }
        });

        // Botón buscar venta
        vista.btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                buscarVentaPorId();
            }
        });
        vista.btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actualizarVenta();
            }
        });
        // Evento del botón eliminar
        this.vista.btnEliminarVenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eliminarVenta();
            }
        });
    }

    // ====================================================
    // ELIMINAR VENTA
    // ====================================================
    private void eliminarVenta() {
        try {
            String idTexto = vista.txtIdVenta.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID de la venta a eliminar.");
                return;
            }

            long idVenta = Long.parseLong(idTexto);

            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Seguro que deseas eliminar la venta con ID " + idVenta + " y todos sus detalles?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // =============================
            // 1️⃣ Buscar detalles de la venta
            // =============================
            List<ModeloDetalleVenta> detalles = daoDetalle.listarPorIdVenta(idVenta);

            // =============================
            // 2️⃣ Eliminar cada detalle y restaurar stock
            // =============================
            InventarioDAO inventarioDAO = new InventarioDAO();
            boolean errorEnDetalles = false;

            for (ModeloDetalleVenta detalle : detalles) {
                long idProducto = detalle.getIdProducto();
                int cantidad = detalle.getCantidad();

                // Recuperar stock actual
                int stockActual = inventarioDAO.obtenerStockActual(idProducto);
                int nuevoStock = stockActual + cantidad;

                // Registrar el movimiento de "entrada" (devolución al inventario)
                ModeloInventario movimiento = new ModeloInventario();
                movimiento.setIdProducto(idProducto);
                movimiento.setCantidad(cantidad);
                movimiento.setStockAnterior(stockActual);
                movimiento.setStockActual(nuevoStock);
                movimiento.setMovimiento("ENTRADA");
                movimiento.setMotivo("Eliminación de venta ID " + idVenta);
                movimiento.setIdUsuario(1);
                movimiento.setFecha(new java.util.Date());

                boolean movOk = inventarioDAO.insertarInventario(movimiento);

                if (!movOk) {
                    System.err.println("⚠️ No se registró movimiento de inventario para producto ID " + idProducto);
                    errorEnDetalles = true;
                }

                // Eliminar el detalle
                daoDetalle.eliminarDetalleVenta(detalle.getIdDetalleVenta());
            }

            // =============================
            // 3️⃣ Eliminar la venta principal
            // =============================
            boolean eliminado = dao.eliminarVenta(idVenta);

            if (eliminado) {
                String msg = "🗑️ Venta y detalles eliminados correctamente.";
                if (errorEnDetalles) {
                    msg += "\n⚠️ Algunos movimientos de inventario no se registraron correctamente.";
                }
                JOptionPane.showMessageDialog(vista, msg);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se pudo eliminar la venta.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al eliminar en cascada: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void actualizarVenta() {
        try {
            ModeloVenta venta = new ModeloVenta();
            venta.setIdVenta(Long.parseLong(vista.txtIdVenta.getText()));
            venta.setNit(vista.txtNIT.getText());
            venta.setFecha(new java.util.Date()); // o la fecha del campo
            venta.setTipoPago(vista.cmbMetodoPago.getSelectedItem().toString());
            venta.setDocumento(vista.txtDocumento.getText());
            venta.setTotal(Double.parseDouble(vista.txtTotal.getText()));
            venta.setIdUsuario(1); // o el usuario actual
            venta.setObservacion(vista.txtObservaciones.getText());

            boolean actualizado = dao.actualizarVenta(venta);

            if (actualizado) {
                JOptionPane.showMessageDialog(vista, "✅ Venta actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se pudo actualizar la venta.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al actualizar: " + ex.getMessage());
        }
    }

    // ====================================================
    // GUARDAR VENTA SIMPLE
    // ====================================================
    private void guardarVenta() {
        try {
            String nit = vista.txtNIT.getText().trim();
            String tipoPago = vista.cmbMetodoPago.getSelectedItem().toString();
            String observacion = vista.txtObservaciones.getText().trim();
            String totalTxt = vista.txtTotal.getText().trim();
            String documento = vista.txtDocumento.getText().trim();

            if (nit.isEmpty() || totalTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el NIT y el total de la venta.");
                return;
            }

            double total = Double.parseDouble(totalTxt);

            ModeloVenta venta = new ModeloVenta();
            venta.setNit(nit);
            venta.setFecha(new java.sql.Date(System.currentTimeMillis()));
            venta.setTipoPago(tipoPago);
            venta.setDocumento(documento.isEmpty() ? "VENTA SIN DETALLE" : documento);
            venta.setTotal(total);
            venta.setIdUsuario(1); // Cambiar según usuario logueado
            venta.setObservacion(observacion);

            boolean exito = dao.insertarVenta(venta);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Venta guardada correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al guardar la venta.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ El total debe ser un número válido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ====================================================
    // BUSCAR VENTA + DETALLES
    // ====================================================
    private void buscarVentaPorId() {
        try {
            String idTexto = vista.txtIdVenta.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID de la venta a buscar.");
                return;
            }

            long idVenta = Long.parseLong(idTexto);

            ModeloVenta venta = dao.obtenerVentaPorId(idVenta);

            if (venta != null) {
                // Llenar los campos de la vista con los datos
                vista.txtNIT.setText(venta.getNit());
                vista.txtDocumento.setText(venta.getDocumento());
                vista.txtTotal.setText(String.valueOf(venta.getTotal()));
                vista.txtObservaciones.setText(venta.getObservacion());
                vista.cmbMetodoPago.setSelectedItem(venta.getTipoPago());

                // Mostrar fecha (si existe)
                if (venta.getFecha() != null) {
                    vista.txtFecha.setText(venta.getFecha().toString());
                } else {
                    vista.txtFecha.setText("");
                }

                // === Cargar los detalles asociados en la tabla ===
                cargarDetallesEnTabla(idVenta);

                JOptionPane.showMessageDialog(vista, "✅ Venta encontrada y cargada correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró una venta con ese ID.");
                limpiarTablaDetalles();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ El ID debe ser un número válido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al buscar la venta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ====================================================
    // CARGAR DETALLES EN TABLA
    // ====================================================
    private void cargarDetallesEnTabla(long idVenta) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblDetalle().getModel();
        modelo.setRowCount(0); // limpiar tabla

        List<ModeloDetalleVenta> listaDetalles = daoDetalle.listarPorIdVenta(idVenta);

        for (ModeloDetalleVenta detalle : listaDetalles) {
            Object[] fila = {
                detalle.getIdDetalleVenta(),
                detalle.getNombreProducto(), // ✅ muestra nombre del producto
                detalle.getCantidad(),
                detalle.getPrecioVenta(),
                detalle.getDescuento()
            };
            modelo.addRow(fila);
        }
    }

    private void limpiarTablaDetalles() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblDetalle().getModel();
        modelo.setRowCount(0);
    }

    // ====================================================
    // LIMPIAR CAMPOS
    // ====================================================
    private void limpiarCampos() {
        vista.txtNIT.setText("");
        vista.txtDocumento.setText("");
        vista.txtObservaciones.setText("");
        vista.txtTotal.setText("");
        vista.cmbMetodoPago.setSelectedIndex(0);
        vista.txtFecha.setText("");
        limpiarTablaDetalles();
    }
}
