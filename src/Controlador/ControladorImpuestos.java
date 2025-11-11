/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Conector.DBConnection;
import Implementacion.ImpuestosImpl;
import Interfaces.IImpuestos;
import Modelo.ModeloImpuesto;
import Vistas.PanelImpuestos;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Optional;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



   
   
/**
 *
 * @author anyi4
 */

public class ControladorImpuestos implements MouseListener {

    private final PanelImpuestos vista;
    private final IImpuestos dao = new ImpuestosImpl();
    private final DBConnection db = new DBConnection();
    private final long idUsuarioActual;
     private volatile boolean enProceso = false;
    private long ultimoClickMs = 0L;
    public ControladorImpuestos(PanelImpuestos vista, long idUsuarioActual) {
        this.vista = vista;
        this.idUsuarioActual = idUsuarioActual;
        db.conectar();
        this.vista.setControlador(this);
    }

    private void limpiar() {
        vista.txtIdImpuesto.setText("");
        vista.txtNombreImpuesto.setText("");
        vista.txtTasa.setText("");
    }

    private BigDecimal parseTasa(String s) {
        try {
            BigDecimal t = new BigDecimal(s.trim());
            if (t.compareTo(BigDecimal.ZERO) < 0 || t.compareTo(new BigDecimal("100.00")) > 0)
                throw new NumberFormatException();
            return t.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new IllegalArgumentException("La tasa debe ser un número entre 0 y 100 (máx. 2 decimales).");
        }
    }

    // ==== MouseListener ====
  // package Controlador;


    @Override
    public void mouseClicked(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;

        // Antirrebote  (ignora clicks con menos de 300ms de separación)
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoClickMs < 300) return;
        ultimoClickMs = ahora;

        if (enProceso) return;
        enProceso = true;

        try {
            Object src = e.getSource();
            String name = (src instanceof JComponent) ? ((JComponent) src).getName() : null;

           
            if (src == vista.btnRegistrar  || "REGISTRAR".equals(name)) {
                onRegistrar();
            } else if (src == vista.btnBuscar  || "BUSCAR".equals(name)) {
                onBuscar();
            } else if (src == vista.btnBorrar  || "BORRAR".equals(name)) {
                onBorrar();
            }
        } finally {
            enProceso = false;
        }
    }

    


   private void onRegistrar() {
    String nombre = vista.txtNombreImpuesto.getText().trim();
    String tasaTxt = vista.txtTasa.getText().trim();
    if (nombre.isEmpty()) { JOptionPane.showMessageDialog(vista,"El nombre es obligatorio."); return; }

    BigDecimal tasa;
    try { tasa = parseTasa(tasaTxt); }
    catch (IllegalArgumentException ex) { JOptionPane.showMessageDialog(vista, ex.getMessage()); return; }

    Connection cn = db.getConnection();
    try {
       
        if (dao.existeNombre(cn, nombre)) {
            JOptionPane.showMessageDialog(vista, "Ya existe un impuesto con ese nombre.");
            return;
        }

        db.comenzarTransaccion();
        long id = dao.crear(cn, nombre, tasa, idUsuarioActual);
        db.confirmarTransaccion();

        vista.txtIdImpuesto.setText(String.valueOf(id));
        JOptionPane.showMessageDialog(vista, "Impuesto registrado (ID " + id + ").");
    } catch (Exception ex) {
        db.revertirTransaccion();
        String msg = ex.getMessage();
        if (msg != null && (msg.contains("UQ_IMPUESTOS_NOMBRE") || msg.toUpperCase().contains("ORA-00001"))) {
            JOptionPane.showMessageDialog(vista, "Ya existe un impuesto con ese nombre.");
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar: " + msg);
        }
    }
}

    private void onBuscar() {
        String idTxt = vista.txtIdImpuesto.getText().trim();
        String nombre = vista.txtNombreImpuesto.getText().trim();
        try {
            Optional<ModeloImpuesto> op;
            if (!idTxt.isEmpty()) {
                long id = Long.parseLong(idTxt);
                op = dao.buscarPorId(db.getConnection(), id);
            } else if (!nombre.isEmpty()) {
                op = dao.buscarPorNombreExacto(db.getConnection(), nombre);
            } else {
                JOptionPane.showMessageDialog(vista, "Ingrese ID o Nombre para buscar."); return;
            }

            if (op.isPresent()) {
                ModeloImpuesto m = op.get();
                vista.txtIdImpuesto.setText(String.valueOf(m.getIdImpuestos()));
                vista.txtNombreImpuesto.setText(m.getNombre());
                vista.txtTasa.setText(m.getTasa() == null ? "" : m.getTasa().toPlainString());
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró el impuesto.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser numérico.");
        } catch (Exception ex) {
            manejarError(ex, "buscar");
        }
    }

    private void onBorrar() {
        String idTxt = vista.txtIdImpuesto.getText().trim();
        if (idTxt.isEmpty()) { JOptionPane.showMessageDialog(vista,"Ingrese el ID a borrar."); return; }
        long id = Long.parseLong(idTxt);

        try {
            if (dao.estaEnUsoPorProductos(db.getConnection(), id)) {
                JOptionPane.showMessageDialog(vista, "No se puede borrar: está en uso por productos.");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(vista,
                    "¿Seguro de borrar el impuesto " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            db.comenzarTransaccion();
            boolean borrado = dao.borrar(db.getConnection(), id, idUsuarioActual);
            db.confirmarTransaccion();

            if (borrado) { JOptionPane.showMessageDialog(vista,"Impuesto borrado."); limpiar(); }
            else { JOptionPane.showMessageDialog(vista,"No se encontró el impuesto."); }
        } catch (Exception ex) {
            db.revertirTransaccion();
            manejarError(ex, "borrar");
        }
    }

    private void manejarError(Exception ex, String accion) {
        String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
        if (msg.contains("ORA-00001")) {
            JOptionPane.showMessageDialog(vista, "Nombre de impuesto ya existe (único).");
        } else {
            JOptionPane.showMessageDialog(vista, "Error al " + accion + ": " + msg);
        }
        ex.printStackTrace();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
