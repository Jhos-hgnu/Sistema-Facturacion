/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.DetalleVentasDAO;
import Implementacion.VentasDAO;
import Modelo.ModeloClientesVentas;
import Modelo.ModeloDetalleVenta;
import Modelo.ModeloInicioUsuario;
import Modelo.ModeloProducto;
import Modelo.ModeloRegistroCliente;
import Modelo.ModeloVenta;
import Modelo.ModeloVistaInicio;
import Vistas.PanelVentas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhosu
 */
public class ControladorVentas {

    private final PanelVentas vista;
    private final VentasDAO dao = new VentasDAO();

    public ControladorVentas(PanelVentas vista) {
        this.vista = vista;

        // Asignar eventos a los botones del panel
        vista.btnHacerVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                guardarVenta();
            }
        });

        vista.btnEliminarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                limpiarCampos();
            }
        });
    }

    // ====================================================
    // GUARDAR VENTA SIMPLE
    // ====================================================
    private void guardarVenta() {
        try {
            String nit = vista.txtNIT.getText().trim();
            String tipoPago = vista.cmbMetodoPago.getSelectedItem().toString();
            String observacion = vista.txtObservaciones.getText().trim();
            String totalTxt = vista.txtTotal.getText().trim();
            String documento = vista.txtDocumento.getText().trim();

            if (nit.isEmpty() || totalTxt.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "⚠️ Ingresa el NIT y el total de la venta.");
                return;
            }

            double total = Double.parseDouble(totalTxt);

            ModeloVenta venta = new ModeloVenta();
            venta.setNit(nit);
            venta.setFecha(new java.sql.Date(System.currentTimeMillis()));
            venta.setTipoPago(tipoPago);
            venta.setDocumento(documento.isEmpty() ? "VENTA SIN DETALLE" : documento);
            venta.setTotal(total);
            venta.setIdUsuario(1); // Cambiar según usuario logueado
            venta.setObservacion(observacion);

            boolean exito = dao.insertarVenta(venta);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Venta guardada correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al guardar la venta.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ El total debe ser un número válido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ====================================================
    // LIMPIAR CAMPOS
    // ====================================================
    private void limpiarCampos() {
        vista.txtNIT.setText("");
        vista.txtDocumento.setText("");
        vista.txtObservaciones.setText("");
        vista.txtTotal.setText("");
        vista.cmbMetodoPago.setSelectedIndex(0);
    }
}
