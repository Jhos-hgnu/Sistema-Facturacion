/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Implementacion.CategoriaImpl;
import Modelo.ModeloCategoria;
import Vistas.PanelCategorias;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author anyi4
 */

public class ControladorCategoria implements MouseListener {

    private final PanelCategorias vista;
    private final CategoriaImpl dao;

    // ID de la categoría cargada
    private Integer currentId = null;

    // Flag para evitar que las validaciones de la vista se disparen
    private boolean silencioso = false;

    public ControladorCategoria(PanelCategorias vista) {
        this.vista = vista;
        this.dao = new CategoriaImpl();
        this.vista.setControlador(this);
    }

    /** Permite a la vista consultar si debe saltarse validaciones en vivo */
    public boolean isSilencioso() { return silencioso; }

    /** Ejecuta una acción suspendiendo validaciones en vivo de la vista */
    private void runSilencioso(Runnable r) {
        silencioso = true;
        try { r.run(); } finally { silencioso = false; }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) return; // evita doble disparo
        Object src = e.getSource();
        if (src == vista.btnRegistrar) registrarCategoria();
        else if (src == vista.btnBuscar) buscarCategoria();
        else if (src == vista.btnActualizar) actualizarCategoria();
        else if (src == vista.btnBorrar) borrarCategoria();
    }

    private void registrarCategoria() {
        String nombre = vista.txtNombreCategoria.getText().trim();
        String descripcion = vista.txtDescripcion.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ El nombre de la categoría es obligatorio.");
            return;
        }

        ModeloCategoria categoria = new ModeloCategoria(0, nombre, descripcion);
        if (dao.registrarCategoria(categoria)) {
            ModeloCategoria creada = dao.buscarPorNombre(nombre);
            if (creada != null) {
                currentId = creada.getIdCategoria();
                try { vista.txtIdCategoria.setText(String.valueOf(currentId)); } catch (Exception ignore) {}
            }
            JOptionPane.showMessageDialog(vista, "✅ Categoría registrada correctamente.");
            limpiarCamposSilencioso();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ No se pudo registrar la categoría (¿nombre duplicado?).");
        }
    }

    private void buscarCategoria() {
        String nombre = vista.txtNombreCategoria.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "🔎 Ingresa el nombre de la categoría para buscar.");
            return;
        }

        ModeloCategoria categoria = dao.buscarPorNombre(nombre);
        if (categoria != null) {
            currentId = categoria.getIdCategoria();
            runSilencioso(() -> {
                vista.txtNombreCategoria.setText(categoria.getNombre());
                vista.txtDescripcion.setText(categoria.getDescripcion());
                try { vista.txtIdCategoria.setText(String.valueOf(currentId)); } catch (Exception ignore) {}
            });
        } else {
            JOptionPane.showMessageDialog(vista, "ℹ️ No se encontró una categoría con ese nombre.");
            limpiarCamposSilencioso();
        }
    }

    private void actualizarCategoria() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(vista, "📌 Busca primero la categoría por nombre para obtener su ID.");
            return;
        }
        String nombre = vista.txtNombreCategoria.getText().trim();
        String descripcion = vista.txtDescripcion.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ El nombre no puede estar vacío.");
            return;
        }

        ModeloCategoria categoria = new ModeloCategoria(currentId, nombre, descripcion);
        if (dao.actualizarCategoria(categoria)) {
            JOptionPane.showMessageDialog(vista, "✅ Categoría actualizada correctamente.");
            limpiarCamposSilencioso();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ No se pudo actualizar la categoría.");
        }
    }

    private void borrarCategoria() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(vista, "📌 Busca primero la categoría por nombre para poder eliminarla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(
                vista,
                "¿Eliminar la categoría seleccionada (ID " + currentId + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (r != JOptionPane.YES_OPTION) return;

        if (dao.borrarCategoria(currentId)) {
            JOptionPane.showMessageDialog(vista, "🗑️ Categoría eliminada correctamente.");
            limpiarCamposSilencioso();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar la categoría.");
        }
    }

    /** Limpia campos y resetea ID sin disparar validaciones de la vista */
    private void limpiarCamposSilencioso() {
        runSilencioso(this::limpiarCampos);
    }

    private void limpiarCampos() {
        currentId = null;
        try { vista.txtIdCategoria.setText(""); } catch (Exception ignore) {}
        vista.txtNombreCategoria.setText("");
        vista.txtDescripcion.setText("");
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
