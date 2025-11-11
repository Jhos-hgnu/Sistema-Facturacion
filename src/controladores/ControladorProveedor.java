/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

/**
 *
 * @author Madelin
 */



import Implementacion.ProveedorImp;
import Interfaces.ProveedorDAO;
import Modelo.ModeloProveedor;
import Utilities.Valida;
import Vistas.PanelProveedores;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControladorProveedor {

    private final PanelProveedores vista;
    private final ProveedorDAO dao;
    private final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ControladorProveedor(PanelProveedores vista) {
        this.vista = vista;
        this.dao = new ProveedorImp();
        wireEventos();
        cargarCombo();
        vista.txtEstado.setText("ACTIVO");
    }

    private void cargarCombo() {
        if (vista.cmbMetodoPago.getItemCount() == 0) {
            vista.cmbMetodoPago.addItem("Efectivo");
            vista.cmbMetodoPago.addItem("Transferencia");
            vista.cmbMetodoPago.addItem("Crédito");
            vista.cmbMetodoPago.setSelectedIndex(0);
        }
    }

    private void wireEventos() {
        vista.btnAgregar.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { onAgregar(); } });
        vista.btnActualizar.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { onActualizar(); } });
        vista.btnEliminar.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { onEliminar(); } });
        vista.btnBuscar.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { onBuscar(); } });
    }

    private ModeloProveedor leerFormulario(boolean exigirId) {
        ModeloProveedor p = new ModeloProveedor();

        String idTxt = vista.txtIDProveedor.getText().trim();
        if (exigirId && !Valida.esEnteroPositivo(idTxt))
            throw new IllegalArgumentException("ID Proveedor inválido.");
        if (Valida.esEnteroPositivo(idTxt))
            p.setIdProveedor(Integer.parseInt(idTxt));

        String nit = vista.txtNIT.getText().trim();
        if (nit.isBlank()) throw new IllegalArgumentException("El NIT es obligatorio.");
        p.setNit(nit);

        p.setRazonSocial(vista.txtRazonSocial.getText().trim());
        p.setDireccion(vista.txtDireccion.getText().trim());

        String tel = vista.txtTelefono.getText().trim();
        if (!Valida.esTelefono(tel)) throw new IllegalArgumentException("Teléfono inválido.");
        p.setTelefono(tel);

        String mail = vista.txtEmail.getText().trim();
        if (!Valida.esEmail(mail)) throw new IllegalArgumentException("Email inválido.");
        p.setEmail(mail);

        p.setTipoPago(String.valueOf(vista.cmbMetodoPago.getSelectedItem()));

        String plazo = vista.txtPlazaCredito.getText().trim();
        p.setPlazoCredito(plazo.isBlank()?0:Integer.parseInt(plazo));

        p.setRepresentante(vista.txtRepresentante.getText().trim());

        String estado = vista.txtEstado.getText().trim();
        p.setEstado(estado.isBlank() ? "ACTIVO" : estado.toUpperCase());

        String f = vista.txtFechaRegistro.getText().trim();
        p.setFechaRegistro(f.isBlank()? null : LocalDate.parse(f, F));

        return p;
    }

    private void pintarFormulario(ModeloProveedor p) {
        vista.txtIDProveedor.setText(p.getIdProveedor()==null? "":String.valueOf(p.getIdProveedor()));
        vista.txtNIT.setText(p.getNit());
        vista.txtRazonSocial.setText(p.getRazonSocial());
        vista.txtDireccion.setText(p.getDireccion());
        vista.txtTelefono.setText(p.getTelefono());
        vista.txtEmail.setText(p.getEmail());
        vista.cmbMetodoPago.setSelectedItem(p.getTipoPago());
        vista.txtPlazaCredito.setText(p.getPlazoCredito()==null? "":String.valueOf(p.getPlazoCredito()));
        vista.txtRepresentante.setText(p.getRepresentante());
        vista.txtEstado.setText(p.getEstado());
        vista.txtFechaRegistro.setText(p.getFechaRegistro()==null? "":p.getFechaRegistro().toString());
    }

    private void limpiar() {
        vista.txtIDProveedor.setText("");
        vista.txtNIT.setText("");
        vista.txtRazonSocial.setText("");
        vista.txtDireccion.setText("");
        vista.txtTelefono.setText("");
        vista.txtEmail.setText("");
        vista.cmbMetodoPago.setSelectedIndex(0);
        vista.txtPlazaCredito.setText("");
        vista.txtRepresentante.setText("");
        vista.txtEstado.setText("ACTIVO");
        vista.txtFechaRegistro.setText("");
    }

    private void onAgregar() {
        try {
            ModeloProveedor p = leerFormulario(false);
            int r = dao.crear(p);
            JOptionPane.showMessageDialog(vista, r==1?"Proveedor guardado.":"No se guardó.", "Info", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            ModeloProveedor p = leerFormulario(true);
            int r = dao.actualizar(p);
            JOptionPane.showMessageDialog(vista, r==1?"Proveedor actualizado.":"No se actualizó.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        String[] ops = {"Eliminar FÍSICO", "Desactivar (INACTIVO)", "Cancelar"};
        int op = JOptionPane.showOptionDialog(vista, "¿Qué acción deseas realizar?", "Eliminar/Desactivar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, ops, ops[2]);
        if (op==2 || op==-1) return;

        try {
            int id = Integer.parseInt(vista.txtIDProveedor.getText().trim());
            int r = (op==0) ? dao.eliminarFisico(id) : dao.desactivar(id);
            JOptionPane.showMessageDialog(vista, r==1?"Listo.":"No se realizó el cambio.", "Info", JOptionPane.INFORMATION_MESSAGE);
            if (op==0) limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar/desactivar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        try {
            String criterio = vista.txtIDProveedor.getText().trim();
            Optional<ModeloProveedor> res;
            if (criterio.matches("\\d+")) {
                res = dao.buscarPorId(Integer.parseInt(criterio));
            } else {
                String nit = vista.txtNIT.getText().trim();
                if (nit.isBlank()) throw new IllegalArgumentException("Ingresa ID o NIT para buscar.");
                res = dao.buscarPorNit(nit);
            }
            if (res.isPresent()) pintarFormulario(res.get());
            else JOptionPane.showMessageDialog(vista, "No encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en la búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
