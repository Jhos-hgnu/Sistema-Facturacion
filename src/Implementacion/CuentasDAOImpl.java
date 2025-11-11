// Implementacion/CuentasDAOImpl.java
package Implementacion;

import Conector.DBConnection;
import Interfaces.CuentasDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CuentasDAOImpl implements CuentasDAO {

    private final DBConnection db;

    public CuentasDAOImpl() {
        this.db = new DBConnection();
        this.db.conectar(); // abre conexión interna
    }

    // Implementacion/CuentasDAOImpl.java (solo modifico el uso de la conexión)
@Override
public List<CxcRow> listarCXC(int limit) {
    String sql = """
        SELECT id_cuenta_cobro, id_venta, fecha_emision, fecha_vence, monto, saldo, estado
        FROM cuenta_por_cobrar
        ORDER BY id_cuenta_cobro
    """;
    if (limit > 0) sql += " FETCH FIRST " + limit + " ROWS ONLY";

    List<CxcRow> out = new ArrayList<>();
    java.sql.PreparedStatement ps = null;
    java.sql.ResultSet rs = null;
    try {
java.sql.Connection cn = db.getConnection();
        ps = cn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Long id        = rs.getLong("id_cuenta_cobro");
            Long idVenta   = rs.getLong("id_venta");
            java.util.Date emision = rs.getDate("fecha_emision");
            java.util.Date vence   = rs.getDate("fecha_vence");
            java.math.BigDecimal monto = rs.getBigDecimal("monto");
            java.math.BigDecimal saldo = rs.getBigDecimal("saldo");
            String estado  = rs.getString("estado");
            out.add(new CxcRow(id, idVenta, emision, vence, monto, saldo, estado));
        }
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        // No cerrar la conexión aquí
    }
    return out;
}

@Override
public List<CxpRow> listarCXP(int limit) {
    String sql = """
        SELECT id_cuenta_pago, id_compra, fecha_emision, fecha_vencimiento,
               monto_total, saldo_pendiente, estado_pago
        FROM cuenta_por_pagar
        ORDER BY id_cuenta_pago
    """;
    if (limit > 0) sql += " FETCH FIRST " + limit + " ROWS ONLY";

    List<CxpRow> out = new ArrayList<>();
    java.sql.PreparedStatement ps = null;
    java.sql.ResultSet rs = null;
    try {
        java.sql.Connection cn = db.getConexion(); // NO cerrar cn aquí
        ps = cn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Long id          = rs.getLong("id_cuenta_pago");
            Long idCompra    = rs.getLong("id_compra");
            java.util.Date emision      = rs.getDate("fecha_emision");
            java.util.Date vencimiento  = rs.getDate("fecha_vencimiento");
            java.math.BigDecimal total  = rs.getBigDecimal("monto_total");
            java.math.BigDecimal saldo  = rs.getBigDecimal("saldo_pendiente");
            String estado     = rs.getString("estado_pago");
            out.add(new CxpRow(id, idCompra, emision, vencimiento, total, saldo, estado));
        }
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        // No cerrar la conexión aquí
    }
    return out;
}
}
