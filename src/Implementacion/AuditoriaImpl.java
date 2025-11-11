/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;



import Interfaces.IAuditoria;
import Modelo.ModeloAuditoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import Interfaces.IAuditoria;
import Modelo.ModeloAuditoria;
import java.sql.*;
import java.util.Optional;

/**
 * @author jciri
 */

public class AuditoriaImpl implements IAuditoria {

    @Override
    public boolean agregar(Connection cn, ModeloAuditoria a) {
        String sql = "INSERT INTO AUDITORIA " +
                     "(TIPO_CAMBIO, ID_REGISTRO, OPERACION, ID_USUARIO, MOTIVO, FECHA) " +
                     "VALUES (?, ?, ?, ?, ?, SYSDATE)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getTipoCambio());
            ps.setLong(2, a.getIdRegistro());
            ps.setString(3, a.getOperacion());
            ps.setLong(4, a.getIdUsuario());
            ps.setString(5, a.getMotivo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar auditoría: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<ModeloAuditoria> buscarPorIdAuditoria(Connection cn, long idAuditoria) {
        String q = "SELECT ID_AUDITORIA, TIPO_CAMBIO, ID_REGISTRO, OPERACION, ID_USUARIO, MOTIVO, FECHA " +
                   "FROM AUDITORIA WHERE ID_AUDITORIA = ?";
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setLong(1, idAuditoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("❌ Buscar por ID_AUDITORIA: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ModeloAuditoria> buscarUltimaPorIdRegistro(Connection cn, long idRegistro) {
        String q = "SELECT ID_AUDITORIA, TIPO_CAMBIO, ID_REGISTRO, OPERACION, ID_USUARIO, MOTIVO, FECHA " +
                   "FROM AUDITORIA WHERE ID_REGISTRO=? ORDER BY FECHA DESC FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setLong(1, idRegistro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("❌ Buscar por ID_REGISTRO: " + e.getMessage());
            return Optional.empty();
        }
    }

   

    private ModeloAuditoria map(ResultSet rs) throws SQLException {
        ModeloAuditoria a = new ModeloAuditoria();
        a.setIdAuditoria(rs.getLong("ID_AUDITORIA"));
        a.setTipoCambio(rs.getString("TIPO_CAMBIO"));
        a.setIdRegistro(rs.getLong("ID_REGISTRO"));
        a.setOperacion(rs.getString("OPERACION"));
        a.setIdUsuario(rs.getLong("ID_USUARIO"));
        a.setMotivo(rs.getString("MOTIVO"));
        a.setFecha(rs.getTimestamp("FECHA")); 
        return a;
    }
}
