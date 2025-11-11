/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Conector.SQL;
import Modelo.ModeloReporteVentaDia;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jhosu
 */
public class ReporteVentaDAO {
    
   private DBConnection dbConnection;
    
    public ReporteVentaDAO() {
        this.dbConnection = new DBConnection();
    }
    
    public List<ModeloReporteVentaDia> obtenerVentasDelDia() {
        List<ModeloReporteVentaDia> ventas = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            String sql = "SELECT " +
                         "    TO_CHAR(v.fecha, 'HH24:MI') as hora, " +
                         "    'F' || TO_CHAR(v.id_venta) as factura, " +
                         "    c.primer_nombre || ' ' || c.primer_apellido as cliente, " +
                         "    v.total, " +
                         "    u.primer_nombre || ' ' || u.primer_apellido as vendedor, " +
                         "    v.id_venta " +
                         "FROM venta v " +
                         "JOIN clientes c ON v.nit = c.nit " +
                         "JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                         "WHERE TRUNC(v.fecha) = TRUNC(SYSDATE) " +
                         "ORDER BY v.fecha ASC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String productos = obtenerProductosVenta(rs.getInt("id_venta"));
                ModeloReporteVentaDia venta = new ModeloReporteVentaDia(
                    rs.getString("hora"),
                    rs.getString("factura"),
                    rs.getString("cliente"),
                    productos,
                    rs.getDouble("total"),
                    rs.getString("vendedor")
                );
                ventas.add(venta);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerVentasDelDia: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return ventas;
    }
    
    private String obtenerProductosVenta(int idVenta) {
        StringBuilder productos = new StringBuilder();
        
        try {
            Connection conn = dbConnection.getConnection();
            String sql = "SELECT p.nombre_producto, dv.cantidad " +
                         "FROM detalle_venta dv " +
                         "JOIN productos p ON dv.id_producto = p.id_producto " +
                         "WHERE dv.id_venta = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                if (productos.length() > 0) {
                    productos.append(", ");
                }
                productos.append(rs.getString("nombre_producto"))
                         .append(" (")
                         .append(rs.getInt("cantidad"))
                         .append(")");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerProductosVenta: " + e.getMessage());
        }
        
        return productos.toString();
    }
    
    // Método para obtener totales del día (opcional, para el resumen)
    public double obtenerTotalVentasDelDia() {
        double total = 0;
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            String sql = "SELECT NVL(SUM(total), 0) as total_dia " +
                         "FROM venta " +
                         "WHERE TRUNC(fecha) = TRUNC(SYSDATE)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total_dia");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerTotalVentasDelDia: " + e.getMessage());
        } finally {
            dbConnection.desconectar();
        }
        
        return total;
    }
    
    
}
