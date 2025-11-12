package controladores;

import Implementacion.ModeloCompraDetalleImpl;
import Implementacion.ModeloCompraImpl;
import Modelo.ModeloCompra;
import Modelo.ModeloCompraDetalle;
import Vistas.PanelCompras;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controlador para gestionar las operaciones de Compras desde PanelCompras.
 * Llama estos métodos desde los actionPerformed de tus botones en la vista.
 * Autor: Joshua Cirilo Alegría
 */
public class controladorCompras {
  
    private final PanelCompras vista;
    private final ModeloCompraImpl daoCompra;
    private final ModeloCompraDetalleImpl daoDetalle;
    private final SimpleDateFormat sdf;

    public controladorCompras (PanelCompras vista) {
        this.vista = vista;
        this.daoCompra = new ModeloCompraImpl();
        this.daoDetalle = new ModeloCompraDetalleImpl();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.sdf.setLenient(false);
    }

    // =====================================================
    // 🟩 SECCIÓN: COMPRAS
    // =====================================================

    public void agregarCompra() {
        try {
            ModeloCompra compra = construirCompraDesdeVista();
            boolean ok = daoCompra.agregar(compra);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Compra agregada correctamente (ID: " + compra.getIdCompra() + ")");
                limpiarVistaCompra();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al agregar compra", ex);
        }
    }

