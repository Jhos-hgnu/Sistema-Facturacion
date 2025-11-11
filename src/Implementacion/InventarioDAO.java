/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Modelo.ModeloInventario;
import com.sun.jdi.connect.spi.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisd
 */
public class InventarioDAO {

    private final DBConnection conexion = new DBConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // === INSERTAR ===
    public boolean insertarInventario(ModeloInventario inventario) {
        boolean exito = false;
        String sql = """
            INSERT INTO inventario (
                id_producto, cantidad, stock_anterior, stock_actual,
                movimiento, motivo, id_usuario, fecha
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);

            ps.setLong(1, inventario.getIdProducto());
            ps.setInt(2, inventario.getCantidad());
            ps.setInt(3, inventario.getStockAnterior());
            ps.setInt(4, inventario.getStockActual());
            ps.setString(5, inventario.getMovimiento());
            ps.setString(6, inventario.getMotivo());
            ps.setLong(7, inventario.getIdUsuario());
            ps.setDate(8, new java.sql.Date(inventario.getFecha().getTime()));

            exito = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al insertar inventario", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === ACTUALIZAR ===
    public boolean actualizarInventario(ModeloInventario inventario) {
        boolean exito = false;
        String sql = """
            UPDATE inventario SET
                id_producto = ?, cantidad = ?, stock_anterior = ?, stock_actual = ?,
                movimiento = ?, motivo = ?, id_usuario = ?, fecha = ?
            WHERE id_inventario = ?
        """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);

            ps.setLong(1, inventario.getIdProducto());
            ps.setInt(2, inventario.getCantidad());
            ps.setInt(3, inventario.getStockAnterior());
            ps.setInt(4, inventario.getStockActual());
            ps.setString(5, inventario.getMovimiento());
            ps.setString(6, inventario.getMotivo());
            ps.setLong(7, inventario.getIdUsuario());
            ps.setDate(8, new java.sql.Date(inventario.getFecha().getTime()));
            ps.setLong(9, inventario.getIdInventario());

            exito = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al actualizar inventario", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === ELIMINAR ===
    public boolean eliminarInventario(long idInventario) {
        boolean exito = false;
        String sql = "DELETE FROM inventario WHERE id_inventario = ?";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idInventario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al eliminar inventario", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === BUSCAR POR ID ===
    public ModeloInventario buscarPorId(long idInventario) {
        ModeloInventario inventario = null;
        String sql = "SELECT * FROM inventario WHERE id_inventario = ?";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idInventario);
            rs = ps.executeQuery();

            if (rs.next()) {
                inventario = mapearResultado(rs);
            }

        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al buscar inventario", e);
        } finally {
            cerrarRecursos();
        }

        return inventario;
    }

    // === OBTENER STOCK ACTUAL ===
    public int obtenerStockActual(long idProducto) {
        int stock = 0;
        String sql = """
            SELECT stock_actual 
            FROM inventario 
            WHERE id_producto = ? 
            ORDER BY id_inventario DESC FETCH FIRST 1 ROWS ONLY
        """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("stock_actual");
            }

        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al obtener stock actual", e);
        } finally {
            cerrarRecursos();
        }

        return stock;
    }

    // === LISTAR TODO EL INVENTARIO ===
    public List<ModeloInventario> listarInventario() {
        List<ModeloInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY id_inventario DESC";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearResultado(rs));
            }

        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.SEVERE, "Error al listar inventario", e);
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    // === MAPEO RESULTADO → MODELO ===
    private ModeloInventario mapearResultado(ResultSet rs) throws SQLException {
        ModeloInventario inv = new ModeloInventario();
        inv.setIdInventario(rs.getLong("id_inventario"));
        inv.setIdProducto(rs.getLong("id_producto"));
        inv.setCantidad(rs.getInt("cantidad"));
        inv.setStockAnterior(rs.getInt("stock_anterior"));
        inv.setStockActual(rs.getInt("stock_actual"));
        inv.setMovimiento(rs.getString("movimiento"));
        inv.setMotivo(rs.getString("motivo"));
        inv.setIdUsuario(rs.getLong("id_usuario"));
        inv.setFecha(rs.getDate("fecha"));
        return inv;
    }

    // === CERRAR CONEXIONES ===
    private void cerrarRecursos() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            conexion.desconectar();
        } catch (SQLException e) {
            Logger.getLogger(InventarioDAO.class.getName())
                    .log(Level.WARNING, "Error al cerrar recursos", e);
        }
    }
}
