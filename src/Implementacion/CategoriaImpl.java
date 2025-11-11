/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;


import Conector.DBConnection;
import Interfaces.ICategoria;
import Modelo.ModeloCategoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author anyi4
 */


public class CategoriaImpl implements ICategoria {

    private final DBConnection conexion;

    public CategoriaImpl() {
        conexion = new DBConnection();
    }

    @Override
    public boolean registrarCategoria(ModeloCategoria categoria) {
        // ID autogenerado → se omite en el INSERT
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar categoría: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public ModeloCategoria buscarCategoria(int idCategoria) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        ModeloCategoria categoria = null;
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoria = new ModeloCategoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar categoría: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
        return categoria;
    }

    @Override
    public boolean actualizarCategoria(ModeloCategoria categoria) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getIdCategoria());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar categoría: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public boolean borrarCategoria(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setInt(1, idCategoria);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al borrar categoría: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }
@Override
public ModeloCategoria buscarPorNombre(String nombre) {
    String sql = "SELECT * FROM categorias WHERE UPPER(nombre) LIKE UPPER(?) FETCH FIRST 1 ROWS ONLY";
    ModeloCategoria categoria = null;
    try {
        conexion.conectar();
        PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
        ps.setString(1, "%" + nombre.trim() + "%");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            categoria = new ModeloCategoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
            );
        }
    } catch (SQLException e) {
        System.err.println(" Error al buscar por nombre: " + e.getMessage());
    } finally {
        conexion.desconectar();
    }
    return categoria;
}

    @Override
    public List<ModeloCategoria> listarCategorias() {
        String sql = "SELECT * FROM categorias ORDER BY id_categoria";
        List<ModeloCategoria> lista = new ArrayList<>();
        try {
            conexion.conectar();
            Statement st = conexion.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                lista.add(new ModeloCategoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar categorías: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
        return lista;
    }
    
}