    public void actualizarCompra() {
        try {
            String idTxt = vista.getTxtIdCompra2().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Debes ingresar el ID de la compra para actualizar.");
                return;
            }
            ModeloCompra compra = construirCompraDesdeVista();
            compra.setIdCompra(parseLong(idTxt, "ID Compra"));
            boolean ok = daoCompra.actualizar(compra);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Compra actualizada correctamente.");
                limpiarVistaCompra();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al actualizar compra", ex);
        }
    }

    public void eliminarCompra() {
        try {
            String idTxt = vista.getTxtIdCompra2().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID de la compra a eliminar.");
                return;
            }
            long idCompra = parseLong(idTxt, "ID Compra");
            long idUsuario = parseLong(vista.getTxtIdUsuario().getText().trim(), "ID Usuario");

            boolean ok = daoCompra.eliminar(idCompra, idUsuario);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "🗑️ Compra eliminada (ID: " + idCompra + ")");
                limpiarVistaCompra();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al eliminar compra", ex);
        }
    }

    public void buscarCompra() {
        try {
            String idCompraTxt = vista.getTxtIdCompra2().getText().trim();
            String idProvTxt = vista.getTxtIdProveedor().getText().trim();

            ModeloCompra compra = null;
            if (!idCompraTxt.isEmpty()) {
                compra = daoCompra.buscarPorIdCompra(parseLong(idCompraTxt, "ID Compra"));
            } else if (!idProvTxt.isEmpty()) {
                compra = daoCompra.buscarPorIdProveedor((int) parseLong(idProvTxt, "ID Proveedor"));
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa ID de compra o ID de proveedor para buscar.");
                return;
            }

            if (compra != null) {
                volcarCompraEnVista(compra);
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró ninguna compra con esos datos.");
            }
        } catch (Exception ex) {
            showError("Error al buscar compra", ex);
        }
    }

    private ModeloCompra construirCompraDesdeVista() throws ParseException {
        ModeloCompra m = new ModeloCompra();

        m.setIdProveedor(parseLong(vista.getTxtIdProveedor().getText().trim(), "ID Proveedor"));
        m.setIdUsuario(parseLong(vista.getTxtIdUsuario().getText().trim(), "ID Usuario"));
        m.setTotalBruto(parseDouble(vista.getTxtTotalBruto().getText().trim(), "Total Bruto"));
        m.setFecha(parseDate(vista.getTxtFecha().getText().trim(), "Fecha"));
        m.setFechaCompra(parseDate(vista.getTxtFechaCompra().getText().trim(), "Fecha de Compra"));
        m.setDocumento(nullIfEmpty(vista.getTxtDocumento().getText()));
        m.setEstado(nullIfEmpty(vista.getTxtEstado().getText()));
        m.setFormaPago(vista.getCmbMetodoPago().getSelectedItem().toString());
        return m;
    }

    private void volcarCompraEnVista(ModeloCompra c) {
        vista.getTxtIdCompra2().setText(String.valueOf(c.getIdCompra()));
        vista.getTxtIdProveedor().setText(String.valueOf(c.getIdProveedor()));
        vista.getTxtFecha().setText(c.getFecha() != null ? sdf.format(c.getFecha()) : "");
        vista.getTxtDocumento().setText(c.getDocumento() != null ? c.getDocumento() : "");
        vista.getTxtTotalBruto().setText(String.valueOf(c.getTotalBruto()));
        vista.getTxtEstado().setText(c.getEstado() != null ? c.getEstado() : "");
        vista.getTxtIdUsuario().setText(String.valueOf(c.getIdUsuario()));
        vista.getTxtFechaCompra().setText(c.getFechaCompra() != null ? sdf.format(c.getFechaCompra()) : "");
        if (c.getFormaPago() != null) {
            vista.getCmbMetodoPago().setSelectedItem(c.getFormaPago());
        }
    }

    private void limpiarVistaCompra() {
        vista.getTxtIdCompra2().setText("");
        vista.getTxtIdProveedor().setText("");
        vista.getTxtFecha().setText("");
        vista.getTxtDocumento().setText("");
        vista.getTxtTotalBruto().setText("");
        vista.getTxtEstado().setText("");
        vista.getTxtIdUsuario().setText("");
        vista.getTxtFechaCompra().setText("");
        if (vista.getCmbMetodoPago().getItemCount() > 0) {
            vista.getCmbMetodoPago().setSelectedIndex(0);
        }
    }

    // =====================================================
    // 🟨 SECCIÓN: DETALLE DE COMPRA
    // =====================================================

    public void agregarDetalle() {
        try {
            ModeloCompraDetalle detalle = construirDetalleDesdeVista();
            boolean ok = daoDetalle.agregar(detalle);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle agregado correctamente (ID: " + detalle.getIdDetalle() + ")");
                limpiarVistaDetalle();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al agregar detalle", ex);
        }
    }

    public void actualizarDetalle() {
        try {
            String idTxt = vista.getTxtIdDetalleCompra().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID del detalle para actualizar.");
                return;
            }

            ModeloCompraDetalle detalle = construirDetalleDesdeVista();
            detalle.setIdDetalle(parseLong(idTxt, "ID Detalle Compra"));

            boolean ok = daoDetalle.actualizar(detalle);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle actualizado correctamente.");
                limpiarVistaDetalle();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al actualizar detalle", ex);
        }
    }

    public void eliminarDetalle() {
        try {
            String idTxt = vista.getTxtIdDetalleCompra().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID del detalle a eliminar.");
                return;
            }
            long id = parseLong(idTxt, "ID Detalle Compra");
            boolean ok = daoDetalle.eliminar(id, 1); // ⚠️ idUsuario fijo por ahora
            if (ok) {
                JOptionPane.showMessageDialog(vista, "🗑️ Detalle eliminado (ID: " + id + ")");
                limpiarVistaDetalle();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al eliminar detalle", ex);
        }
    }

    public void buscarDetalle() {
        try {
            String textoBusqueda = vista.getTxtbusqueda().getText().trim();
            if (textoBusqueda.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un ID de detalle para buscar.");
                return;
            }

            long idDetalle = parseLong(textoBusqueda, "ID Detalle Compra");
            ModeloCompraDetalle detalle = daoDetalle.buscarPorId(idDetalle);

            if (detalle != null) {
                volcarDetalleEnVista(detalle);
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró el detalle.");
            }
        } catch (Exception ex) {
            showError("Error al buscar detalle", ex);
        }
    }

    private ModeloCompraDetalle construirDetalleDesdeVista() {
        ModeloCompraDetalle d = new ModeloCompraDetalle();

        d.setIdCompra(parseLong(vista.getTxtIdCompra2().getText().trim(), "ID Compra"));
        d.setIdProducto(parseLong(vista.getTxtIdProducto().getText().trim(), "ID Producto"));
        d.setCantidad(parseBigDecimal(vista.getTxtCantidad().getText().trim(), "Cantidad"));
        d.setPrecio(parseBigDecimal(vista.getTxtPrecioCompra().getText().trim(), "Precio de Compra"));
        d.setDescuento(parseBigDecimal(vista.getTxtDescuento().getText().trim(), "Descuento"));
      //d.setImpuesto(parseBigDecimal(vista.getTxtImpuesto().getText().trim(), "Impuesto"));
        d.setTotal(parseBigDecimal(vista.getTxtTotal().getText().trim(), "Total"));
        return d;
    }

    private void volcarDetalleEnVista(ModeloCompraDetalle d) {
        vista.getTxtIdDetalleCompra().setText(String.valueOf(d.getIdDetalle()));
        vista.getTxtIdCompra2().setText(String.valueOf(d.getIdCompra()));
        vista.getTxtIdProducto().setText(String.valueOf(d.getIdProducto()));
        vista.getTxtCantidad().setText(d.getCantidad().toPlainString());
        vista.getTxtPrecioCompra().setText(d.getPrecio().toPlainString());
        vista.getTxtDescuento().setText(d.getDescuento().toPlainString());
       // vista.getTxtImpuesto().setText(d.getImpuesto().toPlainString());
        vista.getTxtTotal().setText(d.getTotal().toPlainString());
    }

    private void limpiarVistaDetalle() {
        vista.getTxtIdDetalleCompra().setText("");
        vista.getTxtIdCompra2().setText("");
        vista.getTxtIdProducto().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtPrecioCompra().setText("");
        vista.getTxtDescuento().setText("");
        vista.getTxtImpuesto().setText("");
        vista.getTxtTotal().setText("");
        //vista.getTxtBusqueda().setText("");
    }

    // =====================================================
    // 🧠 MÉTODOS AUXILIARES
    // =====================================================

    private long parseLong(String txt, String campo) {
        if (txt == null || txt.isEmpty())
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser numérico. Valor: " + txt);
        }
    }

    private double parseDouble(String txt, String campo) {
        if (txt == null || txt.isEmpty())
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        try {
            return Double.parseDouble(txt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser decimal. Valor: " + txt);
        }
    }

    private BigDecimal parseBigDecimal(String txt, String campo) {
        if (txt == null || txt.isEmpty())
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        try {
            return new BigDecimal(txt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser decimal. Valor: " + txt);
        }
    }

    private Date parseDate(String txt, String campo) throws ParseException {
        if (txt == null || txt.isEmpty())
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio. Formato esperado: yyyy-MM-dd");
        return sdf.parse(txt);
    }

    private String nullIfEmpty(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private void showError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(vista, "⚠️ " + titulo + ":\n" + ex.getMessage());
    }



  /*  private final PanelCompras vista;
    private final ModeloCompraImpl dao;
    private final SimpleDateFormat sdf;

    public controladorCompras(PanelCompras vista) {
        this.vista = vista;
        this.dao = new ModeloCompraImpl();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.sdf.setLenient(false);
    }

    // =========================
    // MÉTODOS PÚBLICOS (para botones)
    // =========================

    /** Agrega una compra tomando los datos de la vista */
   /* public void agregarCompra() {
        try {
            ModeloCompra compra = construirDesdeVista();
            boolean ok = dao.agregar(compra);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Compra agregada (ID: " + compra.getIdCompra() + ")");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al agregar", ex);
        }
    }

    /** Actualiza una compra existente (requiere ID_COMPRA en txtIdCuentaCobro1) */
    /*public void actualizarCompra() {
        try {
            String idTxt = vista.getTxtIdCuentaCobro1().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Debes ingresar el ID de la compra para actualizar.");
                return;
            }
            ModeloCompra compra = construirDesdeVista();
            compra.setIdCompra(parseLong(idTxt, "ID Compra"));
            boolean ok = dao.actualizar(compra);
            if (ok) {
                JOptionPane.showMessageDialog(vista, "✅ Compra actualizada.");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al actualizar", ex);
        }
    }
    /** Elimina una compra por ID_COMPRA (usa txtIdCuentaCobro1) */
   /* public void eliminarCompra() {
        try {
            String idTxt = vista.getTxtIdCuentaCobro1().getText().trim();
            if (idTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID de la compra a eliminar.");
                return;
            }

            long id = parseLong(idTxt, "ID Compra");
            long idUsuario = parseLong(vista.getTxtIdUsuario().getText().trim(), "ID Usuario"); // ✅ añadido

            boolean ok = dao.eliminar(id, idUsuario); // ✅ corregido
            if (ok) {
                JOptionPane.showMessageDialog(vista, "🗑️ Compra eliminada (ID: " + id + ")");
                limpiarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar la compra.");
            }
        } catch (Exception ex) {
            showError("Error al eliminar", ex);
        }
    }


    /**
     * Busca por ID_COMPRA (si está lleno txtIdCuentaCobro1) o por ID_PROVEEDOR (si está vacío el anterior
     * y está lleno txtIdProveedor). Muestra los datos en la vista si existe.
     */
   /* public void buscarCompra() {
        try {
            String idCompraTxt = vista.getTxtIdCuentaCobro1().getText().trim();
            String idProvTxt   = vista.getTxtIdProveedor().getText().trim();

            ModeloCompra compra = null;

            if (!idCompraTxt.isEmpty()) {
                long idCompra = parseLong(idCompraTxt, "ID Compra");
                compra = dao.buscarPorIdCompra(idCompra);
            } else if (!idProvTxt.isEmpty()) {
                long idProv = parseLong(idProvTxt, "ID Proveedor");
                // Nota: si hay varias compras por proveedor, este método trae la primera que encuentre.
                // Puedes extender el DAO para listar por proveedor si lo necesitas.
                compra = dao.buscarPorIdProveedor((int) idProv); // si tu dao usa int aquí
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa ID de compra o ID de proveedor para buscar.");
                return;
            }

            if (compra != null) {
                volcarModeloEnVista(compra);
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontraron datos con los criterios proporcionados.");
            }
        } catch (Exception ex) {
            showError("Error al buscar", ex);
        }
    }

    /** Limpia todos los campos en la vista */
    /*public void limpiarVista() {
        vista.getTxtIdCuentaCobro1().setText("");
        vista.getTxtIdProveedor().setText("");
        vista.getTxtFecha().setText("");
        vista.getTxtDocumento().setText("");
        vista.getTxtTotalBruto().setText("");
        vista.getTxtEstado().setText("");
        vista.getTxtIdUsuario().setText("");
        vista.getTxtFechaCompra().setText("");
        if (vista.getCmbMetodoPago().getItemCount() > 0) {
            vista.getCmbMetodoPago().setSelectedIndex(0);
        }
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private ModeloCompra construirDesdeVista() throws ParseException {
        ModeloCompra m = new ModeloCompra();

        // Obligatorios numéricos
        long idProv = parseLong(vista.getTxtIdProveedor().getText().trim(), "ID Proveedor");
        long idUser = parseLong(vista.getTxtIdUsuario().getText().trim(), "ID Usuario");
        double total = parseDouble(vista.getTxtTotalBruto().getText().trim(), "Total Bruto");

        // Fechas (yyyy-MM-dd)
        Date fecha = parseDate(vista.getTxtFecha().getText().trim(), "Fecha");
        Date fechaCompra = parseDate(vista.getTxtFechaCompra().getText().trim(), "Fecha de Compra");

        // Texto
        String documento = nullIfEmpty(vista.getTxtDocumento().getText());
        String estado    = nullIfEmpty(vista.getTxtEstado().getText());
        String formaPago = (vista.getCmbMetodoPago().getSelectedItem() != null)
                ? vista.getCmbMetodoPago().getSelectedItem().toString()
                : null;

        m.setIdProveedor(idProv);
        m.setIdUsuario(idUser);
        m.setTotalBruto(total);
        m.setFecha(fecha);
        m.setFechaCompra(fechaCompra);
        m.setDocumento(documento);
        m.setEstado(estado);
        m.setFormaPago(formaPago);

        return m;
    }

    private void volcarModeloEnVista(ModeloCompra c) {
        vista.getTxtIdCuentaCobro1().setText(String.valueOf(c.getIdCompra()));
        vista.getTxtIdProveedor().setText(String.valueOf(c.getIdProveedor()));
        vista.getTxtFecha().setText(c.getFecha() != null ? sdf.format(c.getFecha()) : "");
        vista.getTxtDocumento().setText(c.getDocumento() != null ? c.getDocumento() : "");
        vista.getTxtTotalBruto().setText(String.valueOf(c.getTotalBruto()));
        vista.getTxtEstado().setText(c.getEstado() != null ? c.getEstado() : "");
        vista.getTxtIdUsuario().setText(String.valueOf(c.getIdUsuario()));
        vista.getTxtFechaCompra().setText(c.getFechaCompra() != null ? sdf.format(c.getFechaCompra()) : "");
        if (c.getFormaPago() != null) {
            vista.getCmbMetodoPago().setSelectedItem(c.getFormaPago());
        }
    }

    // ---- Parsers seguros ----
    private long parseLong(String txt, String campo) {
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser numérico. Valor: " + txt);
        }
    }

    private double parseDouble(String txt, String campo) {
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            return Double.parseDouble(txt);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser numérico. Valor: " + txt);
        }
    }

    private Date parseDate(String txt, String campo) throws ParseException {
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio. Formato: yyyy-MM-dd");
        }
        return sdf.parse(txt);
    }

    private String nullIfEmpty(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private void showError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(vista, "⚠️ " + titulo + ":\n" + ex.getMessage());
    }*/
}
