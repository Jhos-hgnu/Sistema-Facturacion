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
import Modelo.ModeloReporteVentaDia;
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
            
            System.out.println("✅ CSV generado: " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error al generar CSV: " + e.getMessage());
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
}