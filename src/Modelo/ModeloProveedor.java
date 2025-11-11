/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Madelin
 */


import java.time.LocalDate;

public class ModeloProveedor {
    private Integer idProveedor;
    private String nit;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String email;
    private String tipoPago;
    private Integer plazoCredito;
    private String representante;
    private String estado;
    private LocalDate fechaRegistro;

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }
    public Integer getPlazoCredito() { return plazoCredito; }
    public void setPlazoCredito(Integer plazoCredito) { this.plazoCredito = plazoCredito; }
    public String getRepresentante() { return representante; }
    public void setRepresentante(String representante) { this.representante = representante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
