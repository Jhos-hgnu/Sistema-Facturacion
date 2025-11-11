/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

/**
 *
 * @author Dulce
 */
import Conector.DBConnection;
import Modelo.ModeloCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.util.Types;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientesImp {

    private final DBConnection db;

    public ClientesImp() {
        this.db = new DBConnection();
        this.db.conectar(); // Abre la conexión a Oracle
    }

    // ➕ AGREGAR CLIENTE
    public boolean agregar(ModeloCliente c) {
        String sql = """
            INSERT INTO FARMACIA.CLIENTES
            (NIT, PRIMER_NOMBRE, SEGUNDO_NOMBRE, PRIMER_APELLIDO, SEGUNDO_APELLIDO,
             APELLIDO_CASADA, DIRECCION, TELEFONO, EMAIL, TIPO_CLIENTE,
             LIMITE_CREDITO, ESTADO, FECHA_REGISTRO)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setString(1, c.getNit());
            ps.setString(2, c.getPrimerNombre());
            ps.setString(3, c.getSegundoNombre());
            ps.setString(4, c.getPrimerApellido());
            ps.setString(5, c.getSegundoApellido());
            ps.setString(6, c.getApellidoCasada());
            ps.setString(7, c.getDireccion());
            ps.setString(8, c.getTelefono());
            ps.setString(9, c.getEmail());
            ps.setString(10, c.getTipoCliente());

            // Manejo de null en limiteCredito
            if (c.getLimiteCredito() != null) {
                ps.setDouble(11, c.getLimiteCredito());
            }

            ps.setString(12, c.getEstado());

            // Manejo de null en fechaRegistro
            if (c.getFechaRegistro() != null) {
                ps.setDate(13, Date.valueOf(c.getFechaRegistro()));
            }
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    // ✏️ ACTUALIZAR CLIENTE
    public boolean actualizar(ModeloCliente c) {
        String sql = """
            UPDATE FARMACIA.CLIENTES SET
            PRIMER_NOMBRE=?, SEGUNDO_NOMBRE=?, PRIMER_APELLIDO=?, SEGUNDO_APELLIDO=?,
            APELLIDO_CASADA=?, DIRECCION=?, TELEFONO=?, EMAIL=?, TIPO_CLIENTE=?,
            LIMITE_CREDITO=?, ESTADO=?, FECHA_REGISTRO=?
            WHERE NIT=?
        """;

        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setString(1, c.getPrimerNombre());
            ps.setString(2, c.getSegundoNombre());
            ps.setString(3, c.getPrimerApellido());
            ps.setString(4, c.getSegundoApellido());
            ps.setString(5, c.getApellidoCasada());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getTelefono());
            ps.setString(8, c.getEmail());
            ps.setString(9, c.getTipoCliente());

            // Manejo de null en limiteCredito
            if (c.getLimiteCredito() != null) {
                ps.setDouble(10, c.getLimiteCredito());
            }
            ps.setString(11, c.getEstado());
            // Manejo de null en fechaRegistro
            if (c.getFechaRegistro() != null) {
                ps.setDate(12, Date.valueOf(c.getFechaRegistro()));
            } 
            ps.setString(13, c.getNit());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    // ❌ ELIMINAR CLIENTE
    public boolean eliminar(String nit) {
        String sql = "DELETE FROM FARMACIA.CLIENTES WHERE NIT=?";
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setString(1, nit);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    // 📋 LISTAR CLIENTES
    public List<ModeloCliente> listar() {
        List<ModeloCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM FARMACIA.CLIENTES ORDER BY NIT";

        try (PreparedStatement ps = db.preparar(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Date fechaSQL = rs.getDate("FECHA_REGISTRO");
                LocalDate fecha = (fechaSQL != null) ? fechaSQL.toLocalDate() : null;

                ModeloCliente c = new ModeloCliente(
                        rs.getString("NIT"),
                        rs.getString("PRIMER_NOMBRE"),
                        rs.getString("SEGUNDO_NOMBRE"),
                        rs.getString("PRIMER_APELLIDO"),
                        rs.getString("SEGUNDO_APELLIDO"),
                        rs.getString("APELLIDO_CASADA"),
                        rs.getString("DIRECCION"),
                        rs.getString("TELEFONO"),
                        rs.getString("EMAIL"),
                        rs.getString("TIPO_CLIENTE"),
                        rs.getDouble("LIMITE_CREDITO"),
                        rs.getString("ESTADO"),
                        fecha
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar clientes: " + e.getMessage());
        }

        return lista;
    }

    // 🔍 BUSCAR CLIENTE POR NIT
    public ModeloCliente buscarPorNit(String nit) {
        String sql = "SELECT * FROM FARMACIA.CLIENTES WHERE NIT=?";
        try (PreparedStatement ps = db.preparar(sql)) {
            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date fechaSQL = rs.getDate("FECHA_REGISTRO");
                    LocalDate fecha = (fechaSQL != null) ? fechaSQL.toLocalDate() : null;

                    return new ModeloCliente(
                            rs.getString("NIT"),
                            rs.getString("PRIMER_NOMBRE"),
                            rs.getString("SEGUNDO_NOMBRE"),
                            rs.getString("PRIMER_APELLIDO"),
                            rs.getString("SEGUNDO_APELLIDO"),
                            rs.getString("APELLIDO_CASADA"),
                            rs.getString("DIRECCION"),
                            rs.getString("TELEFONO"),
                            rs.getString("EMAIL"),
                            rs.getString("TIPO_CLIENTE"),
                            rs.getDouble("LIMITE_CREDITO"),
                            rs.getString("ESTADO"),
                            fecha
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }
}
