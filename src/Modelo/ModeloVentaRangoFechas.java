/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public class ModeloVentaRangoFechas {
    
     private String fecha;
    private String hora;
    private String numeroFactura;
    private String cliente;
    private String productos;
    private double total;
    private String vendedor;
    private String tipoPago;
    
    public ModeloVentaRangoFechas(String fecha, String hora, String numeroFactura, String cliente,
                                 String productos, double total, String vendedor, String tipoPago) {
        this.fecha = fecha;
        this.hora = hora;
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.productos = productos;
        this.total = total;
        this.vendedor = vendedor;
        this.tipoPago = tipoPago;
    }
    
    // GETTERS
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getNumeroFactura() { return numeroFactura; }
    public String getCliente() { return cliente; }
    public String getProductos() { return productos; }
    public double getTotal() { return total; }
    public String getVendedor() { return vendedor; }
    public String getTipoPago() { return tipoPago; }
    
    // Métodos formateados
    public String getTotalFormateado() { return String.format("Q%.2f", total); }
    
    
}
