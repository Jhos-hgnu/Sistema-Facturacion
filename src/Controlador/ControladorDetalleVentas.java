/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.DetalleVentasDAO;
import Modelo.ModeloDetalleVenta;
import Vistas.PanelDetalleVenta;
import Vistas.PanelVentas;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author luisd
 */
public class ControladorDetalleVentas {

    private PanelVentas vista;
    private DetalleVentasDAO dao;

    public ControladorDetalleVentas(PanelVentas vista) {
        this.vista = vista;
        this.dao = new DetalleVentasDAO();

        // Escucha el clic del botón para agregar el detalle
        this.vista.btnHacerVenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                agregarDetalle();
            }
        });
    }

    private void agregarDetalle() {
        try {
            // Validar campos vacíos
            if (vista.txtIdVenta.getText().trim().isEmpty() ||
                vista.txtIdProducto.getText().trim().isEmpty() ||
                vista.txtCantidad.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(vista, "⚠️ Debe llenar todos los campos antes de agregar el detalle.");
                return;
            }

            // Crear modelo y asignar valores
            ModeloDetalleVenta modelo = new ModeloDetalleVenta();
            modelo.setIdVenta(Long.parseLong(vista.txtIdVenta.getText().trim()));
            modelo.setIdProducto(Long.parseLong(vista.txtIdProducto.getText().trim()));
            modelo.setCantidad(Integer.parseInt(vista.txtCantidad.getText().trim()));

            // Si también tienes precio o subtotal, puedes incluirlos aquí:
            if (!vista.txtPrecioVenta.getText().trim().isEmpty()) {
                modelo.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText().trim()));
            }

            // Insertar en base de datos
            boolean exito = dao.insertarDetalleVenta(modelo);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle de venta agregado correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al agregar detalle. Revise los datos.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "⚠️ Verifique que los campos numéricos tengan solo números.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "❌ Error inesperado: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        vista.txtIdProducto.setText("");
        vista.txtCantidad.setText("");
        vista.txtPrecioVenta.setText("");
    }
}
