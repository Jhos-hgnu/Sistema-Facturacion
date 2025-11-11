package Implementacion;

import Conector.DBConnection;
import Modelo.ModeloCompra;
import Modelo.ModeloAuditoria;
import Interfaces.IAuditoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para la tabla FARMACIA.COMPRAS
 * Compatible con Oracle 23c.
 * Auditoría agregada sin alterar funcionalidad existente.
 * Autor: Joshua Cirilo Alegría
 */
public class ModeloCompraImpl {

    private final DBConnection db;
    private final IAuditoria auditoriaDAO = new AuditoriaImpl(); // ✅ agregado

    public ModeloCompraImpl() {
        this.db = new DBConnection();
    }

    // ======================================================
    // ➕ AGREGAR
    // ======================================================
    public boolean agregar(ModeloCompra compra) {
        String sql = """
            INSERT INTO FARMACIA.COMPRAS
            (ID_PROVEEDOR, FECHA, DOCUMENTO, TOTAL_BRUTO, FORMA_PAGO, ESTADO, ID_USUARIO, FECHA_COMPRA)
            VALUES (?, ?, EMPTY_CLOB(), ?, ?, ?, ?, ?)
        """;

        String updateClob = "UPDATE FARMACIA.COMPRAS SET DOCUMENTO=? WHERE ID_COMPRA=?";

        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            long idGenerado = -1;

            // 1️⃣ Insertar registro sin CLOB
            try (PreparedStatement ps = cn.prepareStatement(sql, new String[]{"ID_COMPRA"})) {
                ps.setLong(1, compra.getIdProveedor());
                ps.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
                ps.setDouble(3, compra.getTotalBruto());
                ps.setString(4, compra.getFormaPago());
                ps.setString(5, compra.getEstado());
                ps.setLong(6, compra.getIdUsuario());
                ps.setDate(7, new java.sql.Date(compra.getFechaCompra().getTime()));
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getLong(1);
                        compra.setIdCompra(idGenerado);
                    }
                }
            }

            // 2️⃣ Actualizar el CLOB
            if (compra.getDocumento() != null && !compra.getDocumento().isBlank()) {
                try (PreparedStatement ps2 = cn.prepareStatement(updateClob)) {
                    java.io.Reader reader = new java.io.StringReader(compra.getDocumento());
                    ps2.setCharacterStream(1, reader, compra.getDocumento().length());
                    ps2.setLong(2, idGenerado);
                    ps2.executeUpdate();
                }
            }

            // ✅ 3️⃣ Auditoría (agregada sin modificar la lógica principal)
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA");
            a.setIdRegistro(idGenerado);
            a.setOperacion("INSERT");
            a.setIdUsuario(compra.getIdUsuario());
            a.setMotivo("Nueva compra agregada del proveedor ID=" + compra.getIdProveedor());
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("✅ Compra agregada correctamente con ID: " + compra.getIdCompra());
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al agregar compra: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }

    // ======================================================
    // ✏️ ACTUALIZAR
    // ======================================================
    public boolean actualizar(ModeloCompra compra) {
        String sql = """
            UPDATE FARMACIA.COMPRAS
            SET ID_PROVEEDOR=?, FECHA=?, TOTAL_BRUTO=?, FORMA_PAGO=?, ESTADO=?, ID_USUARIO=?, FECHA_COMPRA=?
            WHERE ID_COMPRA=?
        """;

        String updateClob = "UPDATE FARMACIA.COMPRAS SET DOCUMENTO=? WHERE ID_COMPRA=?";

        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            // 1️⃣ Actualizar datos principales
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setLong(1, compra.getIdProveedor());
                ps.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
                ps.setDouble(3, compra.getTotalBruto());
                ps.setString(4, compra.getFormaPago());
                ps.setString(5, compra.getEstado());
                ps.setLong(6, compra.getIdUsuario());
                ps.setDate(7, new java.sql.Date(compra.getFechaCompra().getTime()));
                ps.setLong(8, compra.getIdCompra());
                ps.executeUpdate();
            }

            // 2️⃣ Actualizar CLOB
            if (compra.getDocumento() != null && !compra.getDocumento().isBlank()) {
                try (PreparedStatement ps2 = cn.prepareStatement(updateClob)) {
                    java.io.Reader reader = new java.io.StringReader(compra.getDocumento());
                    ps2.setCharacterStream(1, reader, compra.getDocumento().length());
                    ps2.setLong(2, compra.getIdCompra());
                    ps2.executeUpdate();
                }
            }

            // ✅ 3️⃣ Auditoría
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA");
            a.setIdRegistro(compra.getIdCompra());
            a.setOperacion("UPDATE");
            a.setIdUsuario(compra.getIdUsuario());
            a.setMotivo("Actualización de compra ID=" + compra.getIdCompra());
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("✅ Compra actualizada correctamente: " + compra.getIdCompra());
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al actualizar compra: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }

    // ======================================================
    // ❌ ELIMINAR
    // ======================================================
    public boolean eliminar(long idCompra, long idUsuario) {
        String sql = "DELETE FROM FARMACIA.COMPRAS WHERE ID_COMPRA=?";
        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            try (PreparedStatement ps = db.preparar(sql)) {
                ps.setLong(1, idCompra);
                ps.executeUpdate();
            }

            // ✅ Auditoría
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA");
            a.setIdRegistro(idCompra);
            a.setOperacion("DELETE");
            a.setIdUsuario(idUsuario);
            a.setMotivo("Eliminación de compra ID=" + idCompra);
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("🗑️ Compra eliminada ID=" + idCompra);
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al eliminar compra: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }

    // ======================================================
    // 🔍 BUSCAR POR ID_COMPRA
    // ======================================================
    public ModeloCompra buscarPorIdCompra(long idCompra) {
        String sql = "SELECT * FROM FARMACIA.COMPRAS WHERE ID_COMPRA=?";
        ModeloCompra compra = null;
        try {
            db.conectar();
            try (PreparedStatement ps = db.preparar(sql)) {
                ps.setLong(1, idCompra);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        compra = mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar compra: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return compra;
    }

    // ======================================================
    // 🔍 BUSCAR POR ID_PROVEEDOR
    // ======================================================
    public ModeloCompra buscarPorIdProveedor(long idProveedor) {
        String sql = "SELECT * FROM FARMACIA.COMPRAS WHERE ID_PROVEEDOR = ? FETCH FIRST 1 ROWS ONLY";
        ModeloCompra compra = null;
        try {
            db.conectar();
            try (PreparedStatement ps = db.preparar(sql)) {
                ps.setLong(1, idProveedor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        compra = mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar por ID_PROVEEDOR: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return compra;
    }

    // ======================================================
    // 🔍 LISTAR TODAS
    // ======================================================
    public List<ModeloCompra> listarTodas() {
        String sql = "SELECT * FROM FARMACIA.COMPRAS ORDER BY ID_COMPRA DESC";
        List<ModeloCompra> lista = new ArrayList<>();
        try {
            db.conectar();
            try (PreparedStatement ps = db.preparar(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar compras: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }

    // ======================================================
    // 🧩 MÉTODO AUXILIAR (NO SE MODIFICA)
    // ======================================================
    private ModeloCompra mapResultSet(ResultSet rs) throws SQLException {
        ModeloCompra compra = new ModeloCompra();
        compra.setIdCompra(rs.getLong("ID_COMPRA"));
        compra.setIdProveedor(rs.getLong("ID_PROVEEDOR"));
        compra.setFecha(rs.getDate("FECHA"));
        compra.setDocumento(rs.getString("DOCUMENTO"));
        compra.setTotalBruto(rs.getDouble("TOTAL_BRUTO"));
        compra.setFormaPago(rs.getString("FORMA_PAGO"));
        compra.setEstado(rs.getString("ESTADO"));
        compra.setIdUsuario(rs.getLong("ID_USUARIO"));
        compra.setFechaCompra(rs.getDate("FECHA_COMPRA"));
        return compra;
    }
}
