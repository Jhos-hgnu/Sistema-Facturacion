/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Interfaces.UsuarioDAO;
import Modelo.Usuario;
import Seguridad.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final DBConnection db;

    public UsuarioDAOImpl() {
        this.db = new DBConnection();
        this.db.conectar(); // abre conexión interna en DBConnection
    }

    // ============================
    // Helpers de normalización
    // ============================
   // Devuelve SIEMPRE el valor que acepta el CHECK: 'ADMIN' o 'VENDEDOR'.
// Acepta tanto '1'/'2' como texto y los normaliza a TEXTO.
private String normalizarTipoUsuario(String tipo) {
    if (tipo == null) throw new IllegalArgumentException("tipo_usuario es obligatorio");
    String t = tipo.trim().toUpperCase();
    switch (t) {
        case "1":
        case "ADMIN":
        case "ADMINISTRADOR":
            return "ADMIN";
        case "2":
        case "VENDEDOR":
            return "VENDEDOR";
        default:
            throw new IllegalArgumentException(
                "tipo_usuario inválido: " + tipo + " (usa ADMIN/VENDEDOR o 1/2)"
            );
    }
}


    private String normalizarEstado(String estado) {
        if (estado == null) throw new IllegalArgumentException("estado es obligatorio");
        String e = estado.trim().toUpperCase();
        if (e.equals("ACTIVO") || e.equals("INACTIVO")) return e;
        // Si tu CHECK es numérico ("1","0"), cambia a:
        // if (e.equals("1") || e.equals("0")) return e;
        throw new IllegalArgumentException("estado inválido: " + estado + " (esperado: ACTIVO/INACTIVO)");
    }

    // ============================
    // CREATE
    // ============================
    @Override
    public Long crear(Usuario u) throws Exception {
        String sql = """
            INSERT INTO usuarios (
              tipo_usuario, contrasena, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido,
              apellido_casado, email, telefono, id_rol, estado, fecha_creacion
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)
            RETURNING id_usuario INTO ?
        """;

        String tipoNormal   = normalizarTipoUsuario(u.getTipoUsuario());
        String estadoNormal = normalizarEstado(u.getEstado());
        if (u.getContrasena() == null || u.getContrasena().isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria");
        if (u.getPrimerNombre() == null || u.getPrimerNombre().isBlank())
            throw new IllegalArgumentException("El primer nombre es obligatorio");
        if (u.getPrimerApellido() == null || u.getPrimerApellido().isBlank())
            throw new IllegalArgumentException("El primer apellido es obligatorio");
        if (u.getTelefono() == null || u.getTelefono().isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio");
        if (u.getIdRol() == null)
            throw new IllegalArgumentException("id_rol es obligatorio");

        Connection cn = db.getConnection(); // normalmente autocommit=true
        PreparedStatement ps = null;
        try {
            ps = cn.prepareStatement(sql);
            int i = 1;
ps.setString(i++, mapTipoForDB(cn, u.getTipoUsuario()));
            ps.setString(i++, PasswordUtil.hash(u.getContrasena())); // <-- HASH AQUÍ
            ps.setString(i++, u.getPrimerNombre());
            ps.setString(i++, u.getSegundoNombre());
            ps.setString(i++, u.getPrimerApellido());
            ps.setString(i++, u.getSegundoApellido());
            ps.setString(i++, u.getApellidoCasado());
            ps.setString(i++, u.getEmail());
            ps.setString(i++, u.getTelefono());
            ps.setObject(i++, u.getIdRol(), Types.NUMERIC);
            ps.setString(i++, estadoNormal);

            // Parámetro de retorno
            OraclePreparedStatement ops = ps.unwrap(OraclePreparedStatement.class);
            ops.registerReturnParameter(i, OracleTypes.NUMBER);

            ops.executeUpdate();

            Long id = null;
            try (ResultSet rs = ops.getReturnResultSet()) {
                if (rs.next()) id = rs.getLong(1);
            }
            return id;
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception ignore) {}
        }
    }

    // ============================
    // UPDATE (dinámico para no tocar contraseña si viene vacía)
    // ============================
  @Override
public boolean actualizar(Usuario u) throws Exception {
    if (u.getIdUsuario() == null)
        throw new IllegalArgumentException("id_usuario es obligatorio para actualizar");

    boolean cambiarPassword = (u.getContrasena() != null && !u.getContrasena().isBlank());

    StringBuilder sb = new StringBuilder();
    sb.append("UPDATE usuarios SET ");
    sb.append("tipo_usuario = ?, ");
    if (cambiarPassword) sb.append("contrasena = ?, ");
    sb.append("primer_nombre = ?, ");
    sb.append("segundo_nombre = ?, ");
    sb.append("primer_apellido = ?, ");
    sb.append("segundo_apellido = ?, ");
    sb.append("apellido_casado = ?, ");
    sb.append("email = ?, ");
    sb.append("telefono = ?, ");
    sb.append("id_rol = ?, ");
    sb.append("estado = ? ");
    sb.append("WHERE id_usuario = ?");

    String sql = sb.toString();

    Connection cn = db.getConnection();
    boolean ac = cn.getAutoCommit();

    try (PreparedStatement ps = cn.prepareStatement(sql)) {
        int i = 1;

        // 🔒 TEXTO que cumple el CHECK ('ADMIN'/'VENDEDOR')
ps.setString(i++, mapTipoForDB(cn, u.getTipoUsuario()));

        if (cambiarPassword) {
            ps.setString(i++, PasswordUtil.hash(u.getContrasena()));
        }

        ps.setString(i++, u.getPrimerNombre());
        ps.setString(i++, u.getSegundoNombre());
        ps.setString(i++, u.getPrimerApellido());
        ps.setString(i++, u.getSegundoApellido());
        ps.setString(i++, u.getApellidoCasado());
        ps.setString(i++, u.getEmail());
        ps.setString(i++, u.getTelefono());
        ps.setObject(i++, u.getIdRol(), Types.NUMERIC);
        ps.setString(i++, normalizarEstado(u.getEstado()));
        ps.setObject(i++, u.getIdUsuario(), Types.NUMERIC);

        boolean ok = ps.executeUpdate() == 1;
        if (!ac) cn.commit();
        return ok;
    } catch (Exception ex) {
        if (!ac) try { cn.rollback(); } catch (Exception ignore) {}
        throw ex;
    }
}
// --- al inicio de la clase ---
private enum TipoMode { NUMERIC, TEXT }
private volatile TipoMode tipoModeCache = null;

// Descubre el modo del CHECK: NUMERIC('1','2') o TEXT('ADMIN','VENDEDOR')
private TipoMode resolveTipoMode(Connection cn) {
    if (tipoModeCache != null) return tipoModeCache;
    final String Q = """
        SELECT search_condition
        FROM user_constraints
        WHERE table_name = 'USUARIOS'
          AND constraint_type = 'C'
          AND constraint_name = 'CHK_USUARIOS_TIPO_12'
    """;
    try (PreparedStatement ps = cn.prepareStatement(Q);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            String cond = rs.getString(1);
            // heurística simple
            if (cond != null && cond.toUpperCase().contains("'ADMIN'")) {
                tipoModeCache = TipoMode.TEXT;
            } else {
                // si menciona '1' o '2' y no ADMIN, asumimos numérico
                tipoModeCache = TipoMode.NUMERIC;
            }
        } else {
            // si no encontramos constraint, por defecto NUMERIC para no romper
            tipoModeCache = TipoMode.NUMERIC;
        }
    } catch (Exception ignore) {
        tipoModeCache = TipoMode.NUMERIC;
    }
    return tipoModeCache;
}

