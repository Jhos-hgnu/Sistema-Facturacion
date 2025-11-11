package Modelo;

import java.math.BigDecimal;

public class ModeloProducto {
    private BigDecimal idProducto;       
    private String nombreProducto;
    private String marca;
    private String descripcion;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private int stock;
    private int idCategoria;
    private int idImpuesto;

    public BigDecimal getIdProducto() { return idProducto; }
    public void setIdProducto(BigDecimal idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(BigDecimal precioCosto) { this.precioCosto = precioCosto; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int getIdImpuesto() { return idImpuesto; }
    public void setIdImpuesto(int idImpuesto) { this.idImpuesto = idImpuesto; }
}
