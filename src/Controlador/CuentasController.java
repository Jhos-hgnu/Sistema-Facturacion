/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author brand
 */
// Controlador/CuentasController.java

import Implementacion.CuentasDAOImpl;
import Interfaces.CuentasDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class CuentasController {

    private final JFrame vista;      // o tu clase de diálogo/panel
    private final JTable tabla;
    private final JButton btnCobrar;
    private final JButton btnPagar;
    private final CuentasDAO dao = new CuentasDAOImpl();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CuentasController(JFrame vista, JTable tabla, JButton btnCobrar, JButton btnPagar) {
        this.vista = vista;
        this.tabla = tabla;
        this.btnCobrar = btnCobrar;
        this.btnPagar = btnPagar;
        wireEvents();
    }

    private void wireEvents() {
        btnCobrar.addActionListener(e -> cargarCXC());
        btnPagar.addActionListener(e -> cargarCXP());
    }

    private void cargarCXC() {
        var rows = dao.listarCXC(0);
        String[] cols = {"ID", "ID Venta", "Emisión", "Vence", "Monto", "Saldo", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        rows.forEach(r -> m.addRow(new Object[]{
            r.id(), r.idVenta(),
            r.emision() == null ? "" : df.format(r.emision()),
            r.vence()   == null ? "" : df.format(r.vence()),
            r.monto(), r.saldo(), r.estado()
        }));
        tabla.setModel(m);
        ajustarAnchosCXC();
    }

    private void cargarCXP() {
        var rows = dao.listarCXP(0);
        String[] cols = {"ID", "ID Compra", "Emisión", "Vencimiento", "Monto Total", "Saldo Pend.", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        rows.forEach(r -> m.addRow(new Object[]{
            r.id(), r.idCompra(),
            r.emision() == null ? "" : df.format(r.emision()),
            r.vencimiento() == null ? "" : df.format(r.vencimiento()),
            r.montoTotal(), r.saldoPend(), r.estado()
        }));
        tabla.setModel(m);
        ajustarAnchosCXP();
    }

    private void ajustarAnchosCXC() {
        int[] widths = {50, 70, 90, 90, 90, 90, 90};
        for (int i = 0; i < widths.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private void ajustarAnchosCXP() {
        int[] widths = {50, 80, 90, 105, 100, 100, 90};
        for (int i = 0; i < widths.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}