// Mapea lo que viene del UI a lo que exige el CHECK
private String mapTipoForDB(Connection cn, String tipoUI) {
    if (tipoUI == null) throw new IllegalArgumentException("tipo_usuario es obligatorio");
    String t = tipoUI.trim().toUpperCase();
    TipoMode mode = resolveTipoMode(cn);

    // normaliza entradas del UI
    boolean isAdmin = t.equals("ADMIN") || t.equals("ADMINISTRADOR") || t.equals("1");
    boolean isVend  = t.equals("VENDEDOR") || t.equals("2");

    if (!isAdmin && !isVend) {
        throw new IllegalArgumentException("tipo_usuario inválido: " + tipoUI + " (usa ADMIN/VENDEDOR o 1/2)");
    }

    if (mode == TipoMode.TEXT) {
        return isAdmin ? "ADMIN" : "VENDEDOR";
    } else { // NUMERIC
        return isAdmin ? "1" : "2";
    }
}


    // ============================
    // DELETE (físico)
    // ============================
    @Override
    public boolean eliminar(Long idUsuario) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        Connection cn = db.getConnection();
        boolean ac = cn.getAutoCommit();

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            boolean ok = ps.executeUpdate() == 1;
            if (!ac) cn.commit();      // evita ORA-17273
            return ok;
        } catch (Exception ex) {
            if (!ac) try { cn.rollback(); } catch (Exception ignore) {}
            throw ex;
        }
    }

    // ============================
    // DELETE lógico (desactivar)
    // ============================
    public boolean desactivar(Long idUsuario) throws Exception {
        String sql = "UPDATE usuarios SET estado = 'INACTIVO' WHERE id_usuario = ?";
        Connection cn = db.getConnection();
        boolean ac = cn.getAutoCommit();

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            boolean ok = ps.executeUpdate() == 1;
            if (!ac) cn.commit();
            return ok;
        } catch (Exception ex) {
            if (!ac) try { cn.rollback(); } catch (Exception ignore) {}
            throw ex;
        }
    }

    // ============================
    // LOGIN (email o primer_nombre) con BCrypt + auto-upgrade
    // ============================
    public Usuario login(String identificador, String plainPassword) throws Exception {
        final String SQL = """
            SELECT
              id_usuario, contrasena, tipo_usuario, primer_nombre, segundo_nombre,
              primer_apellido, segundo_apellido, apellido_casado,
              email, telefono, id_rol, estado
            FROM usuarios
            WHERE (UPPER(email) = UPPER(?) OR UPPER(primer_nombre) = UPPER(?))
              AND estado = 'ACTIVO'
            FETCH FIRST 1 ROWS ONLY
        """;

        try (PreparedStatement ps = db.preparar(SQL)) {
            ps.setString(1, identificador);
            ps.setString(2, identificador);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                long id = rs.getLong("id_usuario");
                String stored = rs.getString("contrasena");

                boolean ok;
                if (PasswordUtil.isHash(stored)) {
                    ok = PasswordUtil.verify(plainPassword, stored);
                } else {
                    // compatibilidad con texto plano legado
                    ok = plainPassword.equals(stored);
                    if (ok) {
                        String newHash = PasswordUtil.hash(plainPassword);
                        try (PreparedStatement up = db.preparar(
                                "UPDATE usuarios SET contrasena=? WHERE id_usuario=?")) {
                            up.setString(1, newHash);
                            up.setLong(2, id);
                            up.executeUpdate();
                        }
                    }
                }

                if (!ok) return null;

                return map(rs); // devuelve el usuario logueado
            }
        }
    }

    // ============================
    // READ extras
    // ============================
    /** Busca por ID (preferente). Si ID es null, busca por PRIMER_NOMBRE (equals, case-insensitive). */
    public Usuario buscarPorIdONombre(Long idUsuario, String primerNombre) throws Exception {
        if (idUsuario == null && (primerNombre == null || primerNombre.trim().isEmpty())) {
            return null;
        }

        final String SQL_BY_ID = """
            SELECT * FROM usuarios
            WHERE id_usuario = ?
            FETCH FIRST 1 ROWS ONLY
        """;

        final String SQL_BY_NAME = """
            SELECT * FROM usuarios
            WHERE UPPER(primer_nombre) = UPPER(?)
            FETCH FIRST 1 ROWS ONLY
        """;

        if (idUsuario != null) {
            try (PreparedStatement ps = db.preparar(SQL_BY_ID)) {
                ps.setLong(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? map(rs) : null;
                }
            }
        } else {
            try (PreparedStatement ps = db.preparar(SQL_BY_NAME)) {
                ps.setString(1, primerNombre.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? map(rs) : null;
                }
            }
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long idUsuario) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Usuario> listar(int limit, int offset) throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }

    // ============================
    // Mapper
    // ============================
    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getLong("id_usuario"));
        if (rs.wasNull()) u.setIdUsuario(null);

        u.setTipoUsuario(rs.getString("tipo_usuario"));
        u.setContrasena(rs.getString("contrasena")); // hash (no lo muestres en UI)
        u.setPrimerNombre(rs.getString("primer_nombre"));
        u.setSegundoNombre(rs.getString("segundo_nombre"));
        u.setPrimerApellido(rs.getString("primer_apellido"));
        u.setSegundoApellido(rs.getString("segundo_apellido"));
        u.setApellidoCasado(rs.getString("apellido_casado"));
        u.setEmail(rs.getString("email"));
        u.setTelefono(rs.getString("telefono"));

        long idRol = rs.getLong("id_rol");
        u.setIdRol(rs.wasNull() ? null : idRol);

        u.setEstado(rs.getString("estado"));

        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) u.setFechaCreacion(ts.toLocalDateTime());
        return u;
    }
}
