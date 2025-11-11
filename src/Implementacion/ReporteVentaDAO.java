/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Conector.SQL;
import Modelo.ModeloMejorCliente;
import Modelo.ModeloProductoMasVendido;
import Modelo.ModeloReporteMensual;
import Modelo.ModeloReporteVentaDia;
import Modelo.ModeloVentaRangoFechas;
import Modelo.TipoRankingCliente;
import Modelo.TipoRankingProducto;
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

    
     public List<ModeloMejorCliente> obtenerMejoresClientes(TipoRankingCliente tipoRanking, int top, 
                                                          String fechaInicio, String fechaFin) {
        List<ModeloMejorCliente> clientes = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            // Calcular total general para porcentajes
            double totalGeneral = calcularTotalVentasPeriodo(fechaInicio, fechaFin);
            
            String sql = construirQueryMejoresClientes(tipoRanking);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
            pstmt.setInt(3, top);
            
            ResultSet rs = pstmt.executeQuery();
            
            int ranking = 1;
            while (rs.next()) {
                double montoCliente = rs.getDouble("monto_total");
                double porcentaje = (totalGeneral > 0) ? (montoCliente / totalGeneral) * 100 : 0;
                
                ModeloMejorCliente cliente = new ModeloMejorCliente(
                    rs.getString("nit"),
                    rs.getString("nombre_cliente"),
                    montoCliente,
                    rs.getInt("cantidad_facturas"),
                    porcentaje,
                    ranking++
                );
                clientes.add(cliente);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerMejoresClientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return clientes;
    }
    
    /**
     * Construye la query según el tipo de ranking
     */
    private String construirQueryMejoresClientes(TipoRankingCliente tipoRanking) {
        String orderBy = "";
        
        switch (tipoRanking) {
            case POR_MONTO:
                orderBy = "monto_total DESC";
                break;
            case POR_FRECUENCIA:
                orderBy = "cantidad_facturas DESC, monto_total DESC";
                break;
            default:
                orderBy = "monto_total DESC";
        }
        
        return "SELECT * FROM ( " +
               "    SELECT " +
               "        c.nit, " +
               "        c.primer_nombre || ' ' || c.primer_apellido as nombre_cliente, " +
               "        SUM(v.total) as monto_total, " +
               "        COUNT(v.id_venta) as cantidad_facturas " +
               "    FROM clientes c " +
               "    JOIN venta v ON c.nit = v.nit " +
               "    WHERE v.fecha BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1 " +
               "    GROUP BY c.nit, c.primer_nombre, c.primer_apellido " +
               "    ORDER BY " + orderBy +
               ") WHERE ROWNUM <= ?";
    }
    
    /**
     * Calcula el total de ventas en el período para los porcentajes
     */
    private double calcularTotalVentasPeriodo(String fechaInicio, String fechaFin) {
        double total = 0;
        
        try {
            Connection conn = dbConnection.getConnection();
            String sql = "SELECT NVL(SUM(total), 0) as total_periodo " +
                         "FROM venta " +
                         "WHERE fecha BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total_periodo");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculando total período: " + e.getMessage());
        }
        
        return total;
    }
    
    /**
     * Método simplificado para últimos 30 días
     */
    public List<ModeloMejorCliente> obtenerMejoresClientesUltimos30Dias(TipoRankingCliente tipoRanking, int top) {
        String fechaFin = java.time.LocalDate.now().toString();
        String fechaInicio = java.time.LocalDate.now().minusDays(30).toString();
        
        return obtenerMejoresClientes(tipoRanking, top, fechaInicio, fechaFin);
    }

 
    
    public List<ModeloProductoMasVendido> obtenerProductosMasVendidos(
            TipoRankingProducto tipoRanking, int top, String fechaInicio, String fechaFin) {
        
        List<ModeloProductoMasVendido> productos = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            // Calcular total general para porcentajes
            double totalGeneral = calcularTotalVentasPeriodo(fechaInicio, fechaFin);
            
            // Construir query según el tipo de ranking
            String sql = construirQueryProductos(tipoRanking);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
            pstmt.setInt(3, top);
            
            ResultSet rs = pstmt.executeQuery();
            
            int ranking = 1;
            while (rs.next()) {
                double montoProducto = rs.getDouble("monto_total");
                double porcentaje = (totalGeneral > 0) ? (montoProducto / totalGeneral) * 100 : 0;
                
                ModeloProductoMasVendido producto = new ModeloProductoMasVendido(
                    rs.getInt("id_producto"),
                    rs.getString("nombre_producto"),
                    rs.getString("categoria"),
                    rs.getInt("cantidad_vendida"),
                    montoProducto,
                    porcentaje,
                    ranking++
                );
                productos.add(producto);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerProductosMasVendidos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return productos;
    }
    
    /**
     * Construye la query SQL según el tipo de ranking
     */
    private String construirQueryProductos(TipoRankingProducto tipoRanking) {
        String orderBy = "";
        
        switch (tipoRanking) {
            case POR_CANTIDAD:
                orderBy = "cantidad_vendida DESC";
                break;
            case POR_MONTO:
                orderBy = "monto_total DESC";
                break;
            default:
                orderBy = "cantidad_vendida DESC";
        }
        
        return "SELECT * FROM ( " +
               "    SELECT " +
               "        p.id_producto, " +
               "        p.nombre_producto, " +
               "        c.nombre as categoria, " +
               "        SUM(dv.cantidad) as cantidad_vendida, " +
               "        SUM(dv.cantidad * dv.precio_venta) as monto_total " +
               "    FROM productos p " +
               "    JOIN detalle_venta dv ON p.id_producto = dv.id_producto " +
               "    JOIN venta v ON dv.id_venta = v.id_venta " +
               "    JOIN categorias c ON p.id_categoria = c.id_categoria " +
               "    WHERE v.fecha BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1 " +
               "    GROUP BY p.id_producto, p.nombre_producto, c.nombre " +
               "    ORDER BY " + orderBy +
               ") WHERE ROWNUM <= ?";
    }
    
    /**
     * Calcula el total de ventas en el período para los porcentajes
     */
//    private double calcularTotalVentasPeriodo(String fechaInicio, String fechaFin) {
//        double total = 0;
//        
//        try {
//            Connection conn = dbConnection.getConnection();
//            String sql = "SELECT NVL(SUM(total), 0) as total_periodo " +
//                         "FROM venta " +
//                         "WHERE fecha BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1";
//            
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, fechaInicio);
//            pstmt.setString(2, fechaFin);
//            ResultSet rs = pstmt.executeQuery();
//            
//            if (rs.next()) {
//                total = rs.getDouble("total_periodo");
//            }
//            
//            rs.close();
//            pstmt.close();
//            
//        } catch (SQLException e) {
//            System.err.println("❌ Error calculando total período: " + e.getMessage());
//        }
//        
//        return total;
//    }
    
    
    public List<ModeloVentaRangoFechas> obtenerVentasPorRangoFechas(String fechaInicio, String fechaFin) {
        List<ModeloVentaRangoFechas> ventas = new ArrayList<>();
        
        try {
            dbConnection.conectar();
            Connection conn = dbConnection.getConnection();
            
            String sql = "SELECT " +
                         "    TO_CHAR(v.fecha, 'YYYY-MM-DD') as fecha, " +
                         "    TO_CHAR(v.fecha, 'HH24:MI') as hora, " +
                         "    'F' || TO_CHAR(v.id_venta) as factura, " +
                         "    c.primer_nombre || ' ' || c.primer_apellido as cliente, " +
                         "    v.total, " +
                         "    u.primer_nombre || ' ' || u.primer_apellido as vendedor, " +
                         "    v.tipo_pago, " +
                         "    v.id_venta " +
                         "FROM venta v " +
                         "JOIN clientes c ON v.nit = c.nit " +
                         "JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                         "WHERE v.fecha BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1 " +
                         "ORDER BY v.fecha ASC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String productos = obtenerProductosVenta(rs.getInt("id_venta"));
                ModeloVentaRangoFechas venta = new ModeloVentaRangoFechas(
                    rs.getString("fecha"),
                    rs.getString("hora"),
                    rs.getString("factura"),
                    rs.getString("cliente"),
                    productos,
                    rs.getDouble("total"),
                    rs.getString("vendedor"),
                    rs.getString("tipo_pago")
                );
                ventas.add(venta);
            }
            
            rs.close();
            pstmt.close();
            
            System.out.println("✅ Se obtuvieron " + ventas.size() + " ventas del rango " + fechaInicio + " a " + fechaFin);
            
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenerVentasPorRangoFechas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.desconectar();
        }
        
        return ventas;
    }
    
}
