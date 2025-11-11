/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public class ModeloReporteMensual {
    
    private String mes;
    private double ventasMes;
    private double ventasMesAnterior;
    private double diferenciaMonto;
    private double diferenciaPorcentaje;
    private String tendencia; // "▲" o "▼"


       public ModeloReporteMensual(String mes, double ventasMes, double ventasMesAnterior) {
        this.mes = mes;
        this.ventasMes = ventasMes;
        this.ventasMesAnterior = ventasMesAnterior;
        calcularDiferencias();
    }
    
    
    
    
     private void calcularDiferencias() {
        // Diferencia en monto
        this.diferenciaMonto = ventasMes - ventasMesAnterior;
        
        // Diferencia en porcentaje (evitar división por cero)
        if (ventasMesAnterior != 0) {
            this.diferenciaPorcentaje = (diferenciaMonto / ventasMesAnterior) * 100;
        } else {
            this.diferenciaPorcentaje = (ventasMes != 0) ? 100.0 : 0.0;
        }
        
        // Determinar tendencia
        this.tendencia = (diferenciaMonto >= 0) ? "▲" : "▼";
    }


// GETTERS Y SETTERS
    
    public String getMes() { 
        return mes; 
    }
    
    public void setMes(String mes) { 
        this.mes = mes; 
    }
    
    public double getVentasMes() { 
        return ventasMes; 
    }
    
    public void setVentasMes(double ventasMes) { 
        this.ventasMes = ventasMes;
        calcularDiferencias(); // Recalcular al cambiar valor
    }
    
    public double getVentasMesAnterior() { 
        return ventasMesAnterior; 
    }
    
    public void setVentasMesAnterior(double ventasMesAnterior) { 
        this.ventasMesAnterior = ventasMesAnterior;
        calcularDiferencias(); // Recalcular al cambiar valor
    }
    
    public double getDiferenciaMonto() { 
        return diferenciaMonto; 
    }
    
    public double getDiferenciaPorcentaje() { 
        return diferenciaPorcentaje; 
    }
    
    public String getTendencia() { 
        return tendencia; 
    }
    
    /**
     * Método auxiliar para obtener la diferencia formateada como string
     */
    public String getDiferenciaMontoFormateada() {
        return String.format("Q%.2f", diferenciaMonto);
    }
    
    /**
     * Método auxiliar para obtener el porcentaje formateado como string
     */
    public String getDiferenciaPorcentajeFormateada() {
        return String.format("%.2f%%", diferenciaPorcentaje);
    }
    
    /**
     * Método auxiliar para obtener las ventas del mes formateadas
     */
    public String getVentasMesFormateadas() {
        return String.format("Q%.2f", ventasMes);
    }
    
    /**
     * Método auxiliar para obtener las ventas del mes anterior formateadas
     */
    public String getVentasMesAnteriorFormateadas() {
        return String.format("Q%.2f", ventasMesAnterior);
    }
    
    /**
     * Indica si el mes tuvo crecimiento positivo
     */
    public boolean isCrecimientoPositivo() {
        return diferenciaMonto > 0;
    }
    
    /**
     * Indica si el mes tuvo crecimiento negativo
     */
    public boolean isCrecimientoNegativo() {
        return diferenciaMonto < 0;
    }
    
    /**
     * Indica si el mes se mantuvo estable
     */
    public boolean isEstable() {
        return diferenciaMonto == 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s: Q%.2f (vs Q%.2f) - %s Q%.2f (%.2f%%)", 
            mes, ventasMes, ventasMesAnterior, tendencia, diferenciaMonto, diferenciaPorcentaje);
    }







    
}

