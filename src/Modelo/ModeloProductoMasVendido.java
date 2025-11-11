/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public class ModeloProductoMasVendido {
    
    private int idProducto;
    private String nombreProducto;
    private String categoria;
    private int cantidadVendida;
    private double montoTotal;
    private double porcentajeDelTotal;
    private int ranking;
    
    // Constructor
    public ModeloProductoMasVendido(int idProducto, String nombreProducto, String categoria,
                                   int cantidadVendida, double montoTotal, 
                                   double porcentajeDelTotal, int ranking) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidadVendida = cantidadVendida;
        this.montoTotal = montoTotal;
        this.porcentajeDelTotal = porcentajeDelTotal;
        this.ranking = ranking;
    }
    
    // Getters
    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public String getCategoria() { return categoria; }
    public int getCantidadVendida() { return cantidadVendida; }
    public double getMontoTotal() { return montoTotal; }
    public double getPorcentajeDelTotal() { return porcentajeDelTotal; }
    public int getRanking() { return ranking; }
    
    // Métodos formateados para CSV
    public String getMontoTotalFormateado() { return String.format("Q%.2f", montoTotal); }
    public String getPorcentajeFormateado() { return String.format("%.2f%%", porcentajeDelTotal); }
    public String getPrecioPromedioFormateado() { 
        return String.format("Q%.2f", (cantidadVendida > 0) ? montoTotal / cantidadVendida : 0); 
    }
    
}
