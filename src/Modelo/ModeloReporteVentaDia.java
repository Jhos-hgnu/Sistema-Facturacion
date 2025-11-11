package Modelo;

public class ModeloReporteVentaDia {
    
    private String hora;
    private String numeroFactura;
    private String cliente;
    private String productos;
    private double total;
    private String vendedor;
    
     // Constructor
    public ModeloReporteVentaDia(String hora, String numeroFactura, String cliente, 
                          String productos, double total, String vendedor) {
        this.hora = hora;
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.productos = productos;
        this.total = total;
        this.vendedor = vendedor;
    }

    public String getHora() {
        return hora;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }

    public String getVendedor() {
        return vendedor;
    }
    
    
    
    
    
    
    
    
    
}
