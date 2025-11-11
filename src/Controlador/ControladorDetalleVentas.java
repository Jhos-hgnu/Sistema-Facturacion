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

    private final PanelVentas vista;
    private final DetalleVentasDAO dao;

    public ControladorDetalleVentas(PanelVentas vista) {
        this.vista = vista;
        this.dao = new DetalleVentasDAO();

        // Evento para insertar detalle
        this.vista.btnHacerVenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                agregarDetalle();
            }
        });

        this.vista.getBtnBuscarDetalle().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarDetalle();
            }
        });
    }

    // === AGREGAR DETALLE ===
    private void agregarDetalle() {
        try {
            // Validar campos obligatorios
            if (vista.txtIdVenta.getText().trim().isEmpty()
                    || vista.txtIdProducto.getText().trim().isEmpty()
                    || vista.txtCantidad.getText().trim().isEmpty()
                    || vista.txtPrecioVenta.getText().trim().isEmpty()
                    || vista.txtDescuento.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(vista, "⚠️ Debe llenar todos los campos antes de agregar el detalle.");
                return;
            }

            ModeloDetalleVenta detalle = new ModeloDetalleVenta();
            detalle.setIdVenta(Long.parseLong(vista.txtIdVenta.getText().trim()));
            detalle.setIdProducto(Long.parseLong(vista.txtIdProducto.getText().trim()));
            detalle.setCantidad(Integer.parseInt(vista.txtCantidad.getText().trim()));
            detalle.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText().trim()));
            detalle.setDescuento(Double.parseDouble(vista.txtDescuento.getText().trim()));

            boolean exito = dao.insertarDetalleVenta(detalle);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "✅ Detalle de venta agregado correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo agregar el detalle de venta.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "⚠️ Verifique que los campos numéricos contengan solo números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // === BUSCAR DETALLE POR ID ===
    private void buscarDetalle() {
        try {
            String idTexto = vista.getTxtIdDetalleVenta().getText().trim();
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese el ID del detalle a buscar.");
                return;
            }

            long idDetalle = Long.parseLong(idTexto);
            ModeloDetalleVenta detalle = dao.buscarPorId(idDetalle);

            if (detalle != null) {
                vista.txtIdVenta.setText(String.valueOf(detalle.getIdVenta()));
                vista.txtIdProducto.setText(String.valueOf(detalle.getIdProducto()));
                vista.txtCantidad.setText(String.valueOf(detalle.getCantidad()));
                vista.txtPrecioVenta.setText(String.valueOf(detalle.getPrecioVenta()));
                vista.txtDescuento.setText(String.valueOf(detalle.getDescuento()));

                JOptionPane.showMessageDialog(vista, "✅ Detalle encontrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista, "⚠️ No se encontró ningún detalle con ese ID.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "⚠️ El ID debe ser numérico.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error al buscar detalle: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // === LIMPIAR CAMPOS ===
    private void limpiarCampos() {
        vista.txtIdProducto.setText("");
        vista.txtCantidad.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtDescuento.setText("");
    }
}
