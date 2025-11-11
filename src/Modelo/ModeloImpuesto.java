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


public class ModeloImpuesto {
    private Long idImpuestos;           
    private String nombre;
    private BigDecimal tasa;          

    public ModeloImpuesto() {}

    public ModeloImpuesto(Long idImpuestos, String nombre, BigDecimal tasa) {
        this.idImpuestos = idImpuestos;
        this.nombre = nombre;
        this.tasa = tasa;
    }

    public Long getIdImpuestos() { return idImpuestos; }
    public void setIdImpuestos(Long idImpuestos) { this.idImpuestos = idImpuestos; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getTasa() { return tasa; }
    public void setTasa(BigDecimal tasa) { this.tasa = tasa; }
}

