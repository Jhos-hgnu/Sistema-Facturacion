/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Modelo.ModeloVenta;
import Conector.DBConnection;
import Modelo.ModeloDetalleVenta;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisd
 */
public class VentasDAO {

    private final DBConnection conector = new DBConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Inserta una nueva venta en la base de datos. El ID se genera
     * automáticamente por la base (IDENTITY).
     */
    public boolean insertarVenta(ModeloVenta venta) {
        boolean resultado = false;
        conector.conectar();

        try {
            String sql = """
                    INSERT INTO venta (nit, fecha, tipo_pago, documento, total, id_usuario, observacion)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

            ps = conector.preparar(sql);
            ps.setString(1, venta.getNit());

            // Si no viene fecha, asigna la actual
            Timestamp fechaSQL = venta.getFecha() != null
                    ? new Timestamp(venta.getFecha().getTime())
                    : Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(2, fechaSQL);

            ps.setString(3, venta.getTipoPago());
            ps.setString(4, venta.getDocumento());
            ps.setDouble(5, venta.getTotal());
            ps.setLong(6, venta.getIdUsuario());
            ps.setString(7, venta.getObservacion());

            resultado = ps.executeUpdate() > 0;

            if (resultado) {
                System.out.println("✅ Venta insertada correctamente.");
            }

        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName()).log(Level.SEVERE, "❌ Error al insertar venta", e);
        } finally {
            cerrarRecursos();
        }

        return resultado;
    }

    /**
     * Obtiene una venta por su ID.
     */
    public ModeloVenta obtenerVentaPorId(long idVenta) {
        ModeloVenta venta = null;
        conector.conectar();

        try {
            String sql = "SELECT * FROM venta WHERE id_venta = ?";
            ps = conector.preparar(sql);
            ps.setLong(1, idVenta);

            rs = ps.executeQuery();
            if (rs.next()) {
                venta = mapearVenta(rs);
            }

        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName()).log(Level.SEVERE, "❌ Error al obtener venta", e);
        } finally {
            cerrarRecursos();
        }

        return venta;
    }

    /**
     * Devuelve una lista con todas las ventas registradas.
     */
    public List<ModeloVenta> listarVentas() {
        List<ModeloVenta> lista = new ArrayList<>();
        conector.conectar();

        try {
            String sql = "SELECT * FROM venta ORDER BY fecha DESC";
            ps = conector.preparar(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearVenta(rs));
            }

        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName()).log(Level.SEVERE, "❌ Error al listar ventas", e);
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Actualiza una venta existente.
     */
    public boolean actualizarVenta(ModeloVenta venta) {
        boolean resultado = false;
        conector.conectar();

        try {
            String sql = """
                    UPDATE venta
                    SET nit = ?, fecha = ?, tipo_pago = ?, documento = ?, total = ?,
                        id_usuario = ?, observacion = ?
                    WHERE id_venta = ?
                    """;

            ps = conector.preparar(sql);
            ps.setString(1, venta.getNit());
            ps.setTimestamp(2, new Timestamp(venta.getFecha().getTime()));
            ps.setString(3, venta.getTipoPago());
            ps.setString(4, venta.getDocumento());
            ps.setDouble(5, venta.getTotal());
            ps.setLong(6, venta.getIdUsuario());
            ps.setString(7, venta.getObservacion());
            ps.setLong(8, venta.getIdVenta());

            resultado = ps.executeUpdate() > 0;

            if (resultado) {
                System.out.println("✏️ Venta actualizada correctamente: ID " + venta.getIdVenta());
            }

        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName()).log(Level.SEVERE, "❌ Error al actualizar venta", e);
        } finally {
            cerrarRecursos();
        }

        return resultado;
    }

    /**
     * Elimina una venta por su ID.
     */
    public boolean eliminarVenta(long idVenta) {
        boolean resultado = false;
        conector.conectar();

        try {
            // 🔹 1. Eliminar los detalles de esa venta primero
            DetalleVentasDAO detalleDAO = new DetalleVentasDAO();
            List<ModeloDetalleVenta> detalles = detalleDAO.listarPorIdVenta(idVenta);
            for (ModeloDetalleVenta detalle : detalles) {
                detalleDAO.eliminarDetalleVenta(detalle.getIdDetalleVenta());
            }

            // 🔹 2. Luego eliminar la venta principal
            String sql = "DELETE FROM venta WHERE id_venta = ?";
            ps = conector.preparar(sql);
            ps.setLong(1, idVenta);
            resultado = ps.executeUpdate() > 0;

            if (resultado) {
                System.out.println("🗑️ Venta y detalles eliminados correctamente: ID " + idVenta);
            }

        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName())
                    .log(Level.SEVERE, "❌ Error al eliminar venta en cascada", e);
        } finally {
            cerrarRecursos();
        }

        return resultado;
    }

    // ===============================================
    // 🔹 Métodos auxiliares
    // ===============================================
    private ModeloVenta mapearVenta(ResultSet rs) throws SQLException {
        ModeloVenta venta = new ModeloVenta();
        venta.setIdVenta(rs.getLong("id_venta"));
        venta.setNit(rs.getString("nit"));
        venta.setFecha(rs.getDate("fecha"));
        venta.setTipoPago(rs.getString("tipo_pago"));
        venta.setDocumento(rs.getString("documento"));
        venta.setTotal(rs.getDouble("total"));
        venta.setIdUsuario(rs.getLong("id_usuario"));
        venta.setObservacion(rs.getString("observacion"));
        return venta;
    }

    private void cerrarRecursos() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            conector.desconectar();
        } catch (SQLException e) {
            Logger.getLogger(VentasDAO.class.getName()).log(Level.WARNING, "⚠️ Error al cerrar recursos", e);
        }
    }

}
