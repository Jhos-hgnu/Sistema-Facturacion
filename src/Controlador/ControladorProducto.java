package Controlador;

import Implementacion.CatalogosImpl;

import Implementacion.ProductoImp;
import Modelo.ModeloProducto;
import Modelo.ModeloVistaInicio;
import Vistas.PanelProducto;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class ControladorProducto implements MouseListener {

    private final PanelProducto vista;
    private final ProductoImp dao;
    private final CatalogosImpl catalogos;
    private boolean enProceso = false; 
    private long ultimoClick = 0;

    public ControladorProducto(PanelProducto vista) {
        this.vista = vista;
        this.dao = new ProductoImp();
        this.catalogos = new CatalogosImpl();
    }

   
    private long getUsuarioIdActual() {
        return (long) ModeloVistaInicio.getIdUsuarioEncontrado(); 
    }

    private BigDecimal validarCodigo() {
        String idText = vista.txtCodigoBarras.getText().trim();
        if (!idText.matches("\\d{1,20}")) {
            JOptionPane.showMessageDialog(vista,
                    "El código/ID del producto debe ser numérico y de 1 a 20 dígitos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new BigDecimal(idText);
    }

    private String validarCamposBasicos() {
        if (vista.txtNombreProducto.getText().trim().isEmpty())
            return "El nombre del producto es obligatorio.";

        String costoTxt = vista.txtPrecioCosto.getText().trim();
        String ventaTxt = vista.txtPrecioVenta.getText().trim();
        if (!costoTxt.matches("\\d+(\\.\\d+)?")) return "Precio costo inválido.";
        if (!ventaTxt.matches("\\d+(\\.\\d+)?")) return "Precio venta inválido.";
        BigDecimal costo = new BigDecimal(costoTxt);
        BigDecimal venta = new BigDecimal(ventaTxt);
        if (costo.compareTo(BigDecimal.ZERO) < 0) return "Precio costo no puede ser negativo.";
        if (venta.compareTo(BigDecimal.ZERO) < 0) return "Precio venta no puede ser negativo.";

        String stockTxt = vista.txtStock.getText().trim();
        if (!stockTxt.matches("\\d+")) return "Stock inválido.";
        int stock = Integer.parseInt(stockTxt);
        if (stock < 0) return "El stock no puede ser negativo.";

        return null;
    }

    private Integer obtenerIdCategoria(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        valor = valor.trim();
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            Integer id = catalogos.getCategoriaIdByNombre(valor);
            if (id == null) {
                JOptionPane.showMessageDialog(vista,
                        "No se encontró la categoría: " + valor,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return id;
        }
    }

    private Integer obtenerIdImpuesto(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        valor = valor.trim();
        if (valor.matches("\\d+")) return Integer.parseInt(valor);

        String tasaTxt = valor.replace("%", "").trim();
        if (tasaTxt.matches("\\d+(\\.\\d+)?")) {
            try {
                BigDecimal tasa = new BigDecimal(tasaTxt).setScale(2);
                Integer id = catalogos.getImpuestoIdByTasa(tasa);
                if (id != null) return id;
            } catch (NumberFormatException ignored) {}
        }

        Integer id = catalogos.getImpuestoIdByNombre(valor);
        if (id == null) {
            JOptionPane.showMessageDialog(vista,
                    "No se encontró el impuesto: " + valor,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return id;
    }

    private ModeloProducto buildProducto(BigDecimal idProd) {
        ModeloProducto p = new ModeloProducto();
        p.setIdProducto(idProd);
        p.setNombreProducto(vista.txtNombreProducto.getText().trim());
        p.setMarca(vista.txtMarca.getText().trim());
        p.setDescripcion(vista.txtDescripcion.getText().trim());
        p.setPrecioCosto(new BigDecimal(vista.txtPrecioCosto.getText().trim()));
        p.setPrecioVenta(new BigDecimal(vista.txtPrecioVenta.getText().trim()));
        p.setStock(Integer.parseInt(vista.txtStock.getText().trim()));

        Integer idCat = obtenerIdCategoria(vista.txtIdCategoria.getText());
        Integer idImp = obtenerIdImpuesto(vista.txtIdImpuesto.getText());
        if (idCat == null || idImp == null) return null;

        p.setIdCategoria(idCat);
        p.setIdImpuesto(idImp);
        return p;
    }

    private void limpiarFormulario() {
        vista.txtCodigoBarras.setText("");
        vista.txtNombreProducto.setText("");
        vista.txtMarca.setText("");
        vista.txtDescripcion.setText("");
        vista.txtPrecioCosto.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtStock.setText("");
        vista.txtIdCategoria.setText("");
        vista.txtIdImpuesto.setText("");
        vista.txtCodigoBarrasP.setText("");
        vista.txtNombreProductoB.setText("");

        vista.btnActualizar.setVisible(false);
        vista.btnEliminar.setVisible(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoClick < 300) return; 
        ultimoClick = ahora;

        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if (e.getClickCount() > 1) return;
        if (enProceso) return;

        Object src = e.getSource();

        if (src == vista.btnAgregar) {
            enProceso = true;
            agregarProducto();
            enProceso = false;
            return;
        }
        if (src == vista.btnActualizar) {
            enProceso = true;
            actualizarProducto();
            enProceso = false;
            return;
        }
        if (src == vista.btnEliminar) {
            enProceso = true;
            eliminarProducto();
            enProceso = false;
            return;
        }
        if (src == vista.btnBuscar) {
            enProceso = true;
            buscarProducto();
            enProceso = false;
            return;
        }
        if (src == vista.btnReporte) {
            return;
        }
    }

    public void agregarProducto() {
        enProceso = true;
        vista.btnAgregar.setEnabled(false);
        try {
            BigDecimal idProd = validarCodigo();
            if (idProd == null) return;

            String msg = validarCamposBasicos();
            if (msg != null) { JOptionPane.showMessageDialog(vista, msg); return; }

            if (dao.existeProducto(idProd)) {
                JOptionPane.showMessageDialog(vista, "Ya existe un producto con ese código de barras.");
                return;
            }

            ModeloProducto p = buildProducto(idProd);
            if (p == null) return;

            // ====== ÚNICO CAMBIO: pasar idUsuario para AUDITORÍA ======
            boolean ok = dao.registrarProducto(p, getUsuarioIdActual());
            JOptionPane.showMessageDialog(vista, ok ? "Producto registrado ✅" : "No se pudo registrar ❌");
            if (ok) limpiarFormulario(); 

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al registrar: " + ex.getMessage());
        } finally {
            enProceso = false;
            vista.btnAgregar.setEnabled(true);
        }
    }

    private void actualizarProducto() {
        enProceso = true;
        try {
            BigDecimal idProd = validarCodigo();
            if (idProd == null) return;

            String msg = validarCamposBasicos();
            if (msg != null) { JOptionPane.showMessageDialog(vista, msg); return; }

            if (!dao.existeProducto(idProd)) {
                JOptionPane.showMessageDialog(vista, "No existe un producto con ese código.");
                return;
            }

            ModeloProducto p = buildProducto(idProd);
            if (p == null) return;

            // ====== ÚNICO CAMBIO: pasar idUsuario para AUDITORÍA ======
            boolean ok = dao.actualizarProducto(p, getUsuarioIdActual());
            JOptionPane.showMessageDialog(vista, ok ? "Producto actualizado ✅" : "No se pudo actualizar ❌");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar: " + ex.getMessage());
        } finally {
            enProceso = false;
        }
    }

    private void eliminarProducto() {
        enProceso = true;
        try {
            BigDecimal idProd = validarCodigo();
            if (idProd == null) return;

            if (!dao.existeProducto(idProd)) {
                JOptionPane.showMessageDialog(vista, "No existe un producto con ese código.");
                return;
            }

            int r = JOptionPane.showConfirmDialog(vista,
                    "¿Eliminar producto con ID " + idProd + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (r != JOptionPane.YES_OPTION) return;

            // ====== ÚNICO CAMBIO: pasar idUsuario para AUDITORÍA ======
            boolean ok = dao.eliminarProducto(idProd, getUsuarioIdActual());
            JOptionPane.showMessageDialog(vista, ok ? "Producto eliminado 🗑️" : "No se pudo eliminar ❌");
            if (ok) limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar: " + ex.getMessage());
        } finally {
            enProceso = false;
        }
    }

    private void buscarProducto() {
        try {
            String idTxt = vista.txtCodigoBarrasP.getText().trim();
            String nomTxt = vista.txtNombreProductoB.getText().trim();
            ModeloProducto p = null;

            if (!idTxt.isEmpty()) {
                if (!idTxt.matches("\\d{1,20}")) {
                    JOptionPane.showMessageDialog(vista, "El ID debe ser numérico (1–20 dígitos).");
                    return;
                }
                p = dao.buscarPorId(new BigDecimal(idTxt));
            } else if (!nomTxt.isEmpty()) {
                p = dao.buscarPorNombre(nomTxt); 
            } else {
                JOptionPane.showMessageDialog(vista, "Ingresa ID o Nombre para buscar.");
                return;
            }

            if (p == null) {
                JOptionPane.showMessageDialog(vista, "No se encontraron productos.");
                return;
            }

            vista.txtCodigoBarras.setText(p.getIdProducto().toPlainString());
            vista.txtNombreProducto.setText(p.getNombreProducto());
            vista.txtMarca.setText(p.getMarca());
            vista.txtDescripcion.setText(p.getDescripcion());
            vista.txtPrecioCosto.setText(p.getPrecioCosto().toPlainString());
            vista.txtPrecioVenta.setText(p.getPrecioVenta().toPlainString());
            vista.txtStock.setText(String.valueOf(p.getStock()));
            vista.txtIdCategoria.setText(String.valueOf(p.getIdCategoria()));
            vista.txtIdImpuesto.setText(String.valueOf(p.getIdImpuesto()));

            vista.btnActualizar.setVisible(true);
            vista.btnEliminar.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al buscar: " + ex.getMessage());
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
