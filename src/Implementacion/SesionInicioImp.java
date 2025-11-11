package Implementacion;

import Conector.DBConnection;
import Interfaces.ISesionInicio;
import Modelo.ModeloVistaInicio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Implementación del inicio de sesión CON HASH:
 * - No filtra por contraseña en SQL.
 * - Busca por identificador (email o primer_nombre).
 * - Devuelve hash/legacy al controlador para que verifique con BCrypt.
 */
public class SesionInicioImp implements ISesionInicio {

    private final DBConnection conector = new DBConnection();

    @Override
    public ModeloVistaInicio consultaUsuario(String usuario, String contrasenia) {

        // Normaliza entradas
        final String ident = (usuario == null) ? "" : usuario.trim();

        // Si no mandan nada, corto
        ModeloVistaInicio modelo = new ModeloVistaInicio();
        if (ident.isEmpty()) {
            modelo.setUsuarioActivo(false);
            return modelo;
        }

        // Esta consulta NO compara contraseñas. Solo ubica por email o primer_nombre
        final String SQL = """
            SELECT
                id_usuario,
                contrasena,        -- puede ser hash BCrypt o texto plano heredado
                primer_nombre,
                email,
                tipo_usuario,
                estado
            FROM usuarios
            WHERE (UPPER(email) = UPPER(?) OR UPPER(primer_nombre) = UPPER(?))
            FETCH FIRST 1 ROWS ONLY
        """;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conector.conectar();

            // Usa tu helper para crear el PreparedStatement
            ps = conector.preparar(SQL);
            ps.setString(1, ident);
            ps.setString(2, ident);

            rs = ps.executeQuery();
            if (!rs.next()) {
                // No existe usuario
                modelo.setUsuarioActivo(false);
                return modelo;
            }

            long   id          = rs.getLong("id_usuario");
            String hashOPlain  = rs.getString("contrasena");
            String primerNombre= rs.getString("primer_nombre");
            String email       = rs.getString("email");
            String tipo        = rs.getString("tipo_usuario");
            String estado      = rs.getString("estado");

            // Devuelve el mismo identificador que el usuario escribió (email o nombre)
            String usuarioEncontrado = (ident.contains("@") ? email : primerNombre);

            // Set estáticos (tu controlador los usa)
            ModeloVistaInicio.setIdUsuarioEncontrado((int) id);
            ModeloVistaInicio.setUsuarioEncontrado(usuarioEncontrado);
            ModeloVistaInicio.setContraseniaEncontrada(hashOPlain); // HASH o texto heredado
            ModeloVistaInicio.setTipoUsuario(tipo);

            // También en instancia
            modelo.setUsuario(usuarioEncontrado);
            modelo.setContrasenia(hashOPlain);

            // estado puede ser 'ACTIVO'/'INACTIVO' o '1'/'0'
            boolean activo = false;
            if (estado != null) {
                activo = "ACTIVO".equalsIgnoreCase(estado)
                      || "1".equals(estado)
                      || "TRUE".equalsIgnoreCase(estado);
            }
            modelo.setUsuarioActivo(activo);

        } catch (Exception e) {
            System.err.println("Error en consultaUsuario: " + e.getMessage());
            modelo.setUsuarioActivo(false);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { conector.desconectar(); } catch (Exception ignore) {}
        }

        return modelo;
    }
}
