/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conector.DBConnection;
import Implementacion.ReporteProductosImpl;
import Interfaces.IReporteProductos;
import Modelo.ModeloReporteProductos.ProdMasVendidoDTO;
import Modelo.ModeloReporteProductos.StockBajoDTO;
import Utilities.CsvExporter;
import Vistas.PanelReporteProductos;

import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ControladorReporteProductos implements MouseListener {

    private final PanelReporteProductos vista;
    private final IReporteProductos dao;
    private final DBConnection db;
    private boolean enProceso = false;
    private long ultimoClick = 0;

    public ControladorReporteProductos(PanelReporteProductos vista) {
        this.vista = vista;

        // Conexión local del módulo (igual filosofía que tus otros controladores)
        this.db = new DBConnection();
        this.db.conectar();

        this.dao = new ReporteProductosImpl(db.getConnection());
    }

    /* ===================== Helpers ===================== */

    private static Date toSql(LocalDate d) { return (d == null) ? null : Date.valueOf(d); }

    private void setEstado(String s) { if (vista.lblEstado != null) vista.lblEstado.setText(s); }

    private Integer leerEntero(JTextField tf, String nombreCampo, boolean permitirVacio) {
        String t = tf.getText().trim();
        if (t.isEmpty()) return permitirVacio ? null : 0;
        if (!t.matches("\\d+")) {
            JOptionPane.showMessageDialog(vista, nombreCampo + " debe ser numérico.");
            return null;
        }
        try { return Integer.valueOf(t); } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, nombreCampo + " inválido.");
            return null;
        }
    }

    /* ===================== Acciones ===================== */

    public void accionMasVendidos() {
        try {
            DatePicker dpD = vista.FechaDesde;
            DatePicker dpH = vista.FechaHasta;
            LocalDate d1 = (dpD == null) ? null : dpD.getDate();
            LocalDate d2 = (dpH == null) ? null : dpH.getDate();
            if (d1 != null && d2 != null && d1.isAfter(d2)) {
                JOptionPane.showMessageDialog(vista, "La fecha 'Desde' no puede ser mayor que 'Hasta'.");
                return;
            }

            Integer idCat = leerEntero(vista.txtCategoria, "Categoría (ID)", true);
            if (idCat == null && !vista.txtCategoria.getText().trim().isEmpty()) return;

            boolean porMonto = "MONTO".equalsIgnoreCase(vista.txtorden.getText().trim());
            Integer topN = leerEntero(vista.txtcantidad, "Cantidad (Top N)", true);
            int top = (topN == null) ? 10 : topN;

            List<ProdMasVendidoDTO> datos = dao.listarMasVendidos(
                    toSql(d1), toSql(d2), idCat, top, porMonto);

            DefaultTableModel m = new DefaultTableModel(
                    new Object[]{"ID","Nombre","Marca","Cantidad vendida","Monto vendido"}, 0);

            BigDecimal sumCant = BigDecimal.ZERO, sumMonto = BigDecimal.ZERO;
            for (ProdMasVendidoDTO x : datos) {
                m.addRow(new Object[]{ x.getIdProducto(), x.getNombre(), x.getMarca(),
                        x.getTotalCant(), x.getTotalMonto() });
                if (x.getTotalCant()  != null) sumCant  = sumCant.add(x.getTotalCant());
                if (x.getTotalMonto() != null) sumMonto = sumMonto.add(x.getTotalMonto());
            }
            vista.tblInventario.setModel(m);
            setEstado("Filas: " + datos.size() + "  |  Σ Cantidad: " + sumCant + "  |  Σ Monto: " + sumMonto);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en 'Más vendidos': " + ex.getMessage());
        }
    }

    public void accionStockBajo() {
        try {
            Integer min = leerEntero(vista.txtStock, "Stock mínimo", true);
            int umbral = (min == null) ? 10 : min;

            Integer idCat = leerEntero(vista.txtCategoriaS, "Categoría (ID)", true);
            if (idCat == null && !vista.txtCategoriaS.getText().trim().isEmpty()) return;

            List<StockBajoDTO> datos = dao.listarStockBajo(umbral, idCat);

            DefaultTableModel m = new DefaultTableModel(
                    new Object[]{"ID","Nombre","Marca","Stock actual"}, 0);
            for (StockBajoDTO x : datos) {
                m.addRow(new Object[]{ x.getIdProducto(), x.getNombre(), x.getMarca(), x.getStock() });
            }
            vista.tblInventario.setModel(m);
            setEstado("Productos con stock ≤ " + umbral + ": " + datos.size());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en 'Stock bajo': " + ex.getMessage());
        }
    }

    public void accionExportarCSV() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("reporte.csv"));
            if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
               CsvExporter.exportForExcel(vista.tblInventario, fc.getSelectedFile());

                JOptionPane.showMessageDialog(vista, "CSV exportado correctamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error exportando CSV: " + ex.getMessage());
        }
    }

    /* ===================== MouseListener (mismo patrón) ===================== */

    @Override
    public void mouseClicked(MouseEvent e) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoClick < 300) return;  // antirebote
        ultimoClick = ahora;

        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if (e.getClickCount() > 1) return;
        if (enProceso) return;

        Object src = e.getSource();

        if (src == vista.btnmasvendido) {
            enProceso = true;
            accionMasVendidos();
            enProceso = false;
            return;
        }

        if (src == vista.btnStockBajo) {
            enProceso = true;
            accionStockBajo();
            enProceso = false;
            return;
        }

        if (src == vista.btnexportar) {
            enProceso = true;
            accionExportarCSV();
            enProceso = false;
            return;
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    // (opcional) cerrar cuando cierres el diálogo
    public void cerrar() {
        try {
            if (db != null && db.getConnection() != null && !db.getConnection().isClosed()) {
                db.getConnection().close();
            }
        } catch (Exception ignored) { }
    }
}
