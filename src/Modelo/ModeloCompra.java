package Modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jciri
 */

import java.util.Date;


public class ModeloCompra {
    
    private int idCompra;
    private int idProveedor;
    private Date fecha;
    private String documento;
    private double totalBruto;
    private String formaPago;
    private String estado;
    private int idUsuario;
    private Date fechaCompra;
    
    //===== Constructor =======
    public ModeloCompra() {}
    
    
    public ModeloCompra(int idCompra, int idProveedor, Date fecha, String documento, double totalBruto,
                  String formaPago, String estado, int idUsuario, Date fechaCompra) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.documento = documento;
        this.totalBruto = totalBruto;
        this.formaPago = formaPago;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.fechaCompra = fechaCompra;
    }
    
    
    //=== Getters and setters

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public double getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(double totalBruto) {
        this.totalBruto = totalBruto;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    
   
}
