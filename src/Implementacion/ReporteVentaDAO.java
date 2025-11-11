/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Conector.SQL;
import Modelo.ModeloReporteMensual;
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
            System.out.println("Hola DAO");
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
    
    
    public List<ModeloReporteMensual> obtenerVentasUltimos6Meses() {
        List<ModeloReporteMensual> reporte = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            // Consulta optimizada para últimos 6 meses
            String sql = "WITH meses AS ( " +
                         "    SELECT " +
                         "        ADD_MONTHS(TRUNC(SYSDATE, 'MM'), - (LEVEL - 1)) as mes_actual, " +
                         "        ADD_MONTHS(TRUNC(SYSDATE, 'MM'), - LEVEL) as mes_anterior " +
                         "    FROM dual " +
                         "    CONNECT BY LEVEL <= 6 " +
                         ") " +
                         "SELECT " +
                         "    TO_CHAR(m.mes_actual, 'MM/YYYY') as mes_formateado, " +
                         "    TO_CHAR(m.mes_actual, 'YYYY-MM') as mes_orden, " +
                         "    NVL((SELECT SUM(total) FROM venta " +
                         "         WHERE TRUNC(fecha, 'MM') = m.mes_actual), 0) as ventas_actual, " +
                         "    NVL((SELECT SUM(total) FROM venta " +
                         "         WHERE TRUNC(fecha, 'MM') = m.mes_anterior), 0) as ventas_anterior " +
                         "FROM meses m " +
                         "ORDER BY m.mes_actual DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ModeloReporteMensual mes = new ModeloReporteMensual(
                    rs.getString("mes_formateado"), // Ej: "01/2024"
                    rs.getDouble("ventas_actual"),
                    rs.getDouble("ventas_anterior")
                );
                reporte.add(mes);
            }
            
            rs.close();
            pstmt.close();
            
            System.out.println("✅ Se obtuvieron " + reporte.size() + " meses de datos");
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerVentasUltimos6Meses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return reporte;
    }
    
    // Método alternativo si el anterior falla
    public List<ModeloReporteMensual> obtenerVentasUltimos6MesesAlternativo() {
        List<ModeloReporteMensual> reporte = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            // Obtener lista de los últimos 6 meses
            List<String> ultimos6Meses = obtenerListaUltimos6Meses();
            
            // Para cada mes, obtener ventas
            for (int i = 0; i < ultimos6Meses.size(); i++) {
                String mesActual = ultimos6Meses.get(i);
                String mesAnterior = (i < ultimos6Meses.size() - 1) ? ultimos6Meses.get(i + 1) : null;
                
                double ventasActual = obtenerVentasPorMes(mesActual);
                double ventasAnterior = (mesAnterior != null) ? obtenerVentasPorMes(mesAnterior) : 0;
                
                ModeloReporteMensual mesReporte = new ModeloReporteMensual(
                    formatearMes(mesActual), // Convertir "2024-01" a "Enero 2024"
                    ventasActual, 
                    ventasAnterior
                );
                reporte.add(mesReporte);
            }
            
        } catch (Exception e) {
//            System.err.println("❌ Error en método alternativo: " + e.getMessage());
//            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return reporte;
    }
    
    private List<String> obtenerListaUltimos6Meses() {
        List<String> meses = new ArrayList<>();
        
        try {
            Connection conn = dbConnection.getConnection();
            String sql = "SELECT TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MM'), - (LEVEL - 1)), 'YYYY-MM') as mes " +
                         "FROM dual " +
                         "CONNECT BY LEVEL <= 6 " +
                         "ORDER BY mes DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                meses.add(rs.getString("mes"));
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error obteniendo lista de meses: " + e.getMessage());
        }
        
        return meses;
    }
    
    private double obtenerVentasPorMes(String mes) {
        double ventas = 0;
        
        try {
            Connection conn = dbConnection.getConnection();
            String sql = "SELECT NVL(SUM(total), 0) as total_ventas " +
                         "FROM venta " +
                         "WHERE TO_CHAR(fecha, 'YYYY-MM') = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mes);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ventas = rs.getDouble("total_ventas");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerVentasPorMes: " + e.getMessage());
        }
        
        return ventas;
    }
    
    private String formatearMes(String mes) {
        // Convertir "2024-01" a "Enero 2024"
        try {
            java.time.YearMonth yearMonth = java.time.YearMonth.parse(mes);
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", new java.util.Locale("es"));
            return yearMonth.format(formatter);
        } catch (Exception e) {
            return mes; // Si falla, devolver el original
        }
    }

    
}
