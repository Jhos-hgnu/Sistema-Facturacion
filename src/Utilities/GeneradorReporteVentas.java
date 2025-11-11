/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author jhosu
 */
import Modelo.ModeloClientesVentas;
import Modelo.ModeloDetalleVenta;
import Modelo.ModeloMejorCliente;
import Modelo.ModeloProductoMasVendido;
import Modelo.ModeloReporteMensual;
import Modelo.ModeloReporteVentaDia;
import Modelo.ModeloVentaRangoFechas;
import Modelo.TipoRankingCliente;
import Modelo.TipoRankingProducto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;

public class GeneradorReporteVentas {

   public boolean generarReporteVentasDiaCSV(List<ModeloReporteVentaDia> ventas, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // 1. Escribir encabezado
            writer.append("HORA,FACTURA,CLIENTE,PRODUCTOS,VENDEDOR,TOTAL\n");
            
            // 2. Escribir datos
            for (ModeloReporteVentaDia venta : ventas) {
                writer.append(escapeCSV(venta.getHora())).append(",");
                writer.append(escapeCSV(venta.getNumeroFactura())).append(",");
                writer.append(escapeCSV(venta.getCliente())).append(",");
                writer.append(escapeCSV(venta.getProductos())).append(",");
                writer.append(escapeCSV(venta.getVendedor())).append(",");
                writer.append(String.format("%.2f", venta.getTotal())).append("\n");
            }
            
            // 3. Escribir totales al final
            double totalDia = ventas.stream().mapToDouble(ModeloReporteVentaDia::getTotal).sum();
            writer.append("\n");
            writer.append("TOTAL DEL DÍA:,").append(String.format("%.2f", totalDia)).append("\n");
            writer.append("CANTIDAD DE VENTAS:,").append(String.valueOf(ventas.size())).append("\n");
            writer.append("FECHA:,").append(java.time.LocalDate.now().toString()).append("\n");
            
            System.out.println("CSV generado: " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al generar CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Método para escapar caracteres especiales en CSV
    private String escapeCSV(String value) {
        if (value == null) return "";
        
        // Si contiene comas, comillas o saltos de línea, encerrar entre comillas
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\""); // Escapar comillas internas
            value = "\"" + value + "\"";
        }
        return value;
    }
    
    public static String obtenerRutaComprobantes() {
        String escritorio = System.getProperty("user.home") + File.separator + "Desktop";
        String carpetaComprobantes = escritorio + File.separator + "comprobantesVentas";

        //Crear carpeta si no existe
        File directorio = new File(carpetaComprobantes);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
        return carpetaComprobantes;
    }
    
    
    
    
    
    
    
