/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import Controlador.ControladorReportesVentas;
import Modelo.TipoRankingCliente;
import Modelo.TipoRankingProducto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 *
 * @author jhosu
 */
public class PanelReportesVentas extends javax.swing.JPanel {

    ControladorReportesVentas controlador = new ControladorReportesVentas();

    /**
     * Creates new form PanelReportesVentas
     */
    public PanelReportesVentas() {
        initComponents();

        btnGroupFiltroCliente.add(radBtnCosto);
        btnGroupFiltroCliente.add(radBtnMonto);

        btnReporteVentaDia.addActionListener(e -> {
            controlador.generarReporteVentasDiaCSV();
        });

        btnVentasMensuales.addActionListener(e -> {
            controlador.generarReporteVentasMensuales();
        });

        btnMejoresClientes.addActionListener(e -> {

            datosVaciosMCLientes();

        });

        btnProductosVendidos.addActionListener(e -> {
            datosVaciosProMasVendidos();

        });
        
        btnVentasRango.addActionListener(e -> {
            datosVaciosVentasRangos();
        
        });

        //925 700
    }

    public void datosVaciosMCLientes() {
        int valor = (int) spinnerTopCliente.getValue();
        LocalDate fechaInicial = dateFechaFinalMC.getDate();
        LocalDate fechaFinal = dateFechaFinalMC.getDate();

        if (valor < 1) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar al menos un número igual o mayor a 1",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
        } else if (fechaFinal == null | fechaInicial == null | !(radBtnMonto.isSelected() | radBtnCosto.isSelected())) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar todos los datos",
                    "Acción inválida",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            extraerDatos();
        }

    }

    public void extraerDatos() {
        LocalDate fechaInicial = dateFechaFinalMC.getDate();
        LocalDate fechaFinal = dateFechaFinalMC.getDate();

        DateTimeFormatter formatterOracle = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.ENGLISH);

        String fechaInicialOracle = fechaInicial.format(formatterOracle);
        String fechaFinalOracle = fechaFinal.format(formatterOracle);
        int valor = (int) spinnerTopCliente.getValue();

        if (radBtnMonto.isSelected()) {
            controlador.generarReporteMejoresClientes(TipoRankingCliente.POR_MONTO, valor, fechaInicialOracle, fechaFinalOracle);
        } else {
            controlador.generarReporteMejoresClientes(TipoRankingCliente.POR_FRECUENCIA, valor, fechaInicialOracle, fechaFinalOracle);
        }

    }

    public void datosVaciosProMasVendidos() {

        int valor = (int) spinnerTopPMVendidos.getValue();
        LocalDate fechaInicial = dateFechaInicioPMasVend.getDate();
        LocalDate fechaFinal = dateFechaFinalPMasVen.getDate();
//
        if (fechaInicial == null | fechaFinal == null | !(radBtnMontoVendidos.isSelected() | radBtncantidad.isSelected())) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar todos los datos",
                    "Acción inválida",
                    JOptionPane.WARNING_MESSAGE);
        } else if (valor < 1) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar al menos un número igual o mayor a 1",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            extraerDatosProductosMasVend();
        }

    }

    public void extraerDatosProductosMasVend() {
        LocalDate fechaInicial = dateFechaInicioPMasVend.getDate();
        LocalDate fechaFinal = dateFechaFinalPMasVen.getDate();
        int valor = (int) spinnerTopPMVendidos.getValue();
        DateTimeFormatter formatterOracle = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.ENGLISH);

        String fechaInicialOracle = fechaInicial.format(formatterOracle);
        String fechaFinalOracle = fechaFinal.format(formatterOracle);

        if (radBtnMontoVendidos.isSelected()) {
            controlador.generarReporteProductosMasVendidosCSVFechas(TipoRankingProducto.POR_MONTO, valor, fechaInicialOracle, fechaFinalOracle);
        } else {
            controlador.generarReporteProductosMasVendidosCSVFechas(TipoRankingProducto.POR_CANTIDAD, valor, fechaInicialOracle, fechaFinalOracle);
        }

    }
    
    
    public void datosVaciosVentasRangos(){
        LocalDate fechaInicial = dateFechaInicioVenta.getDate();
        LocalDate fechaFinal = dateFechaFinalVentas.getDate();
        
        DateTimeFormatter formatterOracle = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.ENGLISH);
        
        String fechaInicialOracle = fechaInicial.format(formatterOracle);
        String fechaFinalOracle = fechaFinal.format(formatterOracle);
        
        
        
        if(fechaInicial == null && fechaFinal == null) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar todos los datos",
                    "Acción inválida",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            controlador.generarReporteVentasRangoCSV(fechaInicialOracle, fechaFinalOracle);
        }
        
        
        
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupFiltroCliente = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnReporteVentaDia = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnVentasRango = new javax.swing.JButton();
        dateFechaInicioVenta = new com.github.lgooddatepicker.components.DatePicker();
        dateFechaFinalVentas = new com.github.lgooddatepicker.components.DatePicker();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        btnVentasMensuales = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        btnMejoresClientes = new javax.swing.JButton();
        spinnerTopCliente = new javax.swing.JSpinner();
        radBtnMonto = new javax.swing.JRadioButton();
        radBtnCosto = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        dateFechaInicioMC = new com.github.lgooddatepicker.components.DatePicker();
        dateFechaFinalMC = new com.github.lgooddatepicker.components.DatePicker();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnProductosVendidos = new javax.swing.JButton();
        spinnerTopPMVendidos = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        radBtncantidad = new javax.swing.JRadioButton();
        radBtnMontoVendidos = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        dateFechaInicioPMasVend = new com.github.lgooddatepicker.components.DatePicker();
        dateFechaFinalPMasVen = new com.github.lgooddatepicker.components.DatePicker();
        titulo1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        marcaAgua = new javax.swing.JLabel();
        separador2 = new javax.swing.JSeparator();
        separador = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(28, 95, 118));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(75, 128, 146));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReporteVentaDia.setText("Generar Reporte ");
        btnReporteVentaDia.setBackground(new java.awt.Color(28, 95, 118));
        btnReporteVentaDia.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReporteVentaDia.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnReporteVentaDia.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(btnReporteVentaDia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 130, 30));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Ventas del Día");
        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 130, 20));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 290, 170));

        jPanel2.setBackground(new java.awt.Color(75, 128, 146));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Ventas por Rango de Fechas");
        jLabel13.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 210, 20));

        jLabel15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Fecha Inicio:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, 20));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Fecha Final:");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 100, 20));

        btnVentasRango.setText("Generar Reporte");
        btnVentasRango.setBackground(new java.awt.Color(28, 95, 118));
        btnVentasRango.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnVentasRango.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnVentasRango.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(btnVentasRango, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 130, 30));

        dateFechaInicioVenta.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.add(dateFechaInicioVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 190, -1));

        dateFechaFinalVentas.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.add(dateFechaFinalVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 190, -1));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 430, 420, 190));

        jPanel3.setBackground(new java.awt.Color(75, 128, 146));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Ventas Mensuales");
        jLabel14.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 130, 20));

        btnVentasMensuales.setText("Generar Reporte");
        btnVentasMensuales.setBackground(new java.awt.Color(28, 95, 118));
        btnVentasMensuales.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnVentasMensuales.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnVentasMensuales.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(btnVentasMensuales, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 130, 30));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 290, 160));

        jPanel4.setBackground(new java.awt.Color(75, 128, 146));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Mejores Clientes");
        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 130, 20));

        btnMejoresClientes.setText("Generar Reporte");
        btnMejoresClientes.setBackground(new java.awt.Color(28, 95, 118));
        btnMejoresClientes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMejoresClientes.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnMejoresClientes.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(btnMejoresClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 120, 30));
        jPanel4.add(spinnerTopCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        radBtnMonto.setBackground(new java.awt.Color(75, 128, 146));
        radBtnMonto.setForeground(new java.awt.Color(51, 51, 51));
        radBtnMonto.setText("Por monto ");
        jPanel4.add(radBtnMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, -1, -1));

        radBtnCosto.setBackground(new java.awt.Color(75, 128, 146));
        radBtnCosto.setForeground(new java.awt.Color(51, 51, 51));
        radBtnCosto.setText("Por Costo");
        jPanel4.add(radBtnCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Top:");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        dateFechaInicioMC.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.add(dateFechaInicioMC, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 190, -1));

        dateFechaFinalMC.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.add(dateFechaFinalMC, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 190, -1));

        jLabel3.setText("Fecha Fin");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel4.setText("Fecha Inicio");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, 420, 180));

        jPanel5.setBackground(new java.awt.Color(75, 128, 146));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Productos más Vendidos");
        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 190, 20));

        btnProductosVendidos.setText("Generar Reporte");
        btnProductosVendidos.setBackground(new java.awt.Color(28, 95, 118));
        btnProductosVendidos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProductosVendidos.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnProductosVendidos.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(btnProductosVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 110, 30));
        jPanel5.add(spinnerTopPMVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Top:");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        radBtncantidad.setText("Por cantidad");
        radBtncantidad.setBackground(new java.awt.Color(75, 128, 146));
        radBtncantidad.setForeground(new java.awt.Color(51, 51, 51));
        jPanel5.add(radBtncantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        radBtnMontoVendidos.setText("Por Monto");
        radBtnMontoVendidos.setBackground(new java.awt.Color(75, 128, 146));
        radBtnMontoVendidos.setForeground(new java.awt.Color(51, 51, 51));
        jPanel5.add(radBtnMontoVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Fecha Inicio");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Fecha Fin");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        dateFechaInicioPMasVend.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.add(dateFechaInicioPMasVend, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 190, -1));

        dateFechaFinalPMasVen.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.add(dateFechaFinalPMasVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 190, -1));

        add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 420, 160));

        titulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo1.setText("GENERAR REPORTES");
        titulo1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        titulo1.setForeground(new java.awt.Color(255, 255, 255));
        add(titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, -1));

        btnSalir.setBackground(new java.awt.Color(75, 128, 146));
        btnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("X");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 30, 30));

        add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 30, 30));

        marcaAgua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        marcaAgua.setText("FARMACIA");
        marcaAgua.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        marcaAgua.setForeground(new java.awt.Color(255, 255, 255));
        add(marcaAgua, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 680, 850, -1));

        separador2.setBackground(new java.awt.Color(255, 255, 255));
        separador2.setForeground(new java.awt.Color(255, 255, 255));
        add(separador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 850, 10));

        separador.setBackground(new java.awt.Color(255, 255, 255));
        separador.setForeground(new java.awt.Color(255, 255, 255));
        add(separador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, 850, 10));
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btnSalirMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupFiltroCliente;
    private javax.swing.JButton btnMejoresClientes;
    private javax.swing.JButton btnProductosVendidos;
    private javax.swing.JButton btnReporteVentaDia;
    private javax.swing.JPanel btnSalir;
    private javax.swing.JButton btnVentasMensuales;
    private javax.swing.JButton btnVentasRango;
    private com.github.lgooddatepicker.components.DatePicker dateFechaFinalMC;
    private com.github.lgooddatepicker.components.DatePicker dateFechaFinalPMasVen;
    private com.github.lgooddatepicker.components.DatePicker dateFechaFinalVentas;
    private com.github.lgooddatepicker.components.DatePicker dateFechaInicioMC;
    private com.github.lgooddatepicker.components.DatePicker dateFechaInicioPMasVend;
    private com.github.lgooddatepicker.components.DatePicker dateFechaInicioVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel marcaAgua;
    private javax.swing.JRadioButton radBtnCosto;
    private javax.swing.JRadioButton radBtnMonto;
    private javax.swing.JRadioButton radBtnMontoVendidos;
    private javax.swing.JRadioButton radBtncantidad;
    private javax.swing.JSeparator separador;
    private javax.swing.JSeparator separador2;
    private javax.swing.JSpinner spinnerTopCliente;
    private javax.swing.JSpinner spinnerTopPMVendidos;
    private javax.swing.JLabel titulo1;
    // End of variables declaration//GEN-END:variables
}
