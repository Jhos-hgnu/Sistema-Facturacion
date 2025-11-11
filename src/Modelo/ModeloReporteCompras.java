/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;



import java.math.BigDecimal;
import java.sql.Date;

public class ModeloReporteCompras{

    
    public record ModeloItem(long id, String nombre) {
        @Override public String toString() {
            return nombre;
        }
    }

    public record CompraFechaDTO(
            long idCompra,
            Date fecha,
            String proveedor,
            String formaPago,
            BigDecimal total,
            BigDecimal saldo 
    ) { }

   
    public record CompraProveedorDTO(
            long idCompra,
            Date fecha,
            String tipoPago,          
            BigDecimal total,        
            BigDecimal pagado,      
            BigDecimal saldo          
    ) { }
}
