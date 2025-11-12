/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// src/Controlador/ControladorReporteCompras.java
package Controlador;


import Implementacion.ReporteCompraImpl;
import Interfaces.IReporteCompras;
import Modelo.ModeloReporteCompras.CompraFechaDTO;
import Modelo.ModeloReporteCompras.CompraProveedorDTO;
import Modelo.ModeloReporteCompras.ModeloItem;
import Utilities.CsvExporter;
import Vistas.PanelReporteCompras; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ControladorReporteCompra implements MouseListener {

    private final PanelReporteCompras vista;
    private final IReporteCompras dao = new ReporteCompraImpl();
    private final JTable table = new JTable();

    public ControladorReporteCompra(PanelReporteCompras v) {
        this.vista = v;
        init();
    }

    private void init() {
        
       
        vista.tblcompras.setViewportView(table);

      

DefaultComboBoxModel<ModeloItem> modeloCombo = new DefaultComboBoxModel<>();
for (ModeloItem it : dao.listarProveedores()) modeloCombo.addElement(it);
vista.boxProveedor.setModel(modeloCombo);


     
        vista.btnporFecha.addMouseListener(this);
        vista.btnProveedor.addMouseListener(this);
        vista.btnexportar.addMouseListener(this);

        
        vista.btnporFecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        vista.btnProveedor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        vista.btnexportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src == vista.btnporFecha) onPorFechas();
        else if (src == vista.btnProveedor) onPorProveedor();
        else if (src == vista.btnexportar) onExportarCSV();
    }

    private void onPorFechas() {
        LocalDate d1 = vista.FechaDesde.getDate();
        LocalDate d2 = vista.FechaHasta.getDate();
        if (d1 == null || d2 == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione Fecha desde y Hasta.");
            return;
        }
        List<CompraFechaDTO> data = dao.comprasPorRango(Date.valueOf(d1), Date.valueOf(d2));
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Fecha","Proveedor","Tipo Pago","Total","Saldo CxP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        BigDecimal tot = BigDecimal.ZERO, saldo = BigDecimal.ZERO;
        for (CompraFechaDTO r : data) {
            m.addRow(new Object[]{ r.idCompra(), r.fecha(), r.proveedor(), nz(r.formaPago()), r.total(), r.saldo() });
            tot = tot.add(nz(r.total()));
            saldo = saldo.add(nz(r.saldo()));
        }
        table.setModel(m);
        vista.lblEstado.setText("Total: Q " + tot + "   |   Saldo por pagar: Q " + saldo);
    }

    private void onPorProveedor() {
        if (vista.boxProveedor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No hay proveedores.");
            return;
        }
        Object sel = vista.boxProveedor.getSelectedItem();
        if (!(sel instanceof ModeloItem it)) {
            JOptionPane.showMessageDialog(vista, "Proveedor inválido.");
            return;
        }
        LocalDate d1 = vista.FechaDesde.getDate();
        LocalDate d2 = vista.FechaHasta.getDate();

        List<CompraProveedorDTO> data = dao.comprasPorProveedor(
                it.id(),
                d1 == null ? null : Date.valueOf(d1),
                d2 == null ? null : Date.valueOf(d2)
        );

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Fecha","Tipo Pago","Total","Pagado","Saldo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        BigDecimal tot = BigDecimal.ZERO, saldo = BigDecimal.ZERO;
        for (CompraProveedorDTO r : data) {
            m.addRow(new Object[]{ r.idCompra(), r.fecha(), nz(r.tipoPago()), r.total(), r.pagado(), r.saldo() });
            tot = tot.add(nz(r.total()));
            saldo = saldo.add(nz(r.saldo()));
        }
        table.setModel(m);
        vista.lblEstado.setText("Proveedor: " + it.nombre()
                + "   |   Total Compras: Q " + tot
                + "   |   Saldo por Pagar: Q " + saldo);
    }

    private void onExportarCSV() {
        if (table.getModel() == null || table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No hay datos para exportar.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar CSV");
        fc.setSelectedFile(new java.io.File("reporte_compras.csv"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                CsvExporter.exportForExcel(table, fc.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "CSV generado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    private static BigDecimal nz(BigDecimal b){ return b==null? BigDecimal.ZERO : b; }
    private static String nz(String s){ return s==null? "" : s; }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
