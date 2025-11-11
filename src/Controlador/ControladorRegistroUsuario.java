/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;


import javax.swing.JOptionPane;

/**
 *
 * @author jhosu
 */


import Implementacion.UsuarioDAOImpl;
import Modelo.ModeloRegistroUsuario;
import Modelo.Usuario;
import Vistas.PanelRegistroUsuario;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ControladorRegistroUsuario implements MouseListener {

    private final ModeloRegistroUsuario modelo;
    private final UsuarioDAOImpl dao = new UsuarioDAOImpl(); // <- DAO para crear/buscar

    public ControladorRegistroUsuario(ModeloRegistroUsuario modelo) {
        this.modelo = modelo;

        PanelRegistroUsuario v = this.modelo.getVistaRegistro();
        v.btnRegistrar.addMouseListener(this);
        v.getRegistrarLabel().addMouseListener(this); // opción 2
        v.btnBorrar.addMouseListener(this);
        v.btnBuscar.addMouseListener(this);
    }

    @Override
public void mouseClicked(MouseEvent e) {
    PanelRegistroUsuario v = modelo.getVistaRegistro();
    Object src = e.getComponent();
    String name = (src instanceof JComponent) ? ((JComponent) src).getName() : null;

    if (src.equals(v.btnRegistrar) || src.equals(v.getRegistrarLabel())) {
        onRegistrar();

    } else if (src.equals(v.btnBuscar)) {
        onBuscar();  // ← tu método de buscar

    } else if (src.equals(v.btnBorrar)) {
    onBorrar();


    // 👉 Nuevo: botón/label “Actualizar”
    } else if ("btnActualizar".equals(name) || "lblActualizar".equals(name)
            || (src instanceof JComponent
                && ((JComponent) src).getParent() != null
                && "btnActualizar".equals(((JComponent) src).getParent().getName()))) {
        onActualizar();
    }
}

private void onBorrar() {
    PanelRegistroUsuario v = modelo.getVistaRegistro();

    // 1) Validar ID
    String idStr = v.txtIdUsuario.getText().trim();
    if (idStr.isEmpty()) {
        JOptionPane.showMessageDialog(v,
                "Debes ingresar el ID del usuario a borrar (usa BUSCAR primero si no lo sabes).",
                "Falta ID", JOptionPane.WARNING_MESSAGE);
        return;
    }

    long id;
    try {
        id = Long.parseLong(idStr);
    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(v, "El ID debe ser numérico.", "ID inválido", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // 2) Confirmación
    int resp = JOptionPane.showConfirmDialog(
            v,
            "¿Seguro que deseas eliminar el usuario con ID " + id + "?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );
    if (resp != JOptionPane.YES_OPTION) return;

    // 3) Eliminar en BD
    try {
        UsuarioDAOImpl dao = new UsuarioDAOImpl();
        boolean ok = dao.eliminar(id);

        if (ok) {
            JOptionPane.showMessageDialog(v, "Usuario eliminado correctamente.");
            limpiarFormulario(); // deja el form vacío
        } else {
            JOptionPane.showMessageDialog(v,
                    "No se pudo eliminar (verifica que el ID exista).",
                    "No eliminado", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        // Manejo amable de FK (Oracle ORA-02292)
        String msg = ex.getMessage() == null ? "" : ex.getMessage();
        if (msg.contains("ORA-02292")) {
            JOptionPane.showMessageDialog(v,
                    "No se puede eliminar porque el usuario tiene registros relacionados (FK).\n" +
                    "Sugerencias:\n" +
                    " • Elimina/actualiza primero los registros dependientes (ventas, auditoría, etc.).\n" +
                    " • O usa un 'estado' INACTIVO en lugar de borrar.",
                    "Restricción de integridad (ORA-02292)", JOptionPane.ERROR_MESSAGE);
        } else {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(v,
                    "Error al eliminar: " + msg,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    // -------------------------
    // BUSCAR
    // -------------------------
    private void onBuscar() {
        PanelRegistroUsuario v = modelo.getVistaRegistro();

        String idStr = v.txtIdUsuario.getText().trim();
        String nombre = v.txtNombrePersonal.getText().trim(); // aquí usas "Primer Nombre" como usuario

        if (idStr.isEmpty() && nombre.isEmpty()) {
            JOptionPane.showMessageDialog(v, "Ingrese ID o Primer Nombre para buscar.");
            return;
        }

        Long id = null;
        try {
            if (!idStr.isEmpty()) id = Long.parseLong(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(v, "El ID debe ser numérico.");
            return;
        }

        try {
            Usuario u = dao.buscarPorIdONombre(id, nombre.isEmpty() ? null : nombre);
            if (u == null) {
                JOptionPane.showMessageDialog(v, "No se encontró el usuario.");
                return;
            }

            // Rellenar formulario
            v.txtIdUsuario.setText(u.getIdUsuario() == null ? "" : String.valueOf(u.getIdUsuario()));
            v.boxTipoUsuario.setSelectedItem(
                    u.getTipoUsuario() != null && (u.getTipoUsuario().equals("1") || u.getTipoUsuario().equals("2"))
                            ? (u.getTipoUsuario().equals("1") ? "ADMIN" : "VENDEDOR")
                            : u.getTipoUsuario()
            );
            v.txtPassword.setText(u.getContrasena() == null ? "" : u.getContrasena());
            v.txtNombrePersonal.setText(u.getPrimerNombre() == null ? "" : u.getPrimerNombre());
            v.txtApellidoPersonal.setText(u.getPrimerApellido() == null ? "" : u.getPrimerApellido());
            v.txtApellidoCadado.setText(u.getApellidoCasado() == null ? "" : u.getApellidoCasado());
            v.txtTelefono.setText(u.getTelefono() == null ? "" : u.getTelefono());
            v.txtEmail.setText(u.getEmail() == null ? "" : u.getEmail());
            v.txtIdRol.setText(u.getIdRol() == null ? "" : String.valueOf(u.getIdRol()));
            v.txtEstado.setText(u.getEstado() == null ? "" : u.getEstado());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(v, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------------
    // REGISTRAR (tu lógica existente)
    // -------------------------
    private void onRegistrar() {
        try {
            PanelRegistroUsuario v = modelo.getVistaRegistro();

            if (v.txtNombrePersonal.getText().trim().isEmpty()
                    || v.txtApellidoPersonal.getText().trim().isEmpty()
                    || v.txtTelefono.getText().trim().isEmpty()
                    || v.txtIdRol.getText().trim().isEmpty()
                    || v.boxTipoUsuario.getSelectedItem() == null
                    || v.txtPassword.getPassword().length == 0) {
                JOptionPane.showMessageDialog(v,
                        "Por favor ingresa todos los datos obligatorios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String tipoUI = ((String) v.boxTipoUsuario.getSelectedItem()).trim().toUpperCase();
            String tipoBD;
            switch (tipoUI) {
                case "ADMIN": case "ADMINISTRADOR": tipoBD = "1"; break;
                case "VENDEDOR":                    tipoBD = "2"; break;
                case "1": case "2":                 tipoBD = tipoUI; break;
                default: throw new IllegalArgumentException("Tipo de usuario inválido: " + tipoUI);
            }

            String estadoInput = v.txtEstado.getText().trim().toUpperCase();
            String estadoBD = estadoInput.isEmpty() ? "ACTIVO" : estadoInput;

            Usuario u = new Usuario();
            u.setTipoUsuario(tipoBD);
            u.setContrasena(new String(v.txtPassword.getPassword()));
            u.setPrimerNombre(v.txtNombrePersonal.getText().trim());
            u.setPrimerApellido(v.txtApellidoPersonal.getText().trim());
            String apCasado = v.txtApellidoCadado.getText().trim();
            u.setApellidoCasado(apCasado.isEmpty() ? null : apCasado);
            String email = v.txtEmail.getText().trim();
            u.setEmail(email.isEmpty() ? null : email);
            u.setTelefono(v.txtTelefono.getText().trim());
            u.setIdRol(Long.parseLong(v.txtIdRol.getText().trim()));
            u.setEstado(estadoBD);

            Long id = dao.crear(u);
            JOptionPane.showMessageDialog(v, "Usuario registrado. ID: " + id, "OK", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(modelo.getVistaRegistro(),
                    "Error al registrar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onActualizar() {
    PanelRegistroUsuario v = modelo.getVistaRegistro();

    try {
        // Validación mínima
        String idStr = v.txtIdUsuario.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(v, "Debes ingresar el ID del usuario a actualizar.");
            return;
        }

        Long id = Long.parseLong(idStr);

        // Construir entidad con los datos del formulario
        Usuario u = new Usuario();
        u.setIdUsuario(id);
        u.setPrimerNombre(v.txtNombrePersonal.getText().trim());
        u.setPrimerApellido(v.txtApellidoPersonal.getText().trim());
        u.setApellidoCasado(v.txtApellidoCadado.getText().trim().isEmpty() ? null : v.txtApellidoCadado.getText().trim());
        u.setTelefono(v.txtTelefono.getText().trim());
        u.setEmail(v.txtEmail.getText().trim().isEmpty() ? null : v.txtEmail.getText().trim());
        u.setContrasena(new String(v.txtPassword.getPassword()));
        u.setIdRol(Long.parseLong(v.txtIdRol.getText().trim()));

        // Mapeo de tipo de usuario tal como lo usas en registrar
        String tipoUI = ((String) v.boxTipoUsuario.getSelectedItem()).trim().toUpperCase();
        switch (tipoUI) {
            case "ADMIN":
            case "ADMINISTRADOR":
                u.setTipoUsuario("1"); break;
            case "VENDEDOR":
                u.setTipoUsuario("2"); break;
            case "1": case "2":
                u.setTipoUsuario(tipoUI); break;
            default:
                throw new IllegalArgumentException("Tipo de usuario inválido: " + tipoUI);
        }

        // Estado (ajusta a tu CHECK)
        String estadoInput = v.txtEstado.getText().trim();
        u.setEstado(estadoInput.isEmpty() ? "ACTIVO" : estadoInput.toUpperCase());

        // Llamar DAO
        UsuarioDAOImpl dao = new UsuarioDAOImpl();
        boolean ok = dao.actualizar(u);

        if (ok) {
            JOptionPane.showMessageDialog(v, "Usuario actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(v, "No se pudo actualizar el usuario (verifica el ID).");
        }

    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(v, "ID de usuario e ID de rol deben ser numéricos.");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(v, "Error al actualizar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void limpiarFormulario() {
        PanelRegistroUsuario v = modelo.getVistaRegistro();
        v.txtIdUsuario.setText("");
        v.boxTipoUsuario.setSelectedIndex(0);
        v.txtPassword.setText("");
        v.txtNombrePersonal.setText("");
        v.txtApellidoPersonal.setText("");
        v.txtApellidoCadado.setText("");
        v.txtTelefono.setText("");
        v.txtEmail.setText("");
        v.txtIdRol.setText("");
        v.txtEstado.setText("");
    }

    // Efectos visuales
   private static final Color ACT_BASE  = new Color(70,130,180);
private static final Color ACT_HOVER = new Color(60,115,160);

@Override
public void mouseEntered(MouseEvent e) {
    PanelRegistroUsuario v = modelo.getVistaRegistro();
    Object src = e.getComponent();
    String name = (src instanceof JComponent) ? ((JComponent) src).getName() : null;

    if (src.equals(v.btnRegistrar)) {
        v.btnRegistrar.setBackground(new Color(50,95,110));
    } else if (src.equals(v.btnBorrar)) {
        v.btnBorrar.setBackground(new Color(50,95,110));
    } else if ("btnActualizar".equals(name)) {
        ((JComponent) src).setBackground(ACT_HOVER);
    } else if ("lblActualizar".equals(name) && ((JComponent) src).getParent() != null) {
        ((JComponent) src).getParent().setBackground(ACT_HOVER);
    }
}

@Override
public void mouseExited(MouseEvent e) {
    PanelRegistroUsuario v = modelo.getVistaRegistro();
    Object src = e.getComponent();
    String name = (src instanceof JComponent) ? ((JComponent) src).getName() : null;

    if (src.equals(v.btnRegistrar)) {
        v.btnRegistrar.setBackground(new Color(75,128,146));
    } else if (src.equals(v.btnBorrar)) {
        v.btnBorrar.setBackground(new Color(75,128,146));
    } else if ("btnActualizar".equals(name)) {
        ((JComponent) src).setBackground(ACT_BASE);  // <- vuelve al base
    } else if ("lblActualizar".equals(name) && ((JComponent) src).getParent() != null) {
        ((JComponent) src).getParent().setBackground(ACT_BASE);    // <- vuelve al base
    }
}



    

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
}
