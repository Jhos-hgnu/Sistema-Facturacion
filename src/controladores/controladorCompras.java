package controladores;

import Implementacion.ModeloCompraImpl;
import Modelo.ModeloCompra;
import Vistas.PanelCompras;

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
    public void agregarCompra() {
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
    public void actualizarCompra() {
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
    public void eliminarCompra() {
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
    public void buscarCompra() {
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
    public void limpiarVista() {
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
    }
}