     public boolean generarReporteMensualCSV(List<ModeloReporteMensual> datosMensuales, String filePath) {
        // Crear directorio
        java.io.File archivo = new java.io.File(filePath);
        java.io.File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(archivo)) {
            // Encabezado del reporte
            writer.append("REPORTE COMPARATIVO - ÚLTIMOS 6 MESES\n");
            writer.append("Sistema de Farmacia\n");
            writer.append("Fecha de generación:,").append(new java.util.Date().toString()).append("\n");
            writer.append("Período analizado:,").append("Últimos " + datosMensuales.size() + " meses\n\n");
            
            // Encabezado de la tabla
            writer.append("MES,VENTAS DEL MES,VENTAS MES ANTERIOR,DIFERENCIA Q,DIFERENCIA %,TENDENCIA\n");
            
            // Datos
            double totalVentasActual = 0;
            double totalVentasAnterior = 0;
            int mesesConCrecimiento = 0;
            
            for (ModeloReporteMensual mes : datosMensuales) {
                writer.append(escapeCSV(mes.getMes())).append(",");
                writer.append(String.format("Q%.2f", mes.getVentasMes())).append(",");
                writer.append(String.format("Q%.2f", mes.getVentasMesAnterior())).append(",");
                writer.append(String.format("Q%.2f", mes.getDiferenciaMonto())).append(",");
                writer.append(String.format("%.2f%%", mes.getDiferenciaPorcentaje())).append(",");
                writer.append(mes.getTendencia()).append("\n");
                
                totalVentasActual += mes.getVentasMes();
                totalVentasAnterior += mes.getVentasMesAnterior();
                
                if (mes.getDiferenciaMonto() > 0) {
                    mesesConCrecimiento++;
                }
            }
            
            // Resumen ejecutivo
            writer.append("\n");
            writer.append("RESUMEN EJECUTIVO\n");
            writer.append("Total ventas período actual:,").append(String.format("Q%.2f", totalVentasActual)).append("\n");
            writer.append("Total ventas período anterior:,").append(String.format("Q%.2f", totalVentasAnterior)).append("\n");
            
            double crecimientoTotal = totalVentasActual - totalVentasAnterior;
            double crecimientoPorcentaje = (totalVentasAnterior != 0) ? 
                (crecimientoTotal / totalVentasAnterior) * 100 : 0;
                
            writer.append("Crecimiento total:,").append(String.format("Q%.2f", crecimientoTotal)).append("\n");
            writer.append("Crecimiento porcentual:,").append(String.format("%.2f%%", crecimientoPorcentaje)).append("\n");
            writer.append("Meses con crecimiento:,").append(mesesConCrecimiento + " de " + datosMensuales.size()).append("\n");
            
            // Análisis de tendencia
            writer.append("\n");
            writer.append("ANÁLISIS DE TENDENCIA\n");
            if (crecimientoPorcentaje > 0) {
                writer.append("Tendencia general:,").append("POSITIVA ▲\n");
            } else if (crecimientoPorcentaje < 0) {
                writer.append("Tendencia general:,").append("NEGATIVA ▼\n");
            } else {
                writer.append("Tendencia general:,").append("ESTABLE -\n");
            }
            
            System.out.println("✅ Reporte últimos 6 meses CSV generado: " + archivo.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error al generar reporte mensual CSV: " + e.getMessage());
            return false;
        }
    }
    
//    private String escapeCSV(String value) {
//        if (value == null) return "";
//        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
//            value = value.replace("\"", "\"\"");
//            value = "\"" + value + "\"";
//        }
//        return value;
//    }
    

     
     public boolean generarReporteMejoresClientesCSV(List<ModeloMejorCliente> clientes, 
                                                   TipoRankingCliente tipoRanking, 
                                                   String periodo, String filePath) {
        // Crear directorio
        java.io.File archivo = new java.io.File(filePath);
        java.io.File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(archivo)) {
            // Encabezado del reporte
            writer.append("REPORTE DE MEJORES CLIENTES\n");
            writer.append("Criterio:,").append(tipoRanking.getDescripcion()).append("\n");
            writer.append("Período:,").append(periodo).append("\n");
            writer.append("Fecha de generación:,").append(new java.util.Date().toString()).append("\n\n");
            
            // Encabezado de la tabla
            writer.append("RANKING,NIT,CLIENTE,MONTO TOTAL,CANTIDAD FACTURAS,TICKET PROMEDIO,% DEL TOTAL\n");
            
            // Datos
            double totalMonto = 0;
            int totalFacturas = 0;
            
            for (ModeloMejorCliente cliente : clientes) {
                writer.append(String.valueOf(cliente.getRanking())).append(",");
                writer.append(escapeCSV(cliente.getNit())).append(",");
                writer.append(escapeCSV(cliente.getNombreCliente())).append(",");
                writer.append(cliente.getMontoTotalFormateado()).append(",");
                writer.append(String.valueOf(cliente.getCantidadFacturas())).append(",");
                writer.append(cliente.getTicketPromedioFormateado()).append(",");
                writer.append(cliente.getPorcentajeFormateado()).append("\n");
                
                totalMonto += cliente.getMontoTotal();
                totalFacturas += cliente.getCantidadFacturas();
            }
            
            // Resumen
            writer.append("\n");
            writer.append("RESUMEN DEL REPORTE\n");
            writer.append("Total clientes en el reporte:,").append(String.valueOf(clientes.size())).append("\n");
            writer.append("Monto total de clientes reportados:,").append(String.format("Q%.2f", totalMonto)).append("\n");
            writer.append("Facturas totales de clientes reportados:,").append(String.valueOf(totalFacturas)).append("\n");
            writer.append("Ticket promedio general:,").append(String.format("Q%.2f", totalFacturas > 0 ? totalMonto / totalFacturas : 0)).append("\n");
            
            // Análisis adicional según el tipo de ranking
            writer.append("\n");
            writer.append("ANÁLISIS POR CRITERIO: ").append(tipoRanking.getDescripcion()).append("\n");
            
            if (tipoRanking == TipoRankingCliente.POR_MONTO) {
                writer.append("Cliente con mayor monto:,").append(escapeCSV(clientes.get(0).getNombreCliente())).append("\n");
                writer.append("Monto del top cliente:,").append(clientes.get(0).getMontoTotalFormateado()).append("\n");
            } else {
                writer.append("Cliente más frecuente:,").append(escapeCSV(clientes.get(0).getNombreCliente())).append("\n");
                writer.append("Facturas del top cliente:,").append(String.valueOf(clientes.get(0).getCantidadFacturas())).append("\n");
            }
            
            System.out.println("✅ Reporte mejores clientes CSV generado: " + archivo.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error al generar reporte mejores clientes CSV: " + e.getMessage());
            return false;
        }
    }
     
     
     
