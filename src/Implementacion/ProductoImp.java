package Implementacion;

import Conector.DBConnection;
import Interfaces.IProducto;
import Modelo.ModeloProducto;
import java.math.BigDecimal;

import java.sql.*;




public class ProductoImp implements IProducto {

    private final DBConnection conexion = new DBConnection();

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================
    public boolean existeProducto(BigDecimal idProducto) {
        String sql = "SELECT 1 FROM productos WHERE id_producto = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, idProducto);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("existeProducto: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

    private ModeloProducto map(ResultSet rs) throws SQLException {
        ModeloProducto p = new ModeloProducto();
        p.setIdProducto(rs.getBigDecimal("ID_PRODUCTO"));
        p.setNombreProducto(rs.getString("NOMBRE_PRODUCTO"));
        p.setMarca(rs.getString("MARCA"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setPrecioCosto(rs.getBigDecimal("PRECIO_COSTO"));
        p.setPrecioVenta(rs.getBigDecimal("PRECIO_VENTA"));
        p.setStock(rs.getInt("STOCK"));
        p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
        p.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
        return p;
    }

    // ==========================================================
    // IMPLEMENTACIÓN DE IProducto
    // ==========================================================
    @Override
    public boolean registrarProducto(ModeloProducto p) {
        String sql = "INSERT INTO productos " +
                "(id_producto, nombre_producto, marca, descripcion, precio_costo, precio_venta, stock, id_categoria, id_impuesto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, p.getIdProducto());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getDescripcion());
            ps.setBigDecimal(5, p.getPrecioCosto());
            ps.setBigDecimal(6, p.getPrecioVenta());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getIdCategoria());
            ps.setInt(9, p.getIdImpuesto());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("registrarProducto: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public ModeloProducto buscarPorId(BigDecimal codigo) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, codigo);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            System.err.println("buscarPorId: " + e.getMessage());
            return null;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public ModeloProducto buscarPorNombre(String nombre) {
        // Oracle usa FETCH FIRST para limitar resultados
        String sql = "SELECT * FROM productos WHERE UPPER(nombre_producto) LIKE UPPER(?) FETCH FIRST 1 ROWS ONLY";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, "%" + nombre.trim() + "%");
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            System.err.println("buscarPorNombre: " + e.getMessage());
            return null;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public boolean actualizarProducto(ModeloProducto p) {
        String sql = "UPDATE productos SET " +
                "nombre_producto=?, marca=?, descripcion=?, precio_costo=?, precio_venta=?, " +
                "stock=?, id_categoria=?, id_impuesto=? WHERE id_producto=?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getDescripcion());
            ps.setBigDecimal(4, p.getPrecioCosto());
            ps.setBigDecimal(5, p.getPrecioVenta());
            ps.setInt(6, p.getStock());
            ps.setInt(7, p.getIdCategoria());
            ps.setInt(8, p.getIdImpuesto());
            ps.setBigDecimal(9, p.getIdProducto());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("actualizarProducto: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public boolean eliminarProducto(BigDecimal idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, idProducto);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("eliminarProducto: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }
}
