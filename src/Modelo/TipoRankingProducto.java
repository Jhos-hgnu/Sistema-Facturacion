/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author jhosu
 */
public enum TipoRankingProducto {

    POR_CANTIDAD("Por Cantidad Vendida"), // Ordenar por unidades vendidas
    POR_MONTO("Por Monto Total");            // Ordenar por dinero generado

    private final String descripcion;

    TipoRankingProducto(String descripcion) {
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
