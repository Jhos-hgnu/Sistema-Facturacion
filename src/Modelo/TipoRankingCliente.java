/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public enum TipoRankingCliente {
    
    POR_MONTO("Por Monto Total"),
    POR_FRECUENCIA("Por Número de Facturas");
    
    private final String descripcion;
    
    TipoRankingCliente(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
    
    
}
