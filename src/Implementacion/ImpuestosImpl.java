/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;
import Interfaces.IImpuestos;
import Interfaces.IAuditoria;
import Modelo.ModeloImpuesto;
import Modelo.ModeloAuditoria;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

/**
 *
 * @author anyi4
 */



public class ImpuestosImpl implements IImpuestos {

    private final IAuditoria auditoriaDAO = new AuditoriaImpl();

    @Override
    public long crear(Connection cn, String nombre, BigDecimal tasa, long idUsuario) throws Exception {
        Long newId = null;

        // Insert (ID autoincrementado por Oracle)
        String ins = "INSERT INTO impuestos (nombre, tasa) VALUES (?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(ins)) {
            ps.setString(1, nombre.trim());
            ps.setBigDecimal(2, tasa);
            ps.executeUpdate();
        }

        // Recuperar ID por clave única (nombre)
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_impuestos FROM impuestos WHERE UPPER(nombre)=UPPER(?)")) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) newId = rs.getLong(1);
            }
        }
        if (newId == null) throw new SQLException("No fue posible obtener ID de impuesto.");

        // Auditoría
        ModeloAuditoria a = new ModeloAuditoria();
        a.setTipoCambio("IMPUESTO");
        a.setIdRegistro(newId);
        a.setOperacion("INSERT");
        a.setIdUsuario(idUsuario);
        a.setMotivo("Alta de impuesto: " + nombre + " tasa=" + tasa);
        auditoriaDAO.agregar(cn, a);

        return newId;
    }

    @Override
    public void actualizar(Connection cn, long id, String nombre, BigDecimal tasa, long idUsuario) throws Exception {
        String oldNombre = null; BigDecimal oldTasa = null;

        try (PreparedStatement q = cn.prepareStatement(
                "SELECT nombre, tasa FROM impuestos WHERE id_impuestos=?")) {
            q.setLong(1, id);
            try (ResultSet rs = q.executeQuery()) {
                if (!rs.next()) throw new SQLException("Impuesto no encontrado.");
                oldNombre = rs.getString(1);
                oldTasa = rs.getBigDecimal(2);
            }
        }

        try (PreparedStatement up = cn.prepareStatement(
                "UPDATE impuestos SET nombre=?, tasa=? WHERE id_impuestos=?")) {
            up.setString(1, nombre.trim());
            up.setBigDecimal(2, tasa);
            up.setLong(3, id);
            up.executeUpdate();
        }

        ModeloAuditoria a = new ModeloAuditoria();
        a.setTipoCambio("IMPUESTO");
        a.setIdRegistro(id);
        a.setOperacion("UPDATE");
        a.setIdUsuario(idUsuario);
        a.setMotivo("Nombre: " + oldNombre + "→" + nombre + "; Tasa: " + oldTasa + "→" + tasa);
        auditoriaDAO.agregar(cn, a);
    }

    @Override
    public boolean borrar(Connection cn, long id, long idUsuario) throws Exception {
        if (estaEnUsoPorProductos(cn, id)) return false;

        int rows;
        try (PreparedStatement del = cn.prepareStatement(
                "DELETE FROM impuestos WHERE id_impuestos=?")) {
            del.setLong(1, id);
            rows = del.executeUpdate();
        }
        if (rows > 0) {
            ModeloAuditoria a = new ModeloAuditoria();
            a.setTipoCambio("IMPUESTO");
            a.setIdRegistro(id);
            a.setOperacion("DELETE");
            a.setIdUsuario(idUsuario);
            a.setMotivo("Eliminación de impuesto");
            auditoriaDAO.agregar(cn, a);
            return true;
        }
        return false;
    }

    @Override
    public Optional<ModeloImpuesto> buscarPorId(Connection cn, long id) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_impuestos, nombre, tasa FROM impuestos WHERE id_impuestos=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ModeloImpuesto m = new ModeloImpuesto(
                            rs.getLong(1), rs.getString(2), rs.getBigDecimal(3));
                    return Optional.of(m);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<ModeloImpuesto> buscarPorNombreExacto(Connection cn, String nombre) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_impuestos, nombre, tasa FROM impuestos WHERE UPPER(nombre)=UPPER(?)")) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ModeloImpuesto m = new ModeloImpuesto(
                            rs.getLong(1), rs.getString(2), rs.getBigDecimal(3));
                    return Optional.of(m);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean estaEnUsoPorProductos(Connection cn, long id) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT COUNT(*) FROM productos WHERE id_impuesto=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); return rs.getInt(1) > 0;
            }
        }
    }
    @Override
public boolean existeNombre(Connection cn, String nombre) throws Exception {
    String sql = "SELECT 1 FROM impuestos WHERE UPPER(nombre)=UPPER(?) FETCH FIRST 1 ROWS ONLY";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setString(1, nombre.trim());
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }
}
}