     public boolean generarReporteProductosMasVendidos( List<ModeloProductoMasVendido> productos, 
            TipoRankingProducto tipoRanking,
            int top, String periodo, String filePath){
         
         
         // Crear directorio si no existe
        java.io.File archivo = new java.io.File(filePath);
        java.io.File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(archivo)) {
            // 1. ENCABEZADO DEL REPORTE
            writer.append("REPORTE DE PRODUCTOS MÁS VENDIDOS\n");
            writer.append("Criterio:,").append(tipoRanking.getDescripcion()).append("\n");
            writer.append("Top:,").append(String.valueOf(top)).append(" productos\n");
            writer.append("Período:,").append(periodo).append("\n");
            writer.append("Fecha de generación:,").append(new java.util.Date().toString()).append("\n\n");
            
            // 2. ENCABEZADO DE LA TABLA
            writer.append("RANKING,ID PRODUCTO,PRODUCTO,CATEGORIA,CANTIDAD VENDIDA,MONTO TOTAL,PRECIO PROMEDIO,% DEL TOTAL\n");
            
            // 3. DATOS DE PRODUCTOS
            int totalUnidades = 0;
            double totalMonto = 0;
            
            for (ModeloProductoMasVendido producto : productos) {
                writer.append(String.valueOf(producto.getRanking())).append(",");
                writer.append(String.valueOf(producto.getIdProducto())).append(",");
                writer.append(escapeCSV(producto.getNombreProducto())).append(",");
                writer.append(escapeCSV(producto.getCategoria())).append(",");
                writer.append(String.valueOf(producto.getCantidadVendida())).append(",");
                writer.append(producto.getMontoTotalFormateado()).append(",");
                writer.append(producto.getPrecioPromedioFormateado()).append(",");
                writer.append(producto.getPorcentajeFormateado()).append("\n");
                
                totalUnidades += producto.getCantidadVendida();
                totalMonto += producto.getMontoTotal();
            }
            
            // 4. RESUMEN Y ANÁLISIS
            writer.append("\n");
            writer.append("RESUMEN DEL REPORTE\n");
            writer.append("Total productos en reporte:,").append(String.valueOf(productos.size())).append("\n");
            writer.append("Total unidades vendidas:,").append(String.valueOf(totalUnidades)).append("\n");
            writer.append("Monto total de productos:,").append(String.format("Q%.2f", totalMonto)).append("\n");
            writer.append("Unidades promedio por producto:,").append(String.format("%.1f", (double) totalUnidades / productos.size())).append("\n");
            
            // 5. ANÁLISIS ESPECÍFICO POR CRITERIO
            writer.append("\n");
            writer.append("ANÁLISIS - ").append(tipoRanking.getDescripcion()).append("\n");
            
            if (!productos.isEmpty()) {
                ModeloProductoMasVendido topProducto = productos.get(0);
                
                if (tipoRanking == TipoRankingProducto.POR_CANTIDAD) {
                    writer.append("Producto más vendido:,").append(escapeCSV(topProducto.getNombreProducto())).append("\n");
                    writer.append("Unidades vendidas:,").append(String.valueOf(topProducto.getCantidadVendida())).append("\n");
                    writer.append("Porcentaje del top 1:,").append(String.format("%.1f%%", (double) topProducto.getCantidadVendida() / totalUnidades * 100)).append("\n");
                } else {
                    writer.append("Producto que más genera:,").append(escapeCSV(topProducto.getNombreProducto())).append("\n");
                    writer.append("Monto generado:,").append(topProducto.getMontoTotalFormateado()).append("\n");
                    writer.append("Porcentaje del top 1:,").append(topProducto.getPorcentajeFormateado()).append("\n");
                }
            }
            
            System.out.println("Reporte productos más vendidos CSV generado: " + archivo.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al generar reporte productos CSV: " + e.getMessage());
            return false;
        }
         
     }
     
     
     /**
     * Genera reporte CSV de ventas por rango de fechas
     * Similar al de ventas del día pero con más detalles
     */
    public boolean generarReporteVentasRangoCSV(List<ModeloVentaRangoFechas> ventas, 
                                               String fechaInicio, String fechaFin, 
                                               String filePath) {
        // Crear directorio
        java.io.File archivo = new java.io.File(filePath);
        java.io.File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(archivo)) {
            // 1. ENCABEZADO DEL REPORTE
            writer.append("REPORTE DE VENTAS POR RANGO DE FECHAS\n");
            writer.append("Período:,").append(fechaInicio).append(" a ").append(fechaFin).append("\n");
            writer.append("Fecha de generación:,").append(new java.util.Date().toString()).append("\n");
            writer.append("Total de transacciones:,").append(String.valueOf(ventas.size())).append("\n\n");
            
            // 2. ENCABEZADO DE LA TABLA
            writer.append("FECHA,HORA,FACTURA,CLIENTE,PRODUCTOS,VENDEDOR,TIPO PAGO,TOTAL\n");
            
            // 3. DATOS DE VENTAS
            double totalGeneral = 0;
            int contadorCredito = 0;
            int contadorContado = 0;
            
            for (ModeloVentaRangoFechas venta : ventas) {
                writer.append(escapeCSV(venta.getFecha())).append(",");
                writer.append(escapeCSV(venta.getHora())).append(",");
                writer.append(escapeCSV(venta.getNumeroFactura())).append(",");
                writer.append(escapeCSV(venta.getCliente())).append(",");
                writer.append(escapeCSV(venta.getProductos())).append(",");
                writer.append(escapeCSV(venta.getVendedor())).append(",");
                writer.append(escapeCSV(venta.getTipoPago())).append(",");
                writer.append(venta.getTotalFormateado()).append("\n");
                
                totalGeneral += venta.getTotal();
                
                // Contar tipos de pago
                if ("CREDITO".equalsIgnoreCase(venta.getTipoPago())) {
                    contadorCredito++;
                } else {
                    contadorContado++;
                }
            }
            
            // 4. RESUMEN Y ESTADÍSTICAS
            writer.append("\n");
            writer.append("RESUMEN DEL PERÍODO\n");
            writer.append("Total ventas del período:,").append(String.format("Q%.2f", totalGeneral)).append("\n");
            writer.append("Cantidad total de transacciones:,").append(String.valueOf(ventas.size())).append("\n");
            writer.append("Ventas al contado:,").append(String.valueOf(contadorContado)).append("\n");
            writer.append("Ventas a crédito:,").append(String.valueOf(contadorCredito)).append("\n");
            writer.append("Ticket promedio:,").append(String.format("Q%.2f", ventas.size() > 0 ? totalGeneral / ventas.size() : 0)).append("\n");
            
            // 5. ANÁLISIS ADICIONAL
            writer.append("\n");
            writer.append("ANÁLISIS ADICIONAL\n");
            writer.append("Porcentaje ventas contado:,").append(String.format("%.1f%%", (double) contadorContado / ventas.size() * 100)).append("\n");
            writer.append("Porcentaje ventas crédito:,").append(String.format("%.1f%%", (double) contadorCredito / ventas.size() * 100)).append("\n");
            
            // Ventas por día (si hay datos)
            if (!ventas.isEmpty()) {
                Map<String, Double> ventasPorDia = calcularVentasPorDia(ventas);
                writer.append("\n");
                writer.append("VENTAS POR DÍA\n");
                for (Map.Entry<String, Double> entry : ventasPorDia.entrySet()) {
                    writer.append(entry.getKey()).append(",").append(String.format("Q%.2f", entry.getValue())).append("\n");
                }
            }
            
            System.out.println("Reporte ventas por rango CSV generado: " + archivo.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al generar reporte ventas rango CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Calcula ventas totales por día para el análisis
     */
    private Map<String, Double> calcularVentasPorDia(List<ModeloVentaRangoFechas> ventas) {
        Map<String, Double> ventasPorDia = new LinkedHashMap<>(); // LinkedHashMap mantiene el orden
        
        for (ModeloVentaRangoFechas venta : ventas) {
            String fecha = venta.getFecha();
            ventasPorDia.put(fecha, ventasPorDia.getOrDefault(fecha, 0.0) + venta.getTotal());
        }
        
        return ventasPorDia;
    }
    
   
     
     
     
     
     
     
     
     
     
     

}