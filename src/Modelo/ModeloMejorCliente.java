/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public class ModeloMejorCliente {
    
    private String nit;
    private String nombreCliente;
    private double montoTotal;
    private int cantidadFacturas;
    private double porcentajeDelTotal;
    private int ranking;
    
     public ModeloMejorCliente(String nit, String nombreCliente, double montoTotal, 
                             int cantidadFacturas, double porcentajeDelTotal, int ranking) {
        this.nit = nit;
        this.nombreCliente = nombreCliente;
        this.montoTotal = montoTotal;
        this.cantidadFacturas = cantidadFacturas;
        this.porcentajeDelTotal = porcentajeDelTotal;
        this.ranking = ranking;
    }
    
    // GETTERS
    public String getNit() { return nit; }
    public String getNombreCliente() { return nombreCliente; }
    public double getMontoTotal() { return montoTotal; }
    public int getCantidadFacturas() { return cantidadFacturas; }
    public double getPorcentajeDelTotal() { return porcentajeDelTotal; }
    public int getRanking() { return ranking; }
    
    // Métodos formateados
    public String getMontoTotalFormateado() { return String.format("Q%.2f", montoTotal); }
    public String getPorcentajeFormateado() { return String.format("%.2f%%", porcentajeDelTotal); }
    public String getTicketPromedioFormateado() { 
        return String.format("Q%.2f", (cantidadFacturas > 0) ? montoTotal / cantidadFacturas : 0); 
    }
    
    @Override
    public String toString() {
        return String.format("#%d %s: Q%.2f (%d facturas - %.2f%%)", 
            ranking, nombreCliente, montoTotal, cantidadFacturas, porcentajeDelTotal);
    }
     
}
