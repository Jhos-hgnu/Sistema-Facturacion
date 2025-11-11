/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

/**
 *
 * @author Madelin
 */


import Conector.DBConnection;               
import Interfaces.ProveedorDAO;
import Modelo.ModeloProveedor;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class ProveedorImp implements ProveedorDAO {

    // Mapea una fila a objeto
    private ModeloProveedor map(ResultSet rs) throws Exception {
        ModeloProveedor p = new ModeloProveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setNit(rs.getString("nit"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setDireccion(rs.getString("direccion"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setTipoPago(rs.getString("tipo_pago"));
        p.setPlazoCredito(rs.getInt("plazo_credito"));
        p.setRepresentante(rs.getString("representante"));
        p.setEstado(rs.getString("estado"));
        Date fr = rs.getDate("fecha_registro");
        p.setFechaRegistro(fr==null? null : fr.toLocalDate());
        return p;
    }

    @Override
    public int crear(ModeloProveedor p) throws Exception {
        String sql = """
            INSERT INTO proveedores
              (nit, razon_social, direccion, telefono, email, tipo_pago, plazo_credito,
               representante, estado, fecha_registro)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNit());
            ps.setString(2, p.getRazonSocial());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getTipoPago());
            ps.setInt(7, p.getPlazoCredito()==null?0:p.getPlazoCredito());
            ps.setString(8, p.getRepresentante());
            ps.setString(9, p.getEstado());
            LocalDate fr = p.getFechaRegistro();
            ps.setDate(10, (fr==null) ? new java.sql.Date(System.currentTimeMillis())
                                      : java.sql.Date.valueOf(fr));
            return ps.executeUpdate();
        } finally {
            db.desconectar();
        }
    }

    @Override
    public Optional<ModeloProveedor> buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor = ?";
        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } finally {
            db.desconectar();
        }
    }

    @Override
    public Optional<ModeloProveedor> buscarPorNit(String nit) throws Exception {
        String sql = "SELECT * FROM proveedores WHERE nit = ?";
        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } finally {
            db.desconectar();
        }
    }

    @Override
    public int actualizar(ModeloProveedor p) throws Exception {
        String sql = """
            UPDATE proveedores SET
              nit=?, razon_social=?, direccion=?, telefono=?, email=?, tipo_pago=?,
              plazo_credito=?, representante=?, estado=?, fecha_registro=?
            WHERE id_proveedor=?
            """;
        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNit());
            ps.setString(2, p.getRazonSocial());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getTipoPago());
            ps.setInt(7, p.getPlazoCredito()==null?0:p.getPlazoCredito());
            ps.setString(8, p.getRepresentante());
            ps.setString(9, p.getEstado());
            LocalDate fr = p.getFechaRegistro();
            ps.setDate(10, (fr==null) ? new java.sql.Date(System.currentTimeMillis())
                                      : java.sql.Date.valueOf(fr));
            ps.setInt(11, p.getIdProveedor());
            return ps.executeUpdate();
        } finally {
            db.desconectar();
        }
    }

    @Override
    public int eliminarFisico(int id) throws Exception {
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } finally {
            db.desconectar();
        }
    }

    @Override
    public int desactivar(int id) throws Exception {
        String sql = "UPDATE proveedores SET estado='INACTIVO' WHERE id_proveedor = ?";
        DBConnection db = new DBConnection();
        db.conectar();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } finally {
            db.desconectar();
        }
    }
}
