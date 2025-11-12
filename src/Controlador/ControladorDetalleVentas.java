/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.DetalleVentasDAO;
import Implementacion.InventarioDAO;
import Modelo.ModeloDetalleVenta;
import Modelo.ModeloInventario;
import Vistas.PanelDetalleVenta;
import Vistas.PanelVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author luisd
 */
public class ControladorDetalleVentas {

    private final PanelVentas vista;
    private final DetalleVentasDAO dao;
    private final InventarioDAO inventarioDAO = new InventarioDAO();

    public ControladorDetalleVentas(PanelVentas vista) {
        this.vista = vista;
        this.dao = new DetalleVentasDAO();

        // Evento para insertar detalle
        this.vista.btnHacerVenta.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                agregarDetalle();
            }
        }
        );

        this.vista.getBtnBuscarDetalle()
                .addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e
                    ) {
                        buscarDetalle();
                    }
                });

        vista.btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actualizarDetalleVenta();
            }
        });
        this.vista.btnEliminarVenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eliminarDetalleVenta();
            }
        });

    }

    private void eliminarDetalleVenta() {
        String idDetalleTexto = vista.getTxtIdDetalleVenta().getText().trim();

        if (idDetalleTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingrese el ID del detalle que desea eliminar.");
            return;
        }

        long idDetalle = Long.parseLong(idDetalleTexto);

        // Confirmación antes de eliminar
        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Seguro que deseas eliminar el detalle con ID " + idDetalle + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // 1️⃣ Buscar detalle antes de eliminar para saber qué producto y cantidad devolver al inventario
            ModeloDetalleVenta detalle = dao.buscarPorId(idDetalle);

            if (detalle == null) {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró el detalle a eliminar.");
                return;
            }

            // 2️⃣ Eliminar el detalle de venta
            boolean eliminado = dao.eliminarDetalleVenta(idDetalle);

            if (eliminado) {
                // 3️⃣ Devolver el stock al inventario
                long idProducto = detalle.getIdProducto();
                int cantidad = detalle.getCantidad();

                int stockActual = inventarioDAO.obtenerStockActual(idProducto);
                int nuevoStock = stockActual + cantidad;

                // Registrar el movimiento
                ModeloInventario movimiento = new ModeloInventario();
                movimiento.setIdProducto(idProducto);
                movimiento.setCantidad(cantidad);
                movimiento.setStockAnterior(stockActual);
                movimiento.setStockActual(nuevoStock);
                movimiento.setMovimiento("ENTRADA");
                movimiento.setMotivo("Eliminación de detalle de venta ID " + idDetalle);
                movimiento.setIdUsuario(1); // Aquí puedes poner el usuario logueado
                movimiento.setFecha(new java.util.Date());

                boolean movOk = inventarioDAO.insertarInventario(movimiento);

                if (movOk) {
                    JOptionPane.showMessageDialog(vista, "✅ Detalle eliminado y stock restaurado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(vista, "⚠️ Detalle eliminado, pero no se registró el movimiento en inventario.");
                }

                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se pudo eliminar el detalle.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "❌ Error al eliminar detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarDetalleVenta() {
        try {
            ModeloDetalleVenta detalle = new ModeloDetalleVenta();
            detalle.setIdDetalleVenta(Long.parseLong(vista.getTxtIdDetalleVenta().getText()));
            detalle.setIdVenta(Long.parseLong(vista.txtIdVenta.getText()));
            detalle.setIdProducto(Long.parseLong(vista.txtIdProducto.getText()));
            detalle.setCantidad(Integer.parseInt(vista.txtCantidad.getText()));
            detalle.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText()));
            detalle.setDescuento(Double.parseDouble(vista.txtDescuento.getText()));

            boolean actualizado = dao.actualizarDetalleVenta(detalle);

            if (actualizado) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle de venta actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se pudo actualizar el detalle.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al actualizar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

// === AGREGAR DETALLE ===
    private void agregarDetalle() {
        try {
            // Validar campos obligatorios
            if (vista.txtIdVenta.getText().trim().isEmpty()
                    || vista.txtIdProducto.getText().trim().isEmpty()
                    || vista.txtCantidad.getText().trim().isEmpty()
                    || vista.txtPrecioVenta.getText().trim().isEmpty()
                    || vista.txtDescuento.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(vista, "⚠️ Debe llenar todos los campos antes de agregar el detalle.");
                return;
            }

            // === 1. Crear detalle de venta ===
            ModeloDetalleVenta detalle = new ModeloDetalleVenta();
            detalle.setIdVenta(Long.parseLong(vista.txtIdVenta.getText().trim()));
            detalle.setIdProducto(Long.parseLong(vista.txtIdProducto.getText().trim()));
            detalle.setCantidad(Integer.parseInt(vista.txtCantidad.getText().trim()));
            detalle.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText().trim()));
            detalle.setDescuento(Double.parseDouble(vista.txtDescuento.getText().trim()));

            boolean exito = dao.insertarDetalleVenta(detalle);

            // === 2. Si se insertó correctamente, actualizamos el inventario ===
            if (exito) {
                long idProducto = detalle.getIdProducto();
                int cantidadVendida = detalle.getCantidad();

                // Obtener stock actual
                int stockActual = inventarioDAO.obtenerStockActual(idProducto);
                int nuevoStock = stockActual - cantidadVendida;

                if (nuevoStock < 0) {
                    JOptionPane.showMessageDialog(vista, "⚠️ No hay suficiente stock para este producto. Stock actual: " + stockActual);
                    return;
                }

                // Crear movimiento en inventario
                ModeloInventario movimiento = new ModeloInventario();
                movimiento.setIdProducto(idProducto);
                movimiento.setCantidad(cantidadVendida);
                movimiento.setStockAnterior(stockActual);
                movimiento.setStockActual(nuevoStock);
                movimiento.setMovimiento("SALIDA");
                movimiento.setMotivo("Venta ID " + detalle.getIdVenta());
                movimiento.setIdUsuario(1); // ID del usuario logueado
                movimiento.setFecha(new Date());

                boolean movOk = inventarioDAO.insertarInventario(movimiento);

                if (movOk) {
                    JOptionPane.showMessageDialog(vista, "✅ Detalle agregado y stock actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(vista, "⚠️ Detalle agregado, pero no se registró el movimiento en inventario.");
                }

                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar el detalle de venta.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "⚠️ Verifique que los campos numéricos contengan solo números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // === BUSCAR DETALLE POR ID ===
    private void buscarDetalle() {
        try {
            String idTexto = vista.getTxtIdDetalleVenta().getText().trim();
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese el ID del detalle a buscar.");
                return;
            }

            long idDetalle = Long.parseLong(idTexto);
            ModeloDetalleVenta detalle = dao.buscarPorId(idDetalle);

            if (detalle != null) {
                vista.txtIdVenta.setText(String.valueOf(detalle.getIdVenta()));
                vista.txtIdProducto.setText(String.valueOf(detalle.getIdProducto()));
                vista.txtCantidad.setText(String.valueOf(detalle.getCantidad()));
                vista.txtPrecioVenta.setText(String.valueOf(detalle.getPrecioVenta()));
                vista.txtDescuento.setText(String.valueOf(detalle.getDescuento()));

                JOptionPane.showMessageDialog(vista, "✅ Detalle encontrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se encontró ningún detalle con ese ID.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "⚠️ El ID debe ser numérico.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al buscar detalle: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // === LIMPIAR CAMPOS ===
    private void limpiarCampos() {
        vista.txtIdProducto.setText("");
        vista.txtCantidad.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtDescuento.setText("");
    }
}
