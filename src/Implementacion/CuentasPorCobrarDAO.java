/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

/**
 *
 * @author luisd
 */

import Modelo.ModeloCuentasPorCobrar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentasPorCobrarDAO {

    private Connection conexion;

    // ✅ Constructor
    public CuentasPorCobrarDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // ✅ Insertar nueva cuenta por cobrar
    public void insertar(ModeloCuentasPorCobrar c) throws SQLException {
        String sql = """
            INSERT INTO cuenta_por_cobrar (
                id_venta, fecha_emision, fecha_vence, monto, saldo, estado
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, c.getIdVenta());
            ps.setDate(2, new java.sql.Date(c.getFechaEmision().getTime()));
            ps.setDate(3, new java.sql.Date(c.getFechaVence().getTime()));
            ps.setDouble(4, c.getMonto());
            ps.setDouble(5, c.getSaldo());
            ps.setString(6, c.getEstado());
            ps.executeUpdate();
        }
    }

    // ✅ Buscar por ID
    public ModeloCuentasPorCobrar buscarPorId(long idCuentaCobro) throws SQLException {
        String sql = "SELECT * FROM cuenta_por_cobrar WHERE id_cuenta_cobro = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, idCuentaCobro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ModeloCuentasPorCobrar c = new ModeloCuentasPorCobrar();
                c.setIdCuentaCobro(rs.getLong("id_cuenta_cobro"));
                c.setIdVenta(rs.getLong("id_venta"));
                c.setFechaEmision(rs.getDate("fecha_emision"));
                c.setFechaVence(rs.getDate("fecha_vence"));
                c.setMonto(rs.getDouble("monto"));
                c.setSaldo(rs.getDouble("saldo"));
                c.setEstado(rs.getString("estado"));
                return c;
            }
        }
        return null;
    }

    // ✅ Actualizar registro existente
    public void actualizar(ModeloCuentasPorCobrar c) throws SQLException {
        String sql = """
            UPDATE cuenta_por_cobrar
            SET id_venta = ?, fecha_emision = ?, fecha_vence = ?, monto = ?, saldo = ?, estado = ?
            WHERE id_cuenta_cobro = ?
            """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, c.getIdVenta());
            ps.setDate(2, new java.sql.Date(c.getFechaEmision().getTime()));
            ps.setDate(3, new java.sql.Date(c.getFechaVence().getTime()));
            ps.setDouble(4, c.getMonto());
            ps.setDouble(5, c.getSaldo());
            ps.setString(6, c.getEstado());
            ps.setLong(7, c.getIdCuentaCobro());
            ps.executeUpdate();
        }
    }

    // ✅ Eliminar por ID
    public void eliminar(long idCuentaCobro) throws SQLException {
        String sql = "DELETE FROM cuenta_por_cobrar WHERE id_cuenta_cobro = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, idCuentaCobro);
            ps.executeUpdate();
        }
    }

    // ✅ Listar todas las cuentas por cobrar
    public List<ModeloCuentasPorCobrar> listarTodas() throws SQLException {
        List<ModeloCuentasPorCobrar> lista = new ArrayList<>();
        String sql = "SELECT * FROM cuenta_por_cobrar ORDER BY id_cuenta_cobro";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ModeloCuentasPorCobrar c = new ModeloCuentasPorCobrar(
                        rs.getLong("id_cuenta_cobro"),
                        rs.getLong("id_venta"),
                        rs.getDate("fecha_emision"),
                        rs.getDate("fecha_vence"),
                        rs.getDouble("monto"),
                        rs.getDouble("saldo"),
                        rs.getString("estado")
                );
                lista.add(c);
            }
        }
        return lista;
    }
}