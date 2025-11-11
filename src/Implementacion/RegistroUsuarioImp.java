package Implementacion;

import Conector.DBConnection;
import Conector.SQL;
import Interfaces.IRegistroUsuario;
import Modelo.ModeloRegistroUsuario;
import javax.swing.DefaultComboBoxModel;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author jhosu
 */


public class RegistroUsuarioImp implements IRegistroUsuario {

    DBConnection conector = new DBConnection();
    SQL sql = new SQL();
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public boolean guardarUsuario(ModeloRegistroUsuario modelo) {
        boolean resultado = false;
        conector.conectar();
        // tu código de guardado…
        return resultado;
    }

    @Override
    public DefaultComboBoxModel mostrarTiposUsuarios() { return null; }

    @Override
    public boolean elimiarUsuario(String nombreU) { return false; }

    @Override
    public ModeloRegistroUsuario validarUsuario(String nombreU, String contraU) { return null; }

    // =======================
    // ✅ NUEVO: BUSCAR USUARIO
    // =======================
    @Override
    public ModeloRegistroUsuario buscarUsuarioPorIdONombre(String idUsuario, String usuario) {
        ModeloRegistroUsuario modelo = null;
        String id = (idUsuario == null) ? "" : idUsuario.trim();
        String us = (usuario == null) ? "" : usuario.trim();

        // Ajusta nombres de tabla/columnas si difieren en tu BD
        final String QUERY =
            "SELECT ID_USUARIO, NOMBRE, APELLIDO, TELEFONO, EMAIL, USUARIO, CONTRASENIA, TIPO_USUARIO, ACTIVO " +
            "FROM USUARIOS " +
            "WHERE (? <> '' AND ID_USUARIO = ?) OR (? <> '' AND USUARIO = ?) " +
            "FETCH FIRST 1 ROWS ONLY";

        try {
            conector.conectar();
            ps = conector.preparar(QUERY);
            ps.setString(1, id);
            ps.setString(2, id);
            ps.setString(3, us);
            ps.setString(4, us);

            rs = ps.executeQuery();
            if (rs.next()) {
                modelo = new ModeloRegistroUsuario();
                // 👉 Usa los NOMBRES DE SETTERS que ya tienes en tu modelo:
                // (los siguientes nombres coinciden con lo que ya usas en guardarUsuario)
                modelo.setNombreUsuario(rs.getString("NOMBRE"));
                modelo.setApellidoUsuario(rs.getString("APELLIDO"));
                modelo.setNumeroUsuario(rs.getString("TELEFONO"));
                modelo.setEmailUsuario(rs.getString("EMAIL"));
                modelo.setUsuario(rs.getString("USUARIO"));
                modelo.setContraseniaUsuario(rs.getString("CONTRASENIA"));
                modelo.setTipoUsuario(rs.getString("TIPO_USUARIO"));
                modelo.setActivoUsuario(rs.getBoolean("ACTIVO"));
                // Si tu modelo tiene setIdUsuario, descomenta:
                // modelo.setIdUsuario(rs.getString("ID_USUARIO"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error metodo buscarUsuarioPorIdONombre: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { conector.desconectar(); } catch (Exception ignored) {}
        }
        return modelo;
    }
}
