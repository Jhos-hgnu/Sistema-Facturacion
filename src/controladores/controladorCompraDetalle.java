/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import Implementacion.ModeloCompraDetalleImpl;
import Modelo.ModeloCompraDetalle;
import Vistas.PanelDetalleCompra;

import javax.swing.JOptionPane;
import java.math.BigDecimal;

/**
 * Controlador para la gestión del detalle de compras (COMPRA_DETALLE). Controla
 * las acciones del PanelCompraDetalle y delega las operaciones al DAO. Autor:
 * Joshua Cirilo Alegría
 */
public class controladorCompraDetalle {

    private final PanelDetalleCompra vista;
    private final ModeloCompraDetalleImpl dao;

    public controladorCompraDetalle(PanelDetalleCompra vista) {
        this.vista = vista;
        this.dao = new ModeloCompraDetalleImpl();
    }

    // ======================================================
    // ➕ AGREGAR
    // ======================================================
    public void agregarDetalle() {
        try {
            ModeloCompraDetalle detalle = construirDesdeVista();

            boolean ok = dao.agregar(detalle);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle agregado correctamente (ID: " + detalle.getIdDetalle() + ")");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al agregar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al agregar detalle", ex);
        }
    }

    // ======================================================
    // ✏️ ACTUALIZAR
    // ======================================================
    public void actualizarDetalle() {
        try {
            String idTxt = vista.getTxtIdDetalleCompra().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Debes ingresar el ID del detalle para actualizar.");
                return;
            }

            ModeloCompraDetalle detalle = construirDesdeVista();
            detalle.setIdDetalle(Long.parseLong(idTxt));

            boolean ok = dao.actualizar(detalle);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle actualizado correctamente.");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al actualizar detalle", ex);
        }
    }

    // ======================================================
    // ❌ ELIMINAR
    // ======================================================
    public void eliminarDetalle() {
        try {
            String idTxt = vista.getTxtIdDetalleCompra().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID del detalle a eliminar.");
                return;
            }

            long id = Long.parseLong(idTxt);
            long idUsuario = obtenerIdUsuario(); // ⚙️ Aquí puedes enlazar con sesión si la manejas.

            boolean ok = dao.eliminar(id, idUsuario);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "🗑️ Detalle eliminado (ID: " + id + ")");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al eliminar detalle", ex);
        }
    }

    // ======================================================
    // 🧩 MÉTODOS AUXILIARES
    // ======================================================
    /**
     * Construye un objeto ModeloCompraDetalle a partir de los campos de la
     * vista
     */
    private ModeloCompraDetalle construirDesdeVista() {
        ModeloCompraDetalle d = new ModeloCompraDetalle();

        d.setIdCompra(parseLong(vista.getTxtIdCompra().getText().trim(), "ID Compra"));
        d.setIdProducto(parseLong(vista.getTxtIdProducto().getText().trim(), "ID Producto"));

        d.setCantidad(parseBigDecimal(vista.getTxtCantidad().getText().trim(), "Cantidad"));
        d.setPrecio(parseBigDecimal(vista.getTxtPrecioCompra().getText().trim(), "Precio"));
        d.setDescuento(parseBigDecimal(vista.getTxtDescuento().getText().trim(), "Descuento"));
        d.setTotal(parseBigDecimal(vista.getTxtTotal().getText().trim(), "Total"));

        return d;
    }

    /**
     * Limpia todos los campos de la vista
     */
    public void limpiarVista() {
        vista.getTxtIdDetalleCompra().setText("");
        vista.getTxtIdCompra().setText("");
        vista.getTxtIdProducto().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtPrecioCompra().setText("");
        vista.getTxtDescuento().setText("");
        vista.getTxtTotal().setText("");
        vista.getTxtImpuesto().setText("");
    }

    // ======================================================
    // 🔹 PARSERS SEGUROS
    // ======================================================
    private long parseLong(String txt, String campo) {
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser numérico. Valor: " + txt);
        }
    }

    private BigDecimal parseBigDecimal(String txt, String campo) {
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            return new BigDecimal(txt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser decimal. Valor: " + txt);
        }
    }

    private void showError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(vista, "⚠️ " + titulo + ":\n" + ex.getMessage());
    }

    private long obtenerIdUsuario() {
        // ⚠️ Puedes conectar esto con la sesión del usuario o control superior.
        // Por ahora, devuelve un valor fijo para la auditoría.
        return 1;
    }

    public void buscarDetalle() {
        try {
            String textoBusqueda = vista.getTxtbusqueda().getText().trim();

            if (textoBusqueda.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un ID de detalle para buscar.");
                return;
            }

            long idDetalle = Long.parseLong(textoBusqueda);

            ModeloCompraDetalle detalle = dao.buscarPorId(idDetalle);

            if (detalle != null) {
                vista.getTxtIdDetalleCompra().setText(String.valueOf(detalle.getIdDetalle()));
                vista.getTxtIdCompra().setText(String.valueOf(detalle.getIdCompra()));
                vista.getTxtIdProducto().setText(String.valueOf(detalle.getIdProducto()));
                vista.getTxtCantidad().setText(detalle.getCantidad() != null ? detalle.getCantidad().toPlainString() : "");
                vista.getTxtPrecioCompra().setText(detalle.getPrecio() != null ? detalle.getPrecio().toPlainString() : "");
                vista.getTxtDescuento().setText(detalle.getDescuento() != null ? detalle.getDescuento().toPlainString() : "");
                vista.getTxtTotal().setText(detalle.getTotal() != null ? detalle.getTotal().toPlainString() : "");
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró ningún detalle con ese ID.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "⚠️ El ID debe ser un número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al buscar el detalle: " + e.getMessage());
        }
    }

}
