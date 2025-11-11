/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;

/**
 * Modelo para la tabla FARMACIA.COMPRA_DETALLE
 * Representa el detalle de cada compra realizada.
 * Autor: Joshua Cirilo Alegría
 */
public class ModeloCompraDetalle {

    private long idDetalle;     // PK
    private long idCompra;      // FK → COMPRA
    private long idProducto;    // FK → PRODUCTO
    private BigDecimal cantidad;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal total;

    // ===== Constructor vacío =====
    public ModeloCompraDetalle() {}

    // ===== Constructor completo =====
    public ModeloCompraDetalle(long idDetalle, long idCompra, long idProducto,
                               BigDecimal cantidad, BigDecimal precio,
                               BigDecimal descuento, BigDecimal total) {
        this.idDetalle = idDetalle;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total;
    }

    // ===== Getters & Setters =====
    public long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ModeloCompraDetalle{" +
                "idDetalle=" + idDetalle +
                ", idCompra=" + idCompra +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", descuento=" + descuento +
                ", total=" + total +
                '}';
    }
}
