/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Interfaces.IAuditoria;
import Modelo.ModeloCompraDetalle;
import Modelo.ModeloAuditoria;

import java.math.BigDecimal;
import java.sql.*;

/**
 * DAO para la tabla FARMACIA.COMPRA_DETALLE
 * Permite agregar, actualizar y eliminar registros con auditoría.
 * Autor: Joshua Cirilo Alegría
 */
public class ModeloCompraDetalleImpl {

    private final DBConnection db;
    private final IAuditoria auditoriaDAO = new AuditoriaImpl();

    public ModeloCompraDetalleImpl() {
        this.db = new DBConnection();
    }

    // ======================================================
    // ➕ AGREGAR
    // ======================================================
    public boolean agregar(ModeloCompraDetalle detalle) {
        String sql = """
            INSERT INTO FARMACIA.COMPRA_DETALLE
            (ID_COMPRA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, TOTAL)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            long idGenerado = -1;

            try (PreparedStatement ps = cn.prepareStatement(sql, new String[]{"ID_DETALLE"})) {
                ps.setLong(1, detalle.getIdCompra());
                ps.setLong(2, detalle.getIdProducto());
                ps.setBigDecimal(3, detalle.getCantidad());
                ps.setBigDecimal(4, detalle.getPrecio());
                ps.setBigDecimal(5, detalle.getDescuento());
                ps.setBigDecimal(6, detalle.getTotal());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getLong(1);
                        detalle.setIdDetalle(idGenerado);
                    }
                }
            }

            // ✅ Auditoría
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA_DETALLE");
            a.setIdRegistro(idGenerado);
            a.setOperacion("INSERT");
            a.setIdUsuario(1); // ⚠️ Cambia esto por el usuario actual (si lo manejas en sesión)
            a.setMotivo("Inserción de detalle de compra ID_COMPRA=" + detalle.getIdCompra());
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("✅ Detalle agregado correctamente con ID: " + detalle.getIdDetalle());
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al agregar detalle: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }

    // ======================================================
    // ✏️ ACTUALIZAR
    // ======================================================
    public boolean actualizar(ModeloCompraDetalle detalle) {
        String sql = """
            UPDATE FARMACIA.COMPRA_DETALLE
            SET ID_COMPRA=?, ID_PRODUCTO=?, CANTIDAD=?, PRECIO=?, DESCUENTO=?, TOTAL=?
            WHERE ID_DETALLE=?
        """;

        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setLong(1, detalle.getIdCompra());
                ps.setLong(2, detalle.getIdProducto());
                ps.setBigDecimal(3, detalle.getCantidad());
                ps.setBigDecimal(4, detalle.getPrecio());
                ps.setBigDecimal(5, detalle.getDescuento());
                ps.setBigDecimal(6, detalle.getTotal());
                ps.setLong(7, detalle.getIdDetalle());
                ps.executeUpdate();
            }

            // ✅ Auditoría
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA_DETALLE");
            a.setIdRegistro(detalle.getIdDetalle());
            a.setOperacion("UPDATE");
            a.setIdUsuario(1); // ⚠️ Reemplazar por el ID del usuario actual
            a.setMotivo("Actualización del detalle ID=" + detalle.getIdDetalle());
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("✅ Detalle actualizado correctamente: " + detalle.getIdDetalle());
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al actualizar detalle: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }

    // ======================================================
    // ❌ ELIMINAR
    // ======================================================
    public boolean eliminar(long idDetalle, long idUsuario) {
        String sql = "DELETE FROM FARMACIA.COMPRA_DETALLE WHERE ID_DETALLE=?";
        try {
            db.conectar();
            Connection cn = db.getConnection();
            db.comenzarTransaccion();

            try (PreparedStatement ps = db.preparar(sql)) {
                ps.setLong(1, idDetalle);
                ps.executeUpdate();
            }

            // ✅ Auditoría
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("COMPRA_DETALLE");
            a.setIdRegistro(idDetalle);
            a.setOperacion("DELETE");
            a.setIdUsuario(idUsuario);
            a.setMotivo("Eliminación de detalle de compra ID_DETALLE=" + idDetalle);
            auditoriaDAO.agregar(cn, a);

            db.confirmarTransaccion();
            System.out.println("🗑️ Detalle eliminado ID=" + idDetalle);
            return true;

        } catch (SQLException e) {
            db.revertirTransaccion();
            System.err.println("❌ Error al eliminar detalle: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }
    
    
            // ======================================================
        // 🔍 BUSCAR POR ID_DETALLE
        // ======================================================
public ModeloCompraDetalle buscarPorId(long idDetalle) {
    String sql = "SELECT * FROM FARMACIA.COMPRA_DETALLE WHERE ID_DETALLE = ?";
    ModeloCompraDetalle detalle = null;

    try {
        db.conectar();
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setLong(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapResultSet(rs);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar detalle: " + e.getMessage());
    } finally {
        db.desconectar();
    }
    return detalle;
}

        
// ======================================================
// 🧩 MÉTODO AUXILIAR: Mapea una fila del ResultSet al modelo
// ======================================================
private ModeloCompraDetalle mapResultSet(ResultSet rs) throws SQLException {
    ModeloCompraDetalle detalle = new ModeloCompraDetalle();

    // 👇 Asegúrate que estos nombres coincidan con las columnas reales en Oracle
    detalle.setIdDetalle(rs.getLong("ID_DETALLE"));
    detalle.setIdCompra(rs.getLong("ID_COMPRA"));
    detalle.setIdProducto(rs.getLong("ID_PRODUCTO"));
    detalle.setCantidad(rs.getBigDecimal("CANTIDAD"));
    detalle.setPrecio(rs.getBigDecimal("PRECIO"));
    detalle.setDescuento(rs.getBigDecimal("DESCUENTO"));
    detalle.setTotal(rs.getBigDecimal("TOTAL"));

    return detalle;
}


        


}
