/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.InventarioMovImpl;
import Interfaces.IInventarioMov;
import Modelo.ModeloMovInventario;
import Modelo.ModeloReporteCompras.ModeloItem;
import Utilities.CsvExporter;
import Vistas.PanelMovimientosInventario;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anyi4
 */
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ControladorMovimientos implements MouseListener {

    private final PanelMovimientosInventario vista;
    private final IInventarioMov dao = new InventarioMovImpl();
    private final JTable table = new JTable();

    public ControladorMovimientos(PanelMovimientosInventario v) {
        this.vista = v;
        init();
    }

    private void init() {
        // Tabla en el scroll
        vista.tblcompras.setViewportView(table);
        table.setFillsViewportHeight(true);
        table.setRowHeight(22);
        table.setAutoCreateRowSorter(true);

        // Zebra striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override protected void setValue(Object value) {
                super.setValue(value);
            }
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? new Color(245, 248, 250) : Color.WHITE);
                return c;
            }
        });

        // Combos: Producto y Usuario
        DefaultComboBoxModel<ModeloItem> mProd = new DefaultComboBoxModel<>();
        for (ModeloItem it : dao.listarProductos(null)) mProd.addElement(it);
        vista.boxProducto.setModel(mProd);
        vista.boxProducto.setSelectedIndex(-1);

        DefaultComboBoxModel<ModeloItem> mUsu = new DefaultComboBoxModel<>();
        for (ModeloItem it : dao.listarUsuarios(null)) mUsu.addElement(it);
        vista.boxUsuario.setModel(mUsu);
        vista.boxUsuario.setSelectedIndex(-1);

        // Listeners tipo "panel-botón" y cursores
        vista.btnBuscar.addMouseListener(this);
        vista.btnlimpiar.addMouseListener(this);
        vista.btnexportar.addMouseListener(this);

        vista.btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        vista.btnlimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        vista.btnexportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Placeholder visual en txttipo (si está vacío, mostrar “TODOS”)
        if (vista.txttipo.getText().isBlank()) vista.txttipo.setText("TODOS");

        // Modelo vacío inicial bonito
        ponerModeloTablaVacio();
    }

    @Override public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src == vista.btnBuscar) onBuscar();
        else if (src == vista.btnlimpiar) onLimpiar();
        else if (src == vista.btnexportar) onExportarCSV();
    }

    private void onBuscar() {
        LocalDate d1 = vista.FechaDesde.getDate();
        LocalDate d2 = vista.FechaHasta.getDate();
        if (d1 == null || d2 == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione Fecha desde y Hasta.");
            return;
        }
        if (d2.isBefore(d1)) {
            JOptionPane.showMessageDialog(vista, "La fecha 'Hasta' no puede ser anterior a 'Desde'.");
            return;
        }

        // Fechas a java.sql.Date
        Date fDesde = Date.valueOf(d1);
        Date fHasta = Date.valueOf(d2);

        // Filtros
        ModeloItem prod = (ModeloItem) vista.boxProducto.getSelectedItem();
        Long idProd = (prod == null ? null : prod.id());

        ModeloItem usu  = (ModeloItem) vista.boxUsuario.getSelectedItem();
        Long idUsuario = (usu == null ? null : usu.id());

        // Ya no filtramos por código de barras; se ignora si viene
        String cbarras = null;

        // Normalización del tipo (plural→singular; default TODOS)
        String tipoVisible = vista.txttipo.getText().trim().toUpperCase();
        String tipo = normalizarTipo(tipoVisible);

        boolean soloObs = vista.chkobsv.isSelected();

        // Busy cursor mientras consulta
        setBusy(true);
        try {
            List<ModeloMovInventario> data = dao.buscarMovimientos(
                    idProd, cbarras, idUsuario, fDesde, fHasta, tipo, soloObs);
            llenarTabla(data);
            actualizarResumen(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al buscar: " + ex.getMessage());
        } finally {
            setBusy(false);
        }
    }

    private void llenarTabla(List<ModeloMovInventario> data) {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Fecha/Hora","Tipo","Producto",
                             "Cantidad","Stock Antes","Stock Después","Usuario","Motivo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int col) {
                return switch (col) {
                    case 0 -> Long.class;            // ID
                    case 1 -> java.util.Date.class;  // Fecha/Hora (Timestamp)
                    case 5,6,7 -> BigDecimal.class;  // Cantidades/stock
                    default -> String.class;
                };
            }
        };

        for (ModeloMovInventario r : data) {
           m.addRow(new Object[]{
    r.idMov(),             // 1 ID
    r.fechaHora(),         // 2 Fecha/Hora
    r.tipo(),              // 3 Tipo
    r.producto(),          // 4 Producto (nombre)
   
    r.cantidad(),          // 6 Cantidad
    r.stockAntes(),        // 7 Stock Antes
    r.stockDespues(),      // 8 Stock Después
    r.usuario(),           // 9 Usuario
    r.motivo()             // 10 Motivo
});

           
        }

        table.setModel(m);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(m);
        table.setRowSorter(sorter);

        // Alineaciones y formato
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(0).setCellRenderer(center); // ID
        table.getColumnModel().getColumn(2).setCellRenderer(center); // Tipo
        table.getColumnModel().getColumn(4).setCellRenderer(center); // ID Prod.

        table.getColumnModel().getColumn(5).setCellRenderer(right); // Cantidad
        table.getColumnModel().getColumn(6).setCellRenderer(right); // Stock Antes
        table.getColumnModel().getColumn(7).setCellRenderer(right); // Stock Después

        // Anchos cómodos
        int[] widths = {60,150,90,200,80,90,100,110,140,240};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Encabezados “bonitos”
        JTableHeader th = table.getTableHeader();
        th.setReorderingAllowed(false);
        th.setFont(th.getFont().deriveFont(Font.BOLD));
    }
