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
import Modelo.ModeloReporteMensual;
import Modelo.ModeloReporteVentaDia;
import Modelo.TipoRankingCliente;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

}