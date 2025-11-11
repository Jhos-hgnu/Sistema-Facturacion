package Modelo;

import java.util.Date;

/**
 * Representa una compra registrada en la base de datos FARMACIA.COMPRAS.
 * Autor: Joshua Cirilo Alegría
 */
public class ModeloCompra {

    private long idCompra;        // ID_COMPRA NUMBER(12,0)
    private long idProveedor;     // ID_PROVEEDOR NUMBER(8,0)
    private Date fecha;           // FECHA DATE
    private String documento;     // DOCUMENTO CLOB (texto largo)
    private double totalBruto;    // TOTAL_BRUTO NUMBER(18,4)
    private String formaPago;     // FORMA_PAGO VARCHAR2(600)
    private String estado;        // ESTADO VARCHAR2(800)
    private long idUsuario;       // ID_USUARIO NUMBER(8,0)
    private Date fechaCompra;     // FECHA_COMPRA DATE

    // 🔹 Constructor vacío
    public ModeloCompra() {}

    // 🔹 Constructor con parámetros
    public ModeloCompra(long idCompra, long idProveedor, Date fecha, String documento,
                        double totalBruto, String formaPago, String estado,
                        long idUsuario, Date fechaCompra) {
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

    // ======================
    // Getters y Setters
    // ======================

    public long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
    }

    public long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(long idProveedor) {
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

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public String toString() {
        return "ModeloCompra{" +
                "idCompra=" + idCompra +
                ", idProveedor=" + idProveedor +
                ", fecha=" + fecha +
                ", documento='" + documento + '\'' +
                ", totalBruto=" + totalBruto +
                ", formaPago='" + formaPago + '\'' +
                ", estado='" + estado + '\'' +
                ", idUsuario=" + idUsuario +
                ", fechaCompra=" + fechaCompra +
                '}';
    }
}
