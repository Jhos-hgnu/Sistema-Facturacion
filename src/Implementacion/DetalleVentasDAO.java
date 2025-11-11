/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Modelo.ModeloDetalleVenta;
import Conector.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisd
 */
public class DetalleVentasDAO {

    private final DBConnection conexion = new DBConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // === INSERTAR ===
    public boolean insertarDetalleVenta(ModeloDetalleVenta detalle) {
        boolean exito = false;
        String sql = """
            INSERT INTO detalle_venta (
                id_venta,
                id_producto,
                cantidad,
                precio_venta,
                descuento
            ) VALUES (?, ?, ?, ?, ?)
        """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);

            ps.setLong(1, detalle.getIdVenta());
            ps.setLong(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioVenta());
            ps.setDouble(5, detalle.getDescuento());

            exito = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.SEVERE, "Error al insertar detalle de venta", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === ACTUALIZAR ===
    public boolean actualizarDetalleVenta(ModeloDetalleVenta detalle) {
        boolean exito = false;
        String sql = """
            UPDATE detalle_venta SET
                id_venta = ?,
                id_producto = ?,
                cantidad = ?,
                precio_venta = ?,
                descuento = ?
            WHERE id_detalle_venta = ?
        """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);

            ps.setLong(1, detalle.getIdVenta());
            ps.setLong(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioVenta());
            ps.setDouble(5, detalle.getDescuento());
            ps.setLong(6, detalle.getIdDetalleVenta());

            exito = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.SEVERE, "Error al actualizar detalle de venta", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === ELIMINAR ===
    public boolean eliminarDetalleVenta(long idDetalleVenta) {
        boolean exito = false;
        String sql = "DELETE FROM detalle_venta WHERE id_detalle_venta = ?";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idDetalleVenta);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.SEVERE, "Error al eliminar detalle de venta", e);
        } finally {
            cerrarRecursos();
        }

        return exito;
    }

    // === BUSCAR POR ID ===
    public ModeloDetalleVenta buscarPorId(long idDetalleVenta) {
        ModeloDetalleVenta detalle = null;
        String sql = "SELECT * FROM detalle_venta WHERE id_detalle_venta = ?";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idDetalleVenta);
            rs = ps.executeQuery();

            if (rs.next()) {
                detalle = mapearResultado(rs);
            }

        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.SEVERE, "Error al buscar detalle de venta", e);
        } finally {
            cerrarRecursos();
        }

        return detalle;
    }

    // === LISTAR TODOS ===
    public List<ModeloDetalleVenta> listarDetalles() {
        List<ModeloDetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta ORDER BY id_detalle_venta DESC";

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearResultado(rs));
            }

        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.SEVERE, "Error al listar detalles de venta", e);
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    // === 🔍 NUEVO MÉTODO: LISTAR DETALLES POR ID_VENTA ===
    // === 🔍 NUEVO MÉTODO: LISTAR DETALLES POR ID_VENTA ===
    public List<ModeloDetalleVenta> listarPorIdVenta(long idVenta) {
        List<ModeloDetalleVenta> lista = new ArrayList<>();
        String sql = """
        SELECT dv.*, p.NOMBRE_PRODUCTO AS nombre_producto
        FROM DETALLE_VENTA dv
        INNER JOIN PRODUCTOS p ON dv.ID_PRODUCTO = p.ID_PRODUCTO
        WHERE dv.ID_VENTA = ?
        ORDER BY dv.ID_DETALLE_VENTA ASC
    """;

        conexion.conectar();
        try {
            ps = conexion.preparar(sql);
            ps.setLong(1, idVenta);
            rs = ps.executeQuery();

            while (rs.next()) {
                ModeloDetalleVenta detalle = mapearResultado(rs);

                try {
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                } catch (SQLException ex) {
                    // Ignorar si el campo no existe en el modelo
                }

                lista.add(detalle);
            }

        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName())
                    .log(Level.SEVERE, "Error al listar detalles por id_venta", e);
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    // === MAPEO (ResultSet → Modelo) ===
    private ModeloDetalleVenta mapearResultado(ResultSet rs) throws SQLException {
        ModeloDetalleVenta detalle = new ModeloDetalleVenta();
        detalle.setIdDetalleVenta(rs.getLong("id_detalle_venta"));
        detalle.setIdVenta(rs.getLong("id_venta"));
        detalle.setIdProducto(rs.getLong("id_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioVenta(rs.getDouble("precio_venta"));
        detalle.setDescuento(rs.getDouble("descuento"));
        return detalle;
    }

    // === CERRAR CONEXIONES ===
    private void cerrarRecursos() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            conexion.desconectar();
        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasDAO.class.getName()).log(Level.WARNING, "Error al cerrar recursos", e);
        }
    }
}
