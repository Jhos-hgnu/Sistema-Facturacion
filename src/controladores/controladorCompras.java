package controladores;

import Implementacion.ModeloCompraImpl;
import Modelo.ModeloCompra;
import Vistas.PanelCompras;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Controlador para la gestión de compras.
 * @author Joshua
 */
public class controladorCompras {

    private final PanelCompras vista;
    private final ModeloCompraImpl dao;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Constructor
    public controladorCompras(PanelCompras vista) {
        this.vista = vista;
        this.dao = new ModeloCompraImpl();
    }

    // =========================
    // MÉTODOS PÚBLICOS CRUD
    // =========================

    public void agregarCompra() {
        try {
            ModeloCompra compra = construirCompraDesdeVista();
            boolean exito = dao.agregar(compra);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Compra agregada correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al agregar la compra.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al agregar: " + ex.getMessage());
        }
    }

    public void actualizarCompra() {
        try {
            ModeloCompra compra = construirCompraDesdeVista();
            compra.setIdCompra(Integer.parseInt(vista.getTxtIdCuentaCobro1().getText().trim()));
            boolean exito = dao.actualizar(compra);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Compra actualizada correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al actualizar la compra.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al actualizar: " + ex.getMessage());
        }
    }

    public void eliminarCompra() {
        try {
            int id = Integer.parseInt(vista.getTxtIdCuentaCobro1().getText().trim());
            boolean exito = dao.eliminar(id);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Compra eliminada correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar la compra.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al eliminar: " + ex.getMessage());
        }
    }

    // =========================
    // NUEVOS MÉTODOS
    // =========================

    /**
     * Busca una compra por ID_COMPRA o ID_PROVEEDOR
     * y muestra los datos en la vista.
     */
    public void buscarCompra() {
        try {
            String idCompraTxt = vista.getTxtIdCuentaCobro1().getText().trim();
            String idProveedorTxt = vista.getTxtIdProveedor().getText().trim();

            ModeloCompra compra = null;

            // Validar qué campo usar
            if (!idCompraTxt.isEmpty()) {
                int idCompra = Integer.parseInt(idCompraTxt);
                compra = dao.buscarPorIdCompra(idCompra);
            } else if (!idProveedorTxt.isEmpty()) {
                int idProveedor = Integer.parseInt(idProveedorTxt);
                compra = dao.buscarPorIdProveedor(idProveedor);
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el ID de compra o el ID de proveedor para buscar.");
                return;
            }

            // Mostrar datos si existen
            if (compra != null) {
                vista.getTxtIdCuentaCobro1().setText(String.valueOf(compra.getIdCompra()));
                vista.getTxtIdProveedor().setText(String.valueOf(compra.getIdProveedor()));
                vista.getTxtFecha().setText(sdf.format(compra.getFecha()));
                vista.getTxtDocumento().setText(compra.getDocumento());
                vista.getTxtTotalBruto().setText(String.valueOf(compra.getTotalBruto()));
                vista.getTxtEstado().setText(compra.getEstado());
                vista.getTxtIdUsuario().setText(String.valueOf(compra.getIdUsuario()));
                vista.getTxtFechaCompra().setText(sdf.format(compra.getFechaCompra()));
                vista.getCmbMetodoPago().setSelectedItem(compra.getFormaPago());
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se encontró ninguna compra con los datos proporcionados.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al buscar: " + ex.getMessage());
        }
    }

    /**
     * Limpia todos los campos en la vista.
     */
    public void limpiarVista() {
        vista.getTxtIdCuentaCobro1().setText("");
        vista.getTxtIdProveedor().setText("");
        vista.getTxtFecha().setText("");
        vista.getTxtDocumento().setText("");
        vista.getTxtTotalBruto().setText("");
        vista.getTxtEstado().setText("");
        vista.getTxtIdUsuario().setText("");
        vista.getTxtFechaCompra().setText("");
        vista.getCmbMetodoPago().setSelectedIndex(0);
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private ModeloCompra construirCompraDesdeVista() throws Exception {
        ModeloCompra compra = new ModeloCompra();

        compra.setIdProveedor(Integer.parseInt(vista.getTxtIdProveedor().getText().trim()));
        compra.setDocumento(vista.getTxtDocumento().getText().trim());
        compra.setTotalBruto(Double.parseDouble(vista.getTxtTotalBruto().getText().trim()));
        compra.setFormaPago(vista.getCmbMetodoPago().getSelectedItem().toString());
        compra.setEstado(vista.getTxtEstado().getText().trim());
        compra.setIdUsuario(Integer.parseInt(vista.getTxtIdUsuario().getText().trim()));

        // Conversión de fechas
        Date fecha = sdf.parse(vista.getTxtFecha().getText().trim());
        Date fechaCompra = sdf.parse(vista.getTxtFechaCompra().getText().trim());
        compra.setFecha(fecha);
        compra.setFechaCompra(fechaCompra);

        return compra;
    }

    private void limpiarCampos() {
        limpiarVista(); // reutilizamos el método público
    }
}
