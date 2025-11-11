package Controlador;

import Implementacion.SesionInicioImp;
import Modelo.ModeloInicioUsuario;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Modelo.ModeloVistaInicio;
import Vistas.VistaAdmin;
import Vistas.VistaVendedor;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
//hb
/**
 *
 * @author jhosu
 */


// IMPORTANTE: usa tu util BCrypt
import Seguridad.PasswordUtil;

/**
 * Controlador de la vista de inicio de sesión
 */
public class ControladorVistaInicio implements MouseListener {

    private final ModeloVistaInicio modelo;
    private final SesionInicioImp implementacion = new SesionInicioImp();

    public ControladorVistaInicio(ModeloVistaInicio modelo) {
        this.modelo = modelo;
        configurarEnter();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent().equals(modelo.getVistaInicio().btnAcceder)) {
            inputIsEmpty();
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent().equals(modelo.getVistaInicio().btnAcceder)) {
            modelo.getVistaInicio().btnAcceder.setBackground(new Color(50, 95, 110));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent().equals(modelo.getVistaInicio().btnAcceder)) {
            modelo.getVistaInicio().btnAcceder.setBackground(new Color(75, 128, 146));
        }
    }

    // ============================
    // VALIDACIÓN PRINCIPAL
    // ============================
    public void validarUsuario(String tipoUsuario,
                               String usuarioIngresado,
                               String contraIngresada,
                               String usuarioEncontrado,
                               String hashOPlainEncontrado,
                               boolean usuarioActivo) {

        if (tipoUsuario == null || usuarioEncontrado == null || hashOPlainEncontrado == null) {
            mostrarError("Error al Iniciar Sesión, usuario o contraseña incorrectos");
            limpiarDatos();
            return;
        }

        if (!usuarioActivo) {
            mostrarError("Error al Iniciar Sesión, el usuario está dado de baja");
            limpiarDatos();
            return;
        }

        // Compara usuario (según tu diseño actual, deben ser iguales)
        if (usuarioIngresado == null || !usuarioIngresado.equalsIgnoreCase(usuarioEncontrado)) {
            mostrarError("Usuario o contraseña incorrectos.");
            limpiarDatos();
            return;
        }

        // Verifica contraseña (BCrypt o texto plano legado)
        boolean passwordOK = verificarPassword(contraIngresada, hashOPlainEncontrado);
        if (!passwordOK) {
            mostrarError("Usuario o contraseña incorrectos.");
            limpiarDatos();
            return;
        }

        // Usuario OK: actualizar “sesión”/modelo y navegar
        ModeloInicioUsuario modeloInicioUsuarioActivo = new ModeloInicioUsuario();
        modeloInicioUsuarioActivo.setUsuarioActual(usuarioEncontrado);

        modelo.setUsuario(usuarioEncontrado);
        ModeloVistaInicio.setTipoUsuario(tipoUsuario);
        modelo.setUsuarioActivo(true);

        redirigirSegunTipo(tipoUsuario);
    }

    /**
     * Verifica la contraseña del login contra lo almacenado:
     * - Si es hash BCrypt, usa PasswordUtil.verify
     * - Si es texto plano (dato legado), compara equals
     */
    private boolean verificarPassword(String plainIngresada, String almacenadaBD) {
        if (plainIngresada == null || almacenadaBD == null) return false;

        // Detecta BCrypt por prefijo $2a/$2b/$2y
        if (PasswordUtil.isHash(almacenadaBD)) {
            return PasswordUtil.verify(plainIngresada, almacenadaBD);
        } else {
            // Compatibilidad con datos viejos
            return plainIngresada.equals(almacenadaBD);
            // Si quieres auto-actualizar a hash aquí, necesitas un método en SesionInicioImp/DAO
            // que haga UPDATE a la fila del usuario con PasswordUtil.hash(plainIngresada).
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR INICIO DE SESIÓN", JOptionPane.ERROR_MESSAGE);
    }

    private void redirigirSegunTipo(String tipoUsuario) {
        final String ADMIN = "1";     // o "ADMINISTRADOR"
        final String VENDEDOR = "2";  // o "VENDEDOR"

        JFrame actual = modelo.getVistaInicio();
        try {
            if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase(ADMIN)) {
                VistaAdmin vistaAdmin = new VistaAdmin();
                vistaAdmin.setLocationRelativeTo(null);
                vistaAdmin.setVisible(true);
            } else if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase(VENDEDOR)) {
                VistaVendedor vistaVendedor = new VistaVendedor();
                vistaVendedor.setLocationRelativeTo(null);
                vistaVendedor.setVisible(true);
            } else {
                mostrarError("Tipo de usuario no reconocido: " + tipoUsuario);
                return;
            }
        } finally {
            if (actual != null) actual.dispose();
        }
    }

    // ============================
    // Captura de inputs
    // ============================
    public void inputIsEmpty() {
        String u = modelo.getVistaInicio().txtUsuario.getText();
        String p = String.valueOf(modelo.getVistaInicio().txtPassword.getPassword());

        if (u == null || u.isBlank() || p == null || p.isBlank()) {
            JOptionPane.showInternalMessageDialog(null,
                    "Por favor ingresa usuario y contraseña",
                    "ERROR \"DATOS VACÍOS\"",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            capturaDeDatos();
        }
    }

    public void capturaDeDatos() {
        try {
            String usuarioIngresado = modelo.getVistaInicio().txtUsuario.getText();
            String contraseniaIngresada = String.valueOf(modelo.getVistaInicio().txtPassword.getPassword());

            // Llama a la implementación que consulta en BD (no tocar la vista)
            ModeloVistaInicio model = implementacion.consultaUsuario(usuarioIngresado, contraseniaIngresada);

            // OJO: estos getters vienen de tu SesionInicioImp/ModeloVistaInicio
            String usuarioEncontrado    = model.getUsuarioEncontrado();
            String hashOPlainEncontrado = model.getContraseniaEncontrada(); // ahora normalmente es HASH
            String tipoDeUsuario        = model.getTipoUsuario();
            boolean usuarioActivo       = model.isUsuarioActivo();

            validarUsuario(tipoDeUsuario, usuarioIngresado, contraseniaIngresada,
                           usuarioEncontrado, hashOPlainEncontrado, usuarioActivo);

        } catch (NullPointerException ex) {
            System.out.println("ERROR: Datos no encontrados");
            mostrarError("No se encontró el usuario o hubo un problema con la consulta.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + ex.getMessage(),
                    "ERROR CRÍTICO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================
    // UX: Enter para enviar
    // ============================
    public void configurarEnter() {
        JFrame frame = modelo.getVistaInicio();
        JPanel contentPanel = (JPanel) frame.getContentPane();

        InputMap inputMap = contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "validarInput");
        actionMap.put("validarInput", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputIsEmpty();
            }
        });
    }

    public void limpiarDatos() {
        modelo.getVistaInicio().txtUsuario.setText("");
        modelo.getVistaInicio().txtPassword.setText("");
    }
}