private void actualizarResumen(List<ModeloMovInventario> data) {
    int cntEnt = 0, cntSal = 0, cntAj = 0;
    BigDecimal balance = BigDecimal.ZERO;

    for (ModeloMovInventario r : data) {
        String t = safe(r.tipo()).toUpperCase();
        BigDecimal c = nz(r.cantidad()); // cantidad absoluta o con signo según guardes
        switch (t) {
            case "ENTRADA" -> { cntEnt++; balance = balance.add(c.abs()); }
            case "SALIDA"  -> { cntSal++; balance = balance.subtract(c.abs()); }
            case "AJUSTE"  -> {
                cntAj++;
                // si tus ajustes en BD ya vienen con signo (+/-), usa "balance = balance.add(c);"
                // si vienen siempre positivos y el signo está en el motivo, decide la regla aquí:
                balance = balance.add(c); 
            }
        }
    }

    String texto = "Entradas: " + cntEnt + "  |  Salidas: " + cntSal +
                   "  |  Ajustes: " + cntAj + "  |  Balance: " + balance;

    try {
        java.lang.reflect.Field f = vista.getClass().getField("lblEstado");
        JLabel lbl = (JLabel) f.get(vista);
        lbl.setText(texto);
    } catch (Exception ignore) {
        // Si tu vista no tiene lblEstado, creamos uno provisional arriba de la tabla.
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        vista.tblcompras.setColumnHeaderView(lbl); // lo muestra como franja superior
    }
}


    private void onLimpiar() {
        vista.FechaDesde.clear();
        vista.FechaHasta.clear();
        vista.boxProducto.setSelectedIndex(-1);
        vista.boxUsuario.setSelectedIndex(-1);
        vista.txtcodigobarras.setText("");
        vista.txttipo.setText("TODOS");
        vista.chkobsv.setSelected(false);

        ponerModeloTablaVacio();

        try {
            java.lang.reflect.Field f = vista.getClass().getField("lblEstado");
            JLabel lbl = (JLabel) f.get(vista);
            lbl.setText("");
        } catch (Exception ignore) { }
    }

    private void onExportarCSV() {
        if (table.getModel() == null || table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No hay datos para exportar.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar CSV");
        fc.setSelectedFile(new java.io.File("movimientos_inventario.csv"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                CsvExporter.exportForExcel(table, fc.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "CSV generado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    // ===== util =====
    private static BigDecimal nz(BigDecimal b){ return b == null ? BigDecimal.ZERO : b; }
    private static String safe(String s){ return s == null ? "" : s; }

    private static String normalizarTipo(String visible) {
        return switch (visible) {
            case "ENTRADAS" -> "ENTRADA";
            case "SALIDAS"  -> "SALIDA";
            case "AJUSTES"  -> "AJUSTE";
            case "", "TODOS" -> "TODOS";
            default -> {
                // si escriben algo raro, no mates el filtro
                if ("ENTRADA".equals(visible) || "SALIDA".equals(visible) || "AJUSTE".equals(visible))
                    yield visible;
                yield "TODOS";
            }
        };
    }

    private void setBusy(boolean busy) {
        Component glass = vista.getRootPane().getGlassPane();
        glass.setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        glass.setVisible(busy);
        vista.btnBuscar.setEnabled(!busy);
        vista.btnlimpiar.setEnabled(!busy);
        vista.btnexportar.setEnabled(!busy);
    }

    private void ponerModeloTablaVacio() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Fecha/Hora","Tipo","Producto","ID Prod.",
                             "Cantidad","Stock Antes","Stock Después","Usuario","Motivo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(m);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
