package Modelo;

import java.util.Date;

public class ModeloInventario {

    private long idInventario;
    private long idProducto;
    private int cantidad;
    private int stockAnterior;
    private int stockActual;
    private String movimiento; // Ej: "VENTA", "ENTRADA", "AJUSTE"
    private String motivo;
    private long idUsuario;
    private Date fecha;

    // === Constructores ===
    public ModeloInventario() {
    }

    public ModeloInventario(long idInventario, long idProducto, int cantidad, int stockAnterior,
                            int stockActual, String movimiento, String motivo, long idUsuario, Date fecha) {
        this.idInventario = idInventario;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockActual = stockActual;
        this.movimiento = movimiento;
        this.motivo = motivo;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
    }

    // === Getters y Setters ===
    public long getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(long idInventario) {
        this.idInventario = idInventario;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(int stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    // === Método toString() (opcional, útil para depuración) ===
    @Override
    public String toString() {
        return "Inventario{" +
                "idInventario=" + idInventario +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", stockAnterior=" + stockAnterior +
                ", stockActual=" + stockActual +
                ", movimiento='" + movimiento + '\'' +
                ", motivo='" + motivo + '\'' +
                ", idUsuario=" + idUsuario +
                ", fecha=" + fecha +
                '}';
    }
}
