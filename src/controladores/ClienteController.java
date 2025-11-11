/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

/**
 *
 * @author Dulce
 */
import Implementacion.ClientesImp;
import Modelo.ModeloCliente;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Vistas.PanelClientes; // 👉 Cambia si tu vista se llama diferente

public class ClienteController {

    private final PanelClientes vista;
    private final ClientesImp dao;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ClienteController(PanelClientes vista) {
        this.vista = vista;
        this.dao = new ClientesImp();
    }

    // 🟢 Cargar todos los clientes en la tabla
    public void cargarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblclientes().getModel();
        modelo.setRowCount(0);

        List<ModeloCliente> lista = dao.listar();
        for (ModeloCliente c : lista) {
            modelo.addRow(new Object[]{
                c.getNit(),
                c.getPrimerNombre(),
                c.getSegundoNombre(),
                c.getPrimerApellido(),
                c.getSegundoApellido(),
                c.getApellidoCasada(),
                c.getDireccion(),
                c.getTelefono(),
                c.getEmail(),
                c.getTipoCliente(),
                c.getLimiteCredito(),
                c.getEstado(),
                c.getFechaRegistro()
            });
        }
    }

    // 🟩 Agregar cliente
    public void agregarCliente() {
        ModeloCliente c = obtenerClienteDeVista();
        if (c == null) return;

        if (dao.agregar(c)) {
            JOptionPane.showMessageDialog(vista, "✅ Cliente agregado correctamente.");
            limpiarCampos();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ Error al agregar el cliente.");
        }
    }

    // 🟦 Actualizar cliente
    public void actualizarCliente() {
        ModeloCliente c = obtenerClienteDeVista();
        if (c == null) return;

        if (dao.actualizar(c)) {
            JOptionPane.showMessageDialog(vista, "✅ Cliente actualizado correctamente.");
            limpiarCampos();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "❌ Error al actualizar el cliente.");
        }
    }

    // 🟥 Eliminar cliente
    public void eliminarCliente() {
        String nit = vista.getTxtNIT().getText();
        if (nit == null || nit.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingrese el NIT del cliente a eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Seguro que deseas eliminar el cliente con NIT " + nit + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(nit)) {
                JOptionPane.showMessageDialog(vista, "🗑️ Cliente eliminado correctamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "❌ Error al eliminar el cliente.");
            }
        }
    }

    // 🔍 Buscar cliente por NIT
    public void buscarCliente() {
        String nit = vista.getTxtNIT().getText();
        if (nit == null || nit.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingrese el NIT a buscar.");
            return;
        }

        ModeloCliente c = dao.buscarPorNit(nit);
        if (c != null) {
            vista.getTxtNombreCliente1().setText(c.getPrimerNombre());
            vista.getTxtNombreCliente2().setText(c.getSegundoNombre());
            vista.getTxtApellidoCliente1().setText(c.getPrimerApellido());
            vista.getTxtApellidoCliente2().setText(c.getSegundoApellido());
            vista.getTxtApellidoCasado().setText(c.getApellidoCasada());
            vista.getTxtDireccion().setText(c.getDireccion());
            vista.getTxtTelefono().setText(c.getTelefono());
            vista.getTxtEmail().setText(c.getEmail());
            vista.getTxtTipoCliente().setText(c.getTipoCliente());
            vista.getTxtLimiteCredito().setText(
                c.getLimiteCredito() != null ? c.getLimiteCredito().toString() : ""
            );
            vista.getTxtEstado().setText(c.getEstado());
            vista.getTxtFechaRegistro().setText(
                c.getFechaRegistro() != null ? c.getFechaRegistro().toString() : ""
            );
        } else {
            JOptionPane.showMessageDialog(vista, "❌ No se encontró cliente con ese NIT.");
        }
    }

    // 🧩 Crear objeto ModeloCliente desde los campos del formulario
    private ModeloCliente obtenerClienteDeVista() {
        try {
            ModeloCliente c = new ModeloCliente();
            c.setNit(vista.getTxtNIT().getText());
            c.setPrimerNombre(vista.getTxtNombreCliente1().getText());
            c.setSegundoNombre(vista.getTxtNombreCliente2().getText());
            c.setPrimerApellido(vista.getTxtApellidoCliente1().getText());
            c.setSegundoApellido(vista.getTxtApellidoCliente2().getText());
            c.setApellidoCasada(vista.getTxtApellidoCasado().getText());
            c.setDireccion(vista.getTxtDireccion().getText());
            c.setTelefono(vista.getTxtTelefono().getText());
            c.setEmail(vista.getTxtEmail().getText());
            c.setTipoCliente(vista.getTxtTipoCliente().getText());
            c.setEstado(vista.getTxtEstado().getText());

            // Manejo de límite de crédito
            String limiteStr = vista.getTxtLimiteCredito().getText();
            if (limiteStr == null || limiteStr.trim().isEmpty()) {
                c.setLimiteCredito(null);
            } else {
                c.setLimiteCredito(Double.parseDouble(limiteStr));
            }

            // Manejo de fecha
            String fechaTexto = vista.getTxtFechaRegistro().getText();
            if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
                c.setFechaRegistro(null);
            } else {
                c.setFechaRegistro(LocalDate.parse(fechaTexto, formatoFecha));
            }

            return c;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "⚠️ Error al leer los datos del formulario: " + e.getMessage());
            return null;
        }
    }

    // 🧹 Limpiar formulario
    private void limpiarCampos() {
        vista.getTxtNIT().setText("");
        vista.getTxtNombreCliente1().setText("");
        vista.getTxtNombreCliente2().setText("");
        vista.getTxtApellidoCliente1().setText("");
        vista.getTxtApellidoCliente2().setText("");
        vista.getTxtApellidoCasado().setText("");
        vista.getTxtDireccion().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtTipoCliente().setText("");
        vista.getTxtLimiteCredito().setText("");
        vista.getTxtEstado().setText("");
        vista.getTxtFechaRegistro().setText("");
    }
}