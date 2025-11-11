/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Interfaces.ICatalogos;

import java.sql.*;
import java.math.BigDecimal;

/**
 *
 * @author anyi4
 */


public class CatalogosImpl implements ICatalogos {

    private final DBConnection conexion = new DBConnection();

    @Override
    public Integer getCategoriaIdByNombre(String nombre) {
        if (nombre == null) return null;
        String sql = "SELECT id_categoria FROM categorias WHERE UPPER(nombre)=UPPER(?)";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, nombre.trim());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : null;
        } catch (SQLException e) {
            System.err.println("getCategoriaIdByNombre: " + e.getMessage());
            return null;
        } finally { conexion.desconectar(); }
    }

    @Override
    public Integer getImpuestoIdByNombre(String nombre) {
        if (nombre == null) return null;
        String sql = "SELECT id_impuestos FROM impuestos WHERE UPPER(nombre)=UPPER(?)";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, nombre.trim());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : null;
        } catch (SQLException e) {
            System.err.println("getImpuestoIdByNombre: " + e.getMessage());
            return null;
        } finally { conexion.desconectar(); }
    }

    @Override
    public Integer getImpuestoIdByTasa(BigDecimal tasa) {
        if (tasa == null) return null;
        String sql = "SELECT id_impuestos FROM impuestos WHERE tasa = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, tasa);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : null;
        } catch (SQLException e) {
            System.err.println("getImpuestoIdByTasa: " + e.getMessage());
            return null;
        } finally { conexion.desconectar(); }
    }
}
