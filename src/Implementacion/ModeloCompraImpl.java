package Implementacion;

import Conector.DBConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.ModeloCompra;
import java.sql.ResultSet;

//probar
public class ModeloCompraImpl {

    private final DBConnection db;

    public ModeloCompraImpl() {
        this.db = new DBConnection(); // 👈 crea la conexión
        this.db.conectar();            // 👈 abre la conexión a Oracle
    }
// ➕ AGREGAR
public boolean agregar(ModeloCompra compra) {
    String sql = "INSERT INTO FARMACIA.COMPRAS " +
                 "(ID_PROVEEDOR, FECHA, DOCUMENTO, TOTAL_BRUTO, FORMA_PAGO, ESTADO, ID_USUARIO, FECHA_COMPRA) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
        db.conectar(); // ✅ Reabrir conexión antes de usarla
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, compra.getIdProveedor());
            ps.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
            ps.setString(3, compra.getDocumento());
            ps.setDouble(4, compra.getTotalBruto());
            ps.setString(5, compra.getFormaPago());
            ps.setString(6, compra.getEstado());
            ps.setInt(7, compra.getIdUsuario());
            ps.setDate(8, new java.sql.Date(compra.getFechaCompra().getTime()));
            ps.executeUpdate();
            return true;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al agregar compra: " + e.getMessage());
        return false;
    } finally {
        db.desconectar();
    }
}


// ✏️ ACTUALIZAR
public boolean actualizar(ModeloCompra compra) {
    String sql = "UPDATE FARMACIA.COMPRAS SET " +
                 "ID_PROVEEDOR=?, FECHA=?, DOCUMENTO=?, TOTAL_BRUTO=?, FORMA_PAGO=?, ESTADO=?, " +
                 "ID_USUARIO=?, FECHA_COMPRA=? WHERE ID_COMPRA=?";
    try {
        db.conectar(); // ✅ Conectamos antes de preparar el SQL
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, compra.getIdProveedor());
            ps.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
            ps.setString(3, compra.getDocumento());
            ps.setDouble(4, compra.getTotalBruto());
            ps.setString(5, compra.getFormaPago());
            ps.setString(6, compra.getEstado());
            ps.setInt(7, compra.getIdUsuario());
            ps.setDate(8, new java.sql.Date(compra.getFechaCompra().getTime()));
            ps.setInt(9, compra.getIdCompra());
            ps.executeUpdate();
            return true;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar compra: " + e.getMessage());
        return false;
    } finally {
        db.desconectar();
    }
}


// ❌ ELIMINAR
public boolean eliminar(int idCompra) {
    String sql = "DELETE FROM FARMACIA.COMPRAS WHERE ID_COMPRA=?";
    try {
        db.conectar(); // ✅ Conexión abierta antes de ejecutar
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, idCompra);
            ps.executeUpdate();
            return true;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al eliminar compra: " + e.getMessage());
        return false;
    } finally {
        db.desconectar();
    }
}


// 🔍 BUSCAR POR ID_COMPRA
public ModeloCompra buscarPorIdCompra(int idCompra) {
    String sql = "SELECT * FROM FARMACIA.COMPRAS WHERE ID_COMPRA = ?";
    try {
        db.conectar(); // ✅ Reabrir conexión cada vez
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ModeloCompra compra = new ModeloCompra();
                    compra.setIdCompra(rs.getInt("ID_COMPRA"));
                    compra.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                    compra.setFecha(rs.getDate("FECHA"));
                    compra.setDocumento(rs.getString("DOCUMENTO"));
                    compra.setTotalBruto(rs.getDouble("TOTAL_BRUTO"));
                    compra.setFormaPago(rs.getString("FORMA_PAGO"));
                    compra.setEstado(rs.getString("ESTADO"));
                    compra.setIdUsuario(rs.getInt("ID_USUARIO"));
                    compra.setFechaCompra(rs.getDate("FECHA_COMPRA"));
                    return compra;
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar por ID_COMPRA: " + e.getMessage());
    } finally {
        db.desconectar();
    }
    return null;
}


// 🔍 BUSCAR POR ID_PROVEEDOR
public ModeloCompra buscarPorIdProveedor(int idProveedor) {
    String sql = "SELECT * FROM FARMACIA.COMPRAS WHERE ID_PROVEEDOR = ?";
    try {
        db.conectar(); // ✅ Asegurar conexión activa
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ModeloCompra compra = new ModeloCompra();
                    compra.setIdCompra(rs.getInt("ID_COMPRA"));
                    compra.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                    compra.setFecha(rs.getDate("FECHA"));
                    compra.setDocumento(rs.getString("DOCUMENTO"));
                    compra.setTotalBruto(rs.getDouble("TOTAL_BRUTO"));
                    compra.setFormaPago(rs.getString("FORMA_PAGO"));
                    compra.setEstado(rs.getString("ESTADO"));
                    compra.setIdUsuario(rs.getInt("ID_USUARIO"));
                    compra.setFechaCompra(rs.getDate("FECHA_COMPRA"));
                    return compra;
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar por ID_PROVEEDOR: " + e.getMessage());
    } finally {
        db.desconectar();
    }
    return null;
}

    
}
