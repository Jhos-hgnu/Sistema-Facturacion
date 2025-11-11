/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;
/**
 *
 * @author anyi4
 */
public class ModeloReporteProductos {
        


public static class ProdMasVendidoDTO {
    private long idProducto;
    private String nombre;
    private String marca;
    private BigDecimal totalCant;
    private BigDecimal totalMonto;

    public ProdMasVendidoDTO(long idProducto, String nombre, String marca,
                             BigDecimal totalCant, BigDecimal totalMonto) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.marca = marca;
        this.totalCant = totalCant;
        this.totalMonto = totalMonto;
    }

    public long getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public BigDecimal getTotalCant() { return totalCant; }
    public BigDecimal getTotalMonto() { return totalMonto; }
}


public static class StockBajoDTO {
    private long idProducto;
    private String nombre;
    private String marca;
    private int stock;

    public StockBajoDTO(long idProducto, String nombre, String marca, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.marca = marca;
        this.stock = stock;
    }

    public long getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public int getStock() { return stock; }
}


}
