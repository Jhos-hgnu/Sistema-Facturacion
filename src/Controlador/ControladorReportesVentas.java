package Controlador;

import Implementacion.ReporteVentaDAO;
import Modelo.ModeloReporteVentaDia;
import Utilities.GeneradorReporteVentas;
import Vistas.PanelReportesVentas;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class ControladorReportesVentas {
    
    private ReporteVentaDAO reporteDao;
    private GeneradorReporteVentas csvGenerator;
    private PanelReportesVentas vista;
    private ControladorReportesVentas reportes;

    public ControladorReportesVentas() {
    }

    
    public ControladorReportesVentas(PanelReportesVentas vista, ReporteVentaDAO reporteDao, GeneradorReporteVentas csvGenerator) {
        this.reporteDao = reporteDao;
        this.csvGenerator = csvGenerator;
        this.vista = vista;
    }

   
    
    public void configuracionListeners(){
       // vista.getBtnReporteVentasDia().addActionListener(e -> generarReporteVentasDiaCSV());
        
    }
   
    
    /*no funciona*/
    
    
    
    
    public void generarReporteVentasDiaCSV() {
        System.out.println("Goll ");
        JOptionPane.showMessageDialog(null,
                            "Error al generar el reporte CSV",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
//        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
//            @Override
//            protected Boolean doInBackground() throws Exception {
//                try {
//                    // 1. Obtener datos
//                    List<ModeloReporteVentaDia> ventas = reporteDao.obtenerVentasDelDia();
//                    
//                    if (ventas.isEmpty()) {
//                        JOptionPane.showMessageDialog(null,
//                            "ℹ️ No hay ventas registradas para el día de hoy",
//                            "Sin datos",
//                            JOptionPane.INFORMATION_MESSAGE);
//                        return false;
//                    }
//                    
//                    // 2. Generar CSV
//                    String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
//                    String filePath = "reportes/ventas_dia_" + timestamp + ".csv";
//                    
//                    return csvGenerator.generarReporteVentasDiaCSV(ventas, filePath);
//                    
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//            
//            @Override
//            protected void done() {
//                try {
//                    boolean exito = get();
//                    
//                    if (exito) {
//                        int respuesta = JOptionPane.showConfirmDialog(null,
//                            "✅ Reporte CSV generado exitosamente\n" +
//                            "📁 Archivo guardado en carpeta 'reportes'\n\n" +
//                            "¿Deseas abrir el archivo?",
//                            "Éxito",
//                            JOptionPane.YES_NO_OPTION);
//                        
//                        if (respuesta == JOptionPane.YES_OPTION) {
//                            abrirArchivoCSV();
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null,
//                            "Error al generar el reporte CSV",
//                            "Error",
//                            JOptionPane.ERROR_MESSAGE);
//                    }
//                    
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null,
//                        "Error: " + e.getMessage(),
//                        "Error",
//                        JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        };
//        
//        worker.execute();
    }
    
    private void abrirArchivoCSV() {
        try {
            // Buscar el archivo más reciente en la carpeta reportes
            java.io.File carpeta = new java.io.File("reportes");
            java.io.File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("ventas_dia_") && name.endsWith(".csv"));
            
            if (archivos != null && archivos.length > 0) {
                // Ordenar por fecha de modificación (más reciente primero)
                java.util.Arrays.sort(archivos, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                
                // Abrir el archivo más reciente
                java.awt.Desktop.getDesktop().open(archivos[0]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "⚠️ No se pudo abrir el archivo automáticamente\n" +
                "Puedes abrirlo manualmente desde la carpeta 'reportes'",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
}
